package exercises.ex24_refactor

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import scala.math.BigDecimal.RoundingMode
import scala.util.Try

/** One possible clean version. Same behaviour, same tests, different shape. */
case class Row(number: String, supplier: String, currency: String, amount: BigDecimal, dueDate: LocalDate)

case class Line(row: Row, fee: BigDecimal, pay: BigDecimal)

/** Strategy: how much extra to charge for a late payment. */
trait LateFee {
  def fee(amountEur: BigDecimal, daysLate: Long): BigDecimal
}

object StandardLateFee extends LateFee {
  private val FlatFee = BigDecimal(10)
  private val PercentAfterGrace = BigDecimal("0.02")
  private val GraceDays = 30

  def fee(amountEur: BigDecimal, daysLate: Long): BigDecimal =
    if (daysLate <= 0) BigDecimal(0)
    else if (daysLate > GraceDays) amountEur * PercentAfterGrace
    else FlatFee
}

/** Port: EUR per one unit of a currency. */
trait Rates {
  def toEur(currency: String): Option[BigDecimal]
}

object FixedRates extends Rates {
  private val rates = Map(
    "EUR" -> BigDecimal(1),
    "USD" -> BigDecimal("0.92"),
    "RON" -> BigDecimal("0.20")
  )

  def toEur(currency: String): Option[BigDecimal] = rates.get(currency)
}

object RowParser {
  private val Open = "OPEN"

  /** `Right(Some(row))` an open invoice, `Right(None)` a valid row we ignore, `Left` a bad row. */
  def parse(line: String): Either[String, Option[Row]] = {
    val parts = line.split(",", -1).map(_.trim)
    if (parts.length != 6) Left(s"expected 6 columns: $line")
    else if (parts(5) != Open) Right(None)
    else
      for {
        amount <- Try(BigDecimal(parts(3))).toOption.toRight(s"bad amount: ${parts(3)}")
        due <- Try(LocalDate.parse(parts(4))).toOption.toRight(s"bad date: ${parts(4)}")
      } yield Some(Row(parts(0), parts(1), parts(2), amount, due))
  }
}

object PaymentText {
  private def money(value: BigDecimal): BigDecimal = value.setScale(2, RoundingMode.HALF_UP)

  def line(line: Line): String = {
    val feeText = if (line.fee > 0) s" (late fee ${money(line.fee)})" else ""
    s"${line.row.number} ${line.row.supplier} ${line.pay} EUR$feeText"
  }

  def summary(lines: List[Line], skipped: Int): String = {
    val total = money(lines.map(_.pay).sum)
    val body = lines.map(line(_) + "\n").mkString
    s"${body}Payments: ${lines.size}\nTotal: $total EUR\nSkipped: $skipped"
  }
}

object PaymentRun {
  def plan(lines: List[String], today: LocalDate): String =
    plan(lines, today, FixedRates, StandardLateFee)

  def plan(lines: List[String], today: LocalDate, rates: Rates, lateFee: LateFee): String = {
    val parsed = lines.map(RowParser.parse)
    val open = parsed.collect { case Right(Some(row)) => row }
    val badRows = parsed.count(_.isLeft)

    val priced = open.map(row => row -> rates.toEur(row.currency))
    val payable = priced.collect { case (row, Some(rate)) => price(row, rate, today, lateFee) }
    val unknownCurrency = priced.count(_._2.isEmpty)

    PaymentText.summary(payable, badRows + unknownCurrency)
  }

  private def price(row: Row, rate: BigDecimal, today: LocalDate, lateFee: LateFee): Line = {
    val eur = row.amount * rate
    val fee = lateFee.fee(eur, ChronoUnit.DAYS.between(row.dueDate, today))
    Line(row, fee, (eur + fee).setScale(2, RoundingMode.HALF_UP))
  }
}
