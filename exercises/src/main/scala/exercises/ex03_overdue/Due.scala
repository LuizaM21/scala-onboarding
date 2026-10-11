package exercises.ex03_overdue

import java.time.LocalDate

/** Decide if an invoice is overdue.
  *
  * Task: overdue means the due date is before today.
  * A due date of today is not overdue. A future due date is not overdue.
  *
  * Hint: `due.isBefore(today)` is a Boolean.
  * Hint: lesson 3 uses the same rule on `Invoice`.
  */
object Due {
  def isOverdue(due: LocalDate, today: LocalDate): Boolean =
     if due.isBefore(today) then true
      else if due.equals(today) then false
        else false
}
