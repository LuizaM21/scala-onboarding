package lessons.l11_putting_it_together

import java.time.LocalDate

/** Run this lesson with: sbt "lessons/run 11"
  *
  * This object is the "composition root": the one place that picks real implementations
  * and plugs them into the use case. In a Spring (Java) service, Spring does this job for you.
  *
  * Find each idea from lessons 8 to 10 in this folder:
  * - S: the use case only orchestrates. Fee math lives in `FeePolicy`, text output in `Notifier`.
  * - O: a new fee rule is a new `FeePolicy`. The use case does not change.
  * - L: any `InvoiceRepository` works, in memory or a future database one.
  * - I: each port has one method.
  * - D: the use case receives its tools. It never calls `LocalDate.now()` itself.
  * - Strategy: `FeePolicy`. Decorator: `RecordingNotifier`.
  */
object Demo {
  def run(): Unit = {
    println("Lesson 11 — Putting it together")
    println()

    val invoices = InMemoryInvoiceRepository(
      List(
        OpenInvoice("INV-001", "billing@acme.example", BigDecimal("1200.00"), LocalDate.parse("2026-09-09")),
        OpenInvoice("INV-002", "ap@beta.example", BigDecimal("300.00"), LocalDate.parse("2026-10-20")),
        OpenInvoice("INV-003", "ap@beta.example", BigDecimal("80000.00"), LocalDate.parse("2026-08-01"))
      )
    )

    val service = SendOverdueRemindersService(
      invoices = invoices,
      notifier = ConsoleNotifier,
      clock = FixedClock(LocalDate.parse("2026-10-09")),
      feePolicy = FeePolicy.dailyPercentCapped(cap = BigDecimal("500.00"))
    )

    println("Reminders:")
    val summary = service.execute()
    println()
    println(s"Sent ${summary.sent} reminders. Fees: ${summary.totalFees} EUR.")
  }
}
