package lessons.l08_solid

import scala.math.BigDecimal.RoundingMode

/** S — Single Responsibility.
  *
  * A piece of code should have one reason to change.
  * "Reason" means a person or a team who asks for the change.
  *
  * The report below can change for three reasons:
  * - the CSV layout changes (the supplier system),
  * - the totals rule changes (accounting),
  * - the text layout changes (the manager who reads it).
  *
  * `ReportBefore` mixes all three in one method.
  * To test the totals rule you must build CSV text and read formatted text.
  * One test checks three things. When it is red you do not know which part broke.
  */
object ReportBefore {
  def run(csv: String): String = {
    val rows = csv.split("\n").toList.map(_.trim).filter(_.nonEmpty).map { line =>
      val parts = line.split(",")
      parts(0) -> BigDecimal(parts(1))
    }
    val totals = rows.groupBy(_._1).map { case (supplier, pairs) =>
      supplier -> pairs.map(_._2).sum.setScale(2, RoundingMode.HALF_UP)
    }
    totals.toList.sortBy(_._1).map { case (supplier, total) => s"$supplier: $total" }.mkString("\n")
  }
}

/** After: three small objects, one job each. Each one has its own tests. */
case class SupplierAmount(supplier: String, amount: BigDecimal)

/** Job 1: read text. Knows the CSV layout. Knows nothing about totals. */
object SupplierCsv {
  def parse(csv: String): List[SupplierAmount] =
    csv.split("\n").toList.map(_.trim).filter(_.nonEmpty).map { line =>
      val parts = line.split(",")
      SupplierAmount(parts(0).trim, BigDecimal(parts(1).trim))
    }
}

/** Job 2: the business rule. No text in, no text out. Easy to test. */
object SupplierTotals {
  def bySupplier(rows: List[SupplierAmount]): Map[String, BigDecimal] =
    rows.groupBy(_.supplier).map { case (supplier, items) =>
      supplier -> items.map(_.amount).sum.setScale(2, RoundingMode.HALF_UP)
    }
}

/** Job 3: the layout. Change the layout here and nothing else moves. */
object SupplierReportText {
  def format(totals: Map[String, BigDecimal]): String =
    totals.toList.sortBy(_._1).map { case (supplier, total) => s"$supplier: $total" }.mkString("\n")
}

/** The glue. It only calls the three jobs in order. */
object ReportAfter {
  def run(csv: String): String =
    SupplierReportText.format(SupplierTotals.bySupplier(SupplierCsv.parse(csv)))
}
