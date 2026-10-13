package exercises.ex17_factory

class Ex17FactoryShould extends munit.FunSuite {
  test("build an invoice number from clean text") {
    // execute

    val number = InvoiceNumber.from(" inv-001 ")

    // verify

    assertEquals(number.map(_.value), Right("INV-001"))
  }

  test("refuse a blank invoice number") {
    // execute

    val number = InvoiceNumber.from("   ")

    // verify

    assertEquals(number, Left("invoice number is blank"))
  }

  test("refuse an invoice number with too few digits") {
    // execute

    val number = InvoiceNumber.from("INV-12")

    // verify

    assertEquals(number, Left("bad invoice number: INV-12"))
  }

  test("refuse an invoice number with the wrong prefix") {
    // execute

    val number = InvoiceNumber.from("BILL-001")

    // verify

    assertEquals(number, Left("bad invoice number: BILL-001"))
  }

  test("pick the plain format by name, ignoring case") {
    // execute

    val text = AmountFormat.forName(" Plain ").map(_.format(BigDecimal("120.5"), "EUR"))

    // verify

    assertEquals(text, Right("120.50 EUR"))
  }

  test("put the currency first in the accounting format") {
    // execute

    val text = AmountFormat.forName("accounting").map(_.format(BigDecimal("120.5"), "EUR"))

    // verify

    assertEquals(text, Right("EUR 120.50"))
  }

  test("show a negative amount in brackets in the accounting format") {
    // execute

    val text = AccountingFormat.format(BigDecimal("-75"), "USD")

    // verify

    assertEquals(text, "USD (75.00)")
  }

  test("refuse an unknown format name") {
    // execute

    val format = AmountFormat.forName("roman")

    // verify

    assertEquals(format, Left("unknown format: roman"))
  }
}
