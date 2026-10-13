package exercises.ex16_dependency_inversion

import java.time.LocalDate
import scala.collection.mutable.ListBuffer

/** Fakes. Each one replaces a real tool with something the test controls. */
class FixedClock(day: LocalDate) extends Clock {
  def today(): LocalDate = day
}

/** Keeps the date it was asked about, so a test can check the service used the clock. */
class FakeUnpaidInvoices(all: List[(LocalDate, Unpaid)]) extends UnpaidInvoices {
  var askedFor: Option[LocalDate] = None

  def dueBefore(date: LocalDate): List[Unpaid] = {
    askedFor = Some(date)
    all.collect { case (due, invoice) if due.isBefore(date) => invoice }
  }
}

class RecordingMailer extends Mailer {
  val sent: ListBuffer[(String, String)] = ListBuffer.empty

  def send(to: String, subject: String): Unit = sent += (to -> subject)
}

class Ex16ChaseUnpaidShould extends munit.FunSuite {
  private val today = LocalDate.parse("2026-10-09")

  private val acme = Unpaid("INV-001", "ap@acme.example", BigDecimal("120.5"), "EUR")
  private val beta = Unpaid("INV-002", "ap@beta.example", BigDecimal("75"), "USD")

  test("ask for invoices due before the clock's today") {
    // setup

    val invoices = FakeUnpaidInvoices(Nil)
    val service = ChaseUnpaidService(invoices, RecordingMailer(), FixedClock(today))

    // execute

    service.execute()

    // verify

    assertEquals(invoices.askedFor, Some(today))
  }

  test("send one email per unpaid invoice, sorted by number") {
    // setup

    val invoices = FakeUnpaidInvoices(
      List(LocalDate.parse("2026-10-01") -> beta, LocalDate.parse("2026-09-01") -> acme)
    )
    val mailer = RecordingMailer()
    val service = ChaseUnpaidService(invoices, mailer, FixedClock(today))

    // execute

    val count = service.execute()

    // verify

    assertEquals(count, 2)
    assertEquals(
      mailer.sent.toList,
      List(
        "ap@acme.example" -> "Unpaid: INV-001 (120.50 EUR)",
        "ap@beta.example" -> "Unpaid: INV-002 (75.00 USD)"
      )
    )
  }

  test("skip an invoice that is due today") {
    // setup

    val invoices = FakeUnpaidInvoices(List(today -> acme))
    val mailer = RecordingMailer()
    val service = ChaseUnpaidService(invoices, mailer, FixedClock(today))

    // execute

    val count = service.execute()

    // verify

    assertEquals(count, 0)
  }

  // TODO: write this test. Delete `.ignore` when the body is ready.
  // Use the fakes above. No invoices at all: nothing is sent, and the count is 0.
  test("send nothing when there are no invoices".ignore) {
    // setup

    // execute

    // verify
  }
}
