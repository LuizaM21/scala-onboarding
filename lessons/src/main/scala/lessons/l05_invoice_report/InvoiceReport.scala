package lessons.l05_invoice_report

import lessons.l03_case_classes_traits.{Invoice, InvoiceStatus}
import lessons.l04_option_either_errors.Parse

import java.time.LocalDate
import scala.io.Source
import scala.math.BigDecimal.RoundingMode
import scala.util.matching.Regex

/** How many EUR is 1 unit of this currency.
  * USD 92/100 means 1 USD = 0.92 EUR.
  */
object Rates {
  private val Rate: Regex =
    """"([A-Z]{3})"\s*:\s*\{\s*"numerator"\s*:\s*(-?\d+)\s*,\s*"denominator"\s*:\s*(-?\d+)\s*\}""".r

  def parse(json: String): Map[String, BigDecimal] =
    Rate.findAllMatchIn(json).map { m =>
      val code = m.group(1)
      val value = BigDecimal(m.group(2)) / BigDecimal(m.group(3))
      code -> value
    }.toMap

  def load(): Map[String, BigDecimal] =
    parse(Resources.text("exchange_rates.json"))
}

/** Read a file from src/main/resources. The test classpath sees the same files. */
object Resources {
  def text(name: String): String = {
    val source = Source.fromResource(name)
    try source.mkString
    finally source.close()
  }

  /** Drop the header row. Ignore blank lines. */
  def csvRows(name: String): List[String] = {
    val lines = text(name).replace("\r", "").split("\n").toList.map(_.trim).filter(_.nonEmpty)
    if (lines.headOption.exists(_.startsWith("number,"))) lines.drop(1) else lines
  }
}

case class Report(
    valid: List[Invoice],
    rejected: List[String],
    bySupplierEur: Map[String, BigDecimal],
    byStatusEur: Map[InvoiceStatus, BigDecimal]
)

/** Lesson 5 — one small report.
  *
  * 1. Read each CSV row.
  * 2. Parse it. Keep failures in `rejected`.
  * 3. Convert each valid total to EUR.
  * 4. Sum by supplier and by status.
  */
object InvoiceReport {
  private def parseRow(line: String, today: LocalDate): Either[String, Invoice] = {
    val parts = line.split(",", -1).map(_.trim)
    if (parts.length != 6) Left(s"expected 6 columns: $line")
    else
      Parse.validateInvoice(
        number = parts(0),
        supplier = parts(1),
        currency = parts(2),
        total = parts(3),
        issuedOn = parts(4),
        dueDate = parts(5),
        today = today
      )
  }

  private def  toEur(invoice: Invoice, rates: Map[String, BigDecimal]): Option[BigDecimal] =
    rates.get(invoice.total.currency.toString).map { rate =>
      (invoice.total.amount * rate).setScale(2, RoundingMode.HALF_UP)
    }

  def build(lines: List[String], rates: Map[String, BigDecimal], today: LocalDate): Report = {
    val parsed = lines.map(parseRow(_, today))
    val valid = parsed.collect { case Right(invoice) => invoice }
    val rejected = parsed.collect { case Left(message) => message }
    val priced = valid.flatMap { invoice =>
      toEur(invoice, rates).map(invoice -> _)
    }
    val bySupplier = sumBy(priced.map { case (invoice, eur) => invoice.supplier -> eur })
    val byStatus = priced.groupBy { case (invoice, _) => invoice.status }.map { case (status, rows) =>
      status -> rows.map(_._2).foldLeft(BigDecimal("0.00"))(_ + _).setScale(2, RoundingMode.HALF_UP)
    }
    Report(valid, rejected, bySupplier, byStatus)
  }

  def fromResources(today: LocalDate): Report =
    build(Resources.csvRows("invoices.csv"), Rates.load(), today)

  def format(report: Report): String = {
    val suppliers = report.bySupplierEur.toList.sortBy(_._1).map { case (name, total) =>
      s"  $name: $total EUR"
    }.mkString("\n")
    val statuses = report.byStatusEur.toList.sortBy(_._1.toString).map { case (status, total) =>
      s"  $status: $total EUR"
    }.mkString("\n")
    s"""Valid invoices: ${report.valid.size}
       |Rejected rows: ${report.rejected.size}
       |By supplier (EUR):
       |$suppliers
       |By status (EUR):
       |$statuses""".stripMargin
  }

  private def sumBy(rows: List[(String, BigDecimal)]): Map[String, BigDecimal] =
    rows.groupBy(_._1).map { case (name, pairs) =>
      name -> pairs.map(_._2).foldLeft(BigDecimal("0.00"))(_ + _).setScale(2, RoundingMode.HALF_UP)
    }
}
