package exercises.ex16_dependency_inversion

import java.time.LocalDate
import scala.math.BigDecimal.RoundingMode

case class Unpaid(number: String, supplierEmail: String, amount: BigDecimal, currency: String)

trait Clock {
  def today(): LocalDate
}

trait UnpaidInvoices {
  def dueBefore(date: LocalDate): List[Unpaid]
}

trait Mailer {
  def send(to: String, subject: String): Unit
}

class ChaseUnpaidService(invoices: UnpaidInvoices, mailer: Mailer, clock: Clock) {
  def execute(): Int = {
    val unpaid = invoices.dueBefore(clock.today()).sortBy(_.number)
    unpaid.foreach { invoice =>
      val amount = invoice.amount.setScale(2, RoundingMode.HALF_UP)
      mailer.send(invoice.supplierEmail, s"Unpaid: ${invoice.number} ($amount ${invoice.currency})")
    }
    unpaid.size
  }
}
