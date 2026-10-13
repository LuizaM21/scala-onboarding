package exercises.ex14_open_closed

class Ex14OpenClosedShould extends munit.FunSuite {
  private val order = Order("Acme SRL", BigDecimal("1000.00"), paidEarly = false)

  test("give a bulk discount from the minimum amount up") {
    // setup

    val rule = BulkDiscount(minAmount = BigDecimal("1000"), off = BigDecimal("5"))

    // execute

    val atMinimum = rule.percentOff(order)
    val below = rule.percentOff(order.copy(amount = BigDecimal("999.99")))

    // verify

    assertEquals(atMinimum, BigDecimal("5"))
    assertEquals(below, BigDecimal(0))
  }

  test("give a loyalty discount only to listed suppliers") {
    // setup

    val rule = LoyalSupplier(Set("Acme SRL"), off = BigDecimal("3"))

    // execute

    val loyal = rule.percentOff(order)
    val other = rule.percentOff(order.copy(supplier = "Beta SA"))

    // verify

    assertEquals(loyal, BigDecimal("3"))
    assertEquals(other, BigDecimal(0))
  }

  test("give an early payment discount only when paid early") {
    // setup

    val rule = EarlyPayment(off = BigDecimal("2"))

    // execute

    val early = rule.percentOff(order.copy(paidEarly = true))
    val onTime = rule.percentOff(order)

    // verify

    assertEquals(early, BigDecimal("2"))
    assertEquals(onTime, BigDecimal(0))
  }

  test("use the biggest discount, not the sum") {
    // setup

    val rules = List(
      BulkDiscount(BigDecimal("500"), BigDecimal("5")),
      LoyalSupplier(Set("Acme SRL"), BigDecimal("3")),
      EarlyPayment(BigDecimal("2"))
    )

    // execute

    val payable = DiscountEngine.payable(order.copy(paidEarly = true), rules)

    // verify

    assertEquals(payable, BigDecimal("950.00"))
  }

  test("charge the full amount when no rule matches") {
    // setup

    val rules = List(EarlyPayment(BigDecimal("2")))

    // execute

    val payable = DiscountEngine.payable(order, rules)

    // verify

    assertEquals(payable, BigDecimal("1000.00"))
  }
}
