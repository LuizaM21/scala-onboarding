package exercises.ex13_single_responsibility

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import scala.util.Try

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

case class DueRow(number: String, supplier: String, dueDate: LocalDate)

object DueCsv {
  def parse(line: String): Option[DueRow] = {
    val parts = line.split(",", -1).map(_.trim)
    if (parts.length != 3) None
    else Try(LocalDate.parse(parts(2))).toOption.map(due => DueRow(parts(0), parts(1), due))
  }
}

object OverdueRule {
  def overdue(rows: List[DueRow], today: LocalDate): List[DueRow] =
    rows.filter(_.dueDate.isBefore(today)).sortBy(_.dueDate.toEpochDay)
}

object ReminderText {
  def line(row: DueRow, today: LocalDate): String =
    s"${row.supplier}: ${row.number} is ${ChronoUnit.DAYS.between(row.dueDate, today)} days late"
}

object RemindersAfter {
  def run(lines: List[String], today: LocalDate): List[String] =
    OverdueRule.overdue(lines.flatMap(DueCsv.parse), today).map(ReminderText.line(_, today))
}
