package exercises.ex04_sum

class Ex04SumShould extends munit.FunSuite {
  test("sum an empty list as 0.00") {
    assertEquals(Totals.sumAmounts(Nil), BigDecimal("0.00"))}

  test("sum a single amount") {
    assertEquals(Totals.sumAmounts(List(BigDecimal("10.50"))), BigDecimal("10.50"))}

  test("sum several amounts") {
    // setup

    val amounts = List(BigDecimal("10.00"), BigDecimal("2.50"), BigDecimal("0.25"))

    // execute

    val sum = Totals.sumAmounts(amounts)

    // verify

    assertEquals(sum, BigDecimal("12.75"))
  }

  test("include a negative amount") {
    // setup

    val amounts = List(BigDecimal("-1.50"), BigDecimal("2.00"))

    // execute

    val sum = Totals.sumAmounts(amounts)

    // verify

    assertEquals(sum, BigDecimal("0.50"))
  }
}
