package exercises.ex20_strategy

import java.time.{DayOfWeek, LocalDate}

object PaymentTerms {
  type Term = LocalDate => LocalDate

  def net(days: Int): Term = date => date.plusDays(days)

  def endOfMonth: Term = date => date.withDayOfMonth(date.lengthOfMonth)

  def endOfMonthPlus(days: Int): Term = endOfMonth.andThen(net(days))

  def nextWorkingDay(term: Term): Term = term.andThen { date =>
    date.getDayOfWeek match {
      case DayOfWeek.SATURDAY => date.plusDays(2)
      case DayOfWeek.SUNDAY   => date.plusDays(1)
      case _                  => date
    }
  }
}

case class Contract(supplier: String, term: PaymentTerms.Term) {
  def dueDate(issuedOn: LocalDate): LocalDate = term(issuedOn)
}
