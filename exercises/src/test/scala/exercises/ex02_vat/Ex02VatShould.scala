package exercises.ex02_vat

class Ex02VatShould extends munit.FunSuite {
  test("take 19 percent of 100") {
    assertEquals(Vat.vatOf(BigDecimal("100.00"), 19), BigDecimal("19.00"))}

  test("return 0.00 when the percent is 0") {
    assertEquals(Vat.vatOf(BigDecimal("10.00"), 0), BigDecimal("0.00"))}

  test("return 0.00 when the net is 0") {
    assertEquals(Vat.vatOf(BigDecimal("0.00"), 19), BigDecimal("0.00"))}

  test("take 21 percent of 10") {
    assertEquals(Vat.vatOf(BigDecimal("10.00"), 21), BigDecimal("2.10"))}

  test("round half up to two decimals") {
    assertEquals(Vat.vatOf(BigDecimal("1.01"), 19), BigDecimal("0.19"))}

}
