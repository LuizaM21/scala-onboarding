package lessons.l08_solid

class LateFeePolicyShould extends munit.FunSuite {
  test("charge a flat fee when the invoice is late") {
    // setup

    val policy = FlatFee(BigDecimal("25"))

    // execute

    val fee = policy.fee(BigDecimal("1000.00"), daysLate = 3)

    // verify

    assertEquals(fee, BigDecimal("25.00"))
  }

  test("charge nothing when the invoice is not late") {
    // setup

    val policy = PercentFee(BigDecimal("2"))

    // execute

    val fee = policy.fee(BigDecimal("1000.00"), daysLate = 0)

    // verify

    assertEquals(fee, BigDecimal("0.00"))
  }

  test("charge a share of the amount for a percent fee") {
    // setup

    val policy = PercentFee(BigDecimal("2"))

    // execute

    val fee = policy.fee(BigDecimal("1234.50"), daysLate = 5)

    // verify

    assertEquals(fee, BigDecimal("24.69"))
  }

  test("charge per day for a daily fee") {
    // setup

    val policy = DailyFee(BigDecimal("1.50"))

    // execute

    val fee = policy.fee(BigDecimal("1000.00"), daysLate = 10)

    // verify

    assertEquals(fee, BigDecimal("15.00"))
  }

  test("add any policy's fee to the amount in the calculator") {
    // setup

    val calculator = LateFeeCalculator(DailyFee(BigDecimal("2.00")))

    // execute

    val total = calculator.totalDue(BigDecimal("100.00"), daysLate = 4)

    // verify

    assertEquals(total, BigDecimal("108.00"))
  }
}
