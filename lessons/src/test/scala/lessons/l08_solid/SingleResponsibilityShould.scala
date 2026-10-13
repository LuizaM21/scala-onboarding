package lessons.l08_solid

/** Each small object gets its own short test.
  * Compare with `ReportBefore`: one test would need CSV in and text out.
  */
class SingleResponsibilityShould extends munit.FunSuite {
  test("parse one row per non-blank line") {
    // setup

    val csv = "Acme SRL,100.00\n\nBeta SA, 50.00"

    // execute

    val rows = SupplierCsv.parse(csv)

    // verify

    assertEquals(
      rows,
      List(SupplierAmount("Acme SRL", BigDecimal("100.00")), SupplierAmount("Beta SA", BigDecimal("50.00")))
    )
  }

  test("sum amounts by supplier without any text involved") {
    // setup

    val rows = List(
      SupplierAmount("Acme SRL", BigDecimal("100.00")),
      SupplierAmount("Acme SRL", BigDecimal("20.50"))
    )

    // execute

    val totals = SupplierTotals.bySupplier(rows)

    // verify

    assertEquals(totals, Map("Acme SRL" -> BigDecimal("120.50")))
  }

  test("format totals sorted by supplier name") {
    // setup

    val totals = Map("Beta SA" -> BigDecimal("50.00"), "Acme SRL" -> BigDecimal("120.50"))

    // execute

    val text = SupplierReportText.format(totals)

    // verify

    assertEquals(text, "Acme SRL: 120.50\nBeta SA: 50.00")
  }

  test("give the same answer as the old version") {
    // setup

    val csv = "Acme SRL,100.00\nBeta SA,50.00\nAcme SRL,20.50"

    // execute

    val after = ReportAfter.run(csv)

    // verify

    assertEquals(after, ReportBefore.run(csv))
  }
}
