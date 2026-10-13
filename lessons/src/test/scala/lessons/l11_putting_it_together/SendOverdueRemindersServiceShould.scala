package lessons.l11_putting_it_together

import java.time.LocalDate

/** Every dependency is a fake. No real clock, no real email.
  * The test controls "today" and reads back each message.
  */
class SendOverdueRemindersServiceShould extends munit.FunSuite {
  private val today = LocalDate.parse("2026-10-09")

  private def invoice(number: String, amount: String, due: String): OpenInvoice =
    OpenInvoice(number, s"$number@example.com", BigDecimal(amount), LocalDate.parse(due))

  test("remind only the overdue invoices") {
    // setup

    val notifier = RecordingNotifier(SilentNotifier)
    val service = SendOverdueRemindersService(
      InMemoryInvoiceRepository(
        List(invoice("INV-001", "100.00", "2026-10-01"), invoice("INV-002", "100.00", "2026-10-09"))
      ),
      notifier,
      FixedClock(today),
      FeePolicy.free
    )

    // execute

    val summary = service.execute()

    // verify

    assertEquals(summary.sent, 1)
    assertEquals(notifier.sent.map(_._1).toList, List("INV-001@example.com"))
  }

  test("add the fee from the policy to the message") {
    // setup

    val notifier = RecordingNotifier(SilentNotifier)
    val service = SendOverdueRemindersService(
      InMemoryInvoiceRepository(List(invoice("INV-001", "1000.00", "2026-09-29"))),
      notifier,
      FixedClock(today),
      FeePolicy.dailyPercentCapped(cap = BigDecimal("500.00"))
    )

    // execute

    val summary = service.execute()

    // verify

    assertEquals(summary.totalFees, BigDecimal("10.00"))
    assertEquals(
      notifier.sent.head._2,
      "INV-001 is 10 days late. Please pay 1010.00 (fee 10.00)."
    )
  }

  test("never charge more than the cap") {
    // setup

    val policy = FeePolicy.dailyPercentCapped(cap = BigDecimal("50.00"))
    val big = invoice("INV-009", "100000.00", "2026-08-01")

    // execute

    val fee = policy.fee(big, today)

    // verify

    assertEquals(fee, BigDecimal("50.00"))
  }

  test("send nothing when no invoice is overdue") {
    // setup

    val notifier = RecordingNotifier(SilentNotifier)
    val service = SendOverdueRemindersService(
      InMemoryInvoiceRepository(Nil),
      notifier,
      FixedClock(today),
      FeePolicy.free
    )

    // execute

    val summary = service.execute()

    // verify

    assertEquals(summary, ReminderSummary(0, BigDecimal("0.00")))
    assert(notifier.sent.isEmpty)
  }
}
