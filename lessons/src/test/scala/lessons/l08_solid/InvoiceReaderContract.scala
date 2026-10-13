package lessons.l08_solid

/** A contract test. Liskov in practice.
  *
  * The tests are written once, against the parent type `InvoiceReader`.
  * Each implementation gets a small class below that says how to build it.
  * MUnit runs every test once per implementation.
  *
  * `abstract class` means MUnit does not run this class by itself.
  * Only the two classes at the bottom run.
  */
abstract class InvoiceReaderContract extends munit.FunSuite {

  /** Build a reader that already holds these invoices. */
  def readerWith(data: Map[String, BigDecimal]): InvoiceReader

  test("find an invoice that exists") {
    // setup

    val reader = readerWith(Map("INV-001" -> BigDecimal("120.50")))

    // execute

    val found = reader.find("INV-001")

    // verify

    assertEquals(found, Some(BigDecimal("120.50")))
  }

  test("return None for an unknown number") {
    // setup

    val reader = readerWith(Map("INV-001" -> BigDecimal("120.50")))

    // execute

    val found = reader.find("INV-999")

    // verify

    assertEquals(found, None)
  }

  test("work with code that only knows the parent type") {
    // setup

    val reader = readerWith(Map("INV-001" -> BigDecimal("120.50")))

    // execute

    val amount = Lookup.amountOrZero(reader, "INV-404")

    // verify

    assertEquals(amount, BigDecimal("0.00"))
  }
}

class InvoiceArchiveShould extends InvoiceReaderContract {
  def readerWith(data: Map[String, BigDecimal]): InvoiceReader = InvoiceArchive(data)
}

class InMemoryInvoiceStoreShould extends InvoiceReaderContract {
  def readerWith(data: Map[String, BigDecimal]): InvoiceReader = {
    val store = InMemoryInvoiceStore()
    data.foreach { case (number, amount) => store.save(number, amount) }
    store
  }

  test("find an invoice after it was saved") {
    // setup

    val store = InMemoryInvoiceStore()

    // execute

    store.save("INV-002", BigDecimal("75.00"))

    // verify

    assertEquals(store.find("INV-002"), Some(BigDecimal("75.00")))
  }
}
