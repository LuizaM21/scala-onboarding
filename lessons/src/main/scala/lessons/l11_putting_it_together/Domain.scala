package lessons.l11_putting_it_together

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import scala.math.BigDecimal.RoundingMode

/** Layer 1 — the domain. Business words and rules. No files, no database, no console.
  *
  * The small app in this lesson sends a reminder for every overdue invoice,
  * with a late fee added. Each file is one layer:
  *
  *   Domain.scala          what an invoice is, what "overdue" means, which ports we need
  *   Application.scala     one use case that follows the steps
  *   Infrastructure.scala  real and in-memory implementations of the ports
  *   Demo.scala            the wiring: pick implementations, plug them in, run
  *
  * Arrows point inward. Infrastructure knows the domain. The domain knows nothing outside.
  * This is called "ports and adapters", or hexagonal architecture.
  */
case class OpenInvoice(number: String, supplierEmail: String, amount: BigDecimal, dueDate: LocalDate) {
  def daysLate(today: LocalDate): Long = ChronoUnit.DAYS.between(dueDate, today).max(0)

  def isOverdue(today: LocalDate): Boolean = daysLate(today) > 0
}

/** Strategy (lesson 10). The use case does not know which fee rule is active. */
trait FeePolicy {
  def fee(invoice: OpenInvoice, today: LocalDate): BigDecimal
}

object FeePolicy {

  /** 0.1% of the amount per day late, never more than `cap`. */
  def dailyPercentCapped(cap: BigDecimal): FeePolicy = new FeePolicy {
    def fee(invoice: OpenInvoice, today: LocalDate): BigDecimal =
      (invoice.amount * BigDecimal("0.001") * invoice.daysLate(today))
        .min(cap)
        .setScale(2, RoundingMode.HALF_UP)
  }

  val free: FeePolicy = (_, _) => BigDecimal("0.00")
}

/* Ports. Small traits (letter I) that the use case depends on (letter D).
 * The domain says WHAT it needs. Infrastructure decides HOW.
 */

trait InvoiceRepository {
  def openInvoices(): List[OpenInvoice]
}

trait Notifier {
  def send(to: String, message: String): Unit
}

trait Clock {
  def today(): LocalDate
}
