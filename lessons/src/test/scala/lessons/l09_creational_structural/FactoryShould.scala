package lessons.l09_creational_structural

class FactoryShould extends munit.FunSuite {
  test("build an IBAN from text with spaces") {
    // execute

    val iban = Iban.from("ro49 aaaa 1b31 0075 9384 0000")

    // verify

    assertEquals(iban.map(_.value), Right("RO49AAAA1B31007593840000"))
  }

  test("refuse text that is not an IBAN") {
    // execute

    val iban = Iban.from("hello")

    // verify

    assertEquals(iban, Left("not an IBAN: hello"))
  }

  test("pick the CSV exporter for csv") {
    // setup

    val rows = List("Acme SRL" -> BigDecimal("120.50"))

    // execute

    val text = Exporter.forFormat("CSV").map(_.render(rows))

    // verify

    assertEquals(text, Right("Acme SRL,120.50"))
  }

  test("refuse an unknown format") {
    // execute

    val exporter = Exporter.forFormat("xml")

    // verify

    assertEquals(exporter.left.toOption, Some("unknown format: xml"))
  }
}
