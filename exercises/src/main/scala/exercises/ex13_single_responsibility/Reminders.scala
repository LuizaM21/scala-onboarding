package exercises.ex13_single_responsibility

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import scala.util.Try

/** Split one big job into three small ones. Lesson 8, letter S.
  *
  * `RemindersBefore.run` below already works. Read it first. It does three jobs at once:
  * 1. read a CSV line,
  * 2. decide which rows are overdue,
  * 3. write the reminder text.
  *
  * Fill the three small objects. Each does one job. `RemindersAfter.run` is already
  * written. It glues them together. Do not change it.
  * One test checks that the new version gives the same answer as the old one.
  *
  * Rules:
  * - A line is `number,supplier,dueDate`. Trim each column.
  *   Not 3 columns, or a bad date: `None`.
  * - Overdue means the due date is before today. Sort by due date, oldest first.
  * - Text: `Acme SRL: INV-001 is 8 days late`.
  *
  * Hint: copy pieces from `RemindersBefore`. That is how a real refactor starts.
  * Hint: `Try(LocalDate.parse(text)).toOption` turns a bad date into `None`.
  */
object RemindersBefore {
  def run(lines: List[String], today: LocalDate): List[String] = {
    val rows = lines.flatMap { line =>
      val parts = line.split(",", -1).map(_.trim)
      if (parts.length != 3) None
      else Try(LocalDate.parse(parts(2))).toOption.map(due => (parts(0), parts(1), due))
    }
    rows
      .filter { case (_, _, due) => due.isBefore(today) }
      .sortBy { case (_, _, due) => due.toEpochDay }
      .map { case (number, supplier, due) =>
        s"$supplier: $number is ${ChronoUnit.DAYS.between(due, today)} days late"
      }
  }
}

/** One row after parsing. Do not change this shape. The tests build it. */
case class DueRow(number: String, supplier: String, dueDate: LocalDate)

/** Job 1: read one CSV line. */
object DueCsv {
  def parse(line: String): Option[DueRow] =
    line.split(",").toList.map(_.trim).toList match {
      case List(number, supplier, dueDate) => 
        Try(LocalDate.parse(dueDate)).toOption
          .map(date => DueRow(number, supplier, date))
      case _ => None
      
    }
    
}

/** Job 2: the business rule. */
object OverdueRule {
  def overdue(rows: List[DueRow], today: LocalDate): List[DueRow] = ???
}

/** Job 3: the text. */
object ReminderText {
  def line(row: DueRow, today: LocalDate): String = ???
}

/** The glue. Already done. */
object RemindersAfter {
  def run(lines: List[String], today: LocalDate): List[String] =
    OverdueRule.overdue(lines.flatMap(DueCsv.parse), today).map(ReminderText.line(_, today))
}
