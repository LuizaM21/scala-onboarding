package lessons.l09_creational_structural

import lessons.l03_case_classes_traits.InvoiceStatus
import AnInvoice.*

import java.time.LocalDate

/** The test data builder in use.
  * Each test changes only the field that matters to it. The rest is a sensible default.
  */
class AnInvoiceShould extends munit.FunSuite {
  test("treat an invoice due yesterday as overdue") {
    // setup

    val invoice = AnInvoice.default.dueOn("2026-10-08")

    // execute

    val overdue = invoice.isOverdue(LocalDate.parse("2026-10-09"))

    // verify

    assert(overdue)
  }

  test("change one field and keep the rest") {
    // setup

    val invoice = AnInvoice.default.from("Beta SA").paid

    // verify

    assertEquals(invoice.supplier, "Beta SA")
    assertEquals(invoice.status, InvoiceStatus.Paid)
    assertEquals(invoice.number, AnInvoice.default.number)
  }

  test("fill only the named arguments and use defaults for the rest") {
    // execute

    val request = PaymentRequest(invoiceNumber = "INV-001", amount = BigDecimal("10.00"))

    // verify

    assertEquals(request.currency, "EUR")
    assertEquals(request.urgent, false)
  }
}
