package exercises.ex16_dependency_inversion

import java.time.LocalDate

/** A use case that receives its tools. Lesson 8, letters I and D. Lesson 11 for the shape.
  *
  * Part 1 — the code.
  * Write `ChaseUnpaidService.execute`:
  * 1. Ask the clock for today.
  * 2. Ask `invoices` for everything due before today.
  * 3. Send one email per invoice, sorted by number.
  *    To: the supplier email. Subject: `Unpaid: INV-001 (120.50 EUR)`.
  * 4. Return how many emails were sent.
  *
  * Never call `LocalDate.now()` here. That is the whole exercise.
  *
  * Part 2 — the test.
  * Open `Ex16ChaseUnpaidShould`. The fakes are already written.
  * The last test is ignored. Remove `.ignore` and write it, like exercise 12.
  *
  * Hint: `invoices.dueBefore(clock.today())`.
  * Hint: `amount.setScale(2, RoundingMode.HALF_UP)` keeps two decimals in the subject.
  */
case class Unpaid(number: String, supplierEmail: String, amount: BigDecimal, currency: String)

/** Small ports. Each has one method, so a fake is one line. */
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
  def execute(): Int = ???
}
