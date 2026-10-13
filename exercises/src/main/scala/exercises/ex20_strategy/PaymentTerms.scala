package exercises.ex20_strategy

import java.time.LocalDate

/** Payment terms as strategies. Lesson 10, Strategy.
  *
  * A payment term turns the issue date into a due date.
  * Each supplier contract picks one. The invoice code does not care which.
  * Here a strategy is a plain function: `LocalDate => LocalDate`.
  *
  * Write four strategies:
  * - `net(days)`        issue date plus `days`.             net(30) on 2026-01-15 is 2026-02-14
  * - `endOfMonth`       last day of the issue month.        2026-02-10 is 2026-02-28
  * - `endOfMonthPlus(days)`  end of month, then plus days.  plus 15 on 2026-01-10 is 2026-02-15
  * - `nextWorkingDay(term)`  apply `term`; if the result is Saturday or Sunday, move it to Monday.
  *
  * `Contract` is done. It only calls the strategy it was given.
  *
  * Hint: `date.plusDays(n)`, `date.lengthOfMonth`, `date.withDayOfMonth(n)`.
  * Hint: `date.getDayOfWeek` returns `java.time.DayOfWeek.SATURDAY`, `SUNDAY`, and so on.
  * Hint: try to write `endOfMonthPlus` using `endOfMonth` and `andThen`. Functions combine.
  */
object PaymentTerms {
  type Term = LocalDate => LocalDate

  def net(days: Int): Term = ???

  def endOfMonth: Term = ???

  def endOfMonthPlus(days: Int): Term = ???

  def nextWorkingDay(term: Term): Term = ???
}

case class Contract(supplier: String, term: PaymentTerms.Term) {
  def dueDate(issuedOn: LocalDate): LocalDate = term(issuedOn)
}
