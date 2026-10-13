package lessons.l10_behavioural

class DiscountsShould extends munit.FunSuite {
  test("take a percent off the amount") {
    // execute

    val payable = Checkout(Discounts.percentOff(10)).payable(BigDecimal("200.00"))

    // verify

    assertEquals(payable, BigDecimal("180.00"))
  }

  test("never go below zero with a fixed discount") {
    // execute

    val payable = Checkout(Discounts.fixedOff(50)).payable(BigDecimal("30.00"))

    // verify

    assertEquals(payable, BigDecimal("0.00"))
  }

  test("apply two discounts in order") {
    // setup

    val discount = Discounts.both(Discounts.percentOff(10), Discounts.fixedOff(30))

    // execute

    val payable = Checkout(discount).payable(BigDecimal("200.00"))

    // verify

    assertEquals(payable, BigDecimal("150.00"))
  }

  test("accept any function as a strategy") {
    // setup

    val halfPrice: Discounts.Discount = amount => amount / 2

    // execute

    val payable = Checkout(halfPrice).payable(BigDecimal("80.00"))

    // verify

    assertEquals(payable, BigDecimal("40.00"))
  }
}
