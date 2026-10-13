package lessons.l11_putting_it_together

/** Layer 2 — the application. One use case, one class, one `execute` method.
  *
  * The class name says the action: Send + OverdueReminders + Service.
  * Many Java and Scala services name their use cases the same way.
  *
  * The constructor lists every dependency. All of them are traits.
  * In a test you pass fakes. In the app you pass the real ones.
  */
case class ReminderSummary(sent: Int, totalFees: BigDecimal)

class SendOverdueRemindersService(
    invoices: InvoiceRepository,
    notifier: Notifier,
    clock: Clock,
    feePolicy: FeePolicy
) {
  def execute(): ReminderSummary = {
    val today = clock.today()
    val overdue = invoices.openInvoices().filter(_.isOverdue(today)).sortBy(_.number)
    val fees = overdue.map { invoice =>
      val fee = feePolicy.fee(invoice, today)
      val total = invoice.amount + fee
      notifier.send(
        invoice.supplierEmail,
        s"${invoice.number} is ${invoice.daysLate(today)} days late. Please pay $total (fee $fee)."
      )
      fee
    }
    ReminderSummary(sent = overdue.size, totalFees = fees.foldLeft(BigDecimal("0.00"))(_ + _))
  }
}
