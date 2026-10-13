package lessons.l09_creational_structural

import lessons.l03_case_classes_traits.{Invoice, InvoiceStatus, Money}

import java.time.LocalDate

/** Builder — build a big object step by step.
  *
  * In Java a Builder is a separate class:
  *   new InvoiceBuilder().number("INV-1").supplier("Acme").build()
  *
  * Scala mostly does not need one. Two language features do the same job:
  * - named arguments with default values,
  * - `copy`, which changes one field and keeps the rest.
  *
  * Where it still shines for you: test data.
  * A "test data builder" gives a valid invoice by default.
  * Each test changes only the field it cares about. The test reads like the test case.
  */
case class PaymentRequest(
    invoiceNumber: String,
    amount: BigDecimal,
    currency: String = "EUR",
    urgent: Boolean = false,
    note: String = ""
)

/** A test data builder. Kept in main so the lesson tests and the demo can use it.
  * In a real project it lives in the test folder.
  */
object AnInvoice {

  /** A valid, boring invoice. Every field has a sensible value. */
  val default: Invoice = Invoice(
    number = "INV-001",
    supplier = "Acme SRL",
    total = Money.eur("100.00"),
    issuedOn = LocalDate.parse("2026-10-01"),
    dueDate = LocalDate.parse("2026-10-31"),
    status = InvoiceStatus.Open
  )

  /** Small named steps. Each returns a new invoice, so they chain. */
  extension (invoice: Invoice) {
    def from(supplier: String): Invoice = invoice.copy(supplier = supplier)
    def worth(amount: String): Invoice = invoice.copy(total = Money.eur(amount))
    def dueOn(date: String): Invoice = invoice.copy(dueDate = LocalDate.parse(date))
    def paid: Invoice = invoice.copy(status = InvoiceStatus.Paid)
  }
}
