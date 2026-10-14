package exercises.ex11_report

import scala.util.Try

case class 
CsvParser(
          number: String, 
          supplier: String, 
          currency: String, 
          total: BigDecimal
         )
/** A tiny supplier report.
  *
  * Each line has 4 columns: number, supplier, currency, total.
  * Same messages as exercise 7 if you want to reuse that idea.
  * You do not have to call exercise 7. This file should stand alone.
  *
  * Steps:
  * 1. Parse each line.
  * 2. Skip invalid lines.
  * 3. Convert the total to EUR with `rates`. Skip a line when the rate is missing.
  * 4. Sum EUR totals by supplier. Two decimal places, half up.
  *
  * `rates` means "how many EUR is 1 unit of that currency".
  *
  * Hint: `parse(...).toOption` turns `Right` into `Some` and `Left` into `None`.
  * Hint: lesson 5 does this with the full invoice file.
  */
object MiniReport {

  // ─────────────────────────────────────────────────────────────
  // parseLine: checks ONE line of text.
  //   - If the line is good  → Right(CsvParser(...))
  //   - If the line is bad   → Left("error message")
  // ─────────────────────────────────────────────────────────────
  private def parseLine(line: String): Either[String, CsvParser] =
    line.split(",").map(_.trim) match
      // If there are exactly 4 parts, give them names
      case Array(number, supplier, currency, total) =>
        if number.isEmpty then Left("the number is blank")
        else if supplier.isEmpty then Left("missing supplier")
        else if currency.isEmpty then Left("missing currency")
        else
          Try(BigDecimal(total)).toOption match
            case None => Left("total is not a number")
            case Some(total) =>
              Right(CsvParser(number, supplier, currency, total))
      case _ => Left("line must have 4 columns")

  // ─────────────────────────────────────────────────────────────
  // totalsBySupplier: processes ALL lines and returns
  //   supplier → total in EUR
  //
  // Example input:
  //   lines = List("1,Acme,USD,100", "2,Bolt,EUR,20", "3,Acme,EUR,10", "broken")
  //   rates = Map("USD" -> 0.9, "EUR" -> 1)
  // ─────────────────────────────────────────────────────────────
  def totalsBySupplier(lines: List[String], rates: Map[String, BigDecimal]): Map[String, BigDecimal] =
    // parse every line, keep only the good ones
    val validated: List[CsvParser] =
      lines.flatMap(line => parseLine(line).toOption)

    // convert each total to EUR
    val eurAmounts: List[(String, BigDecimal)] =
      validated.flatMap(record => rates.get(record.currency)
                .map(rate => (record.supplier, record.total * rate)))

    // put the pairs into buckets, one bucket per supplier.
    // pair._1 is the first part of the pair (the supplier)
    val grouped: Map[String, List[(String, BigDecimal)]] =
      eurAmounts.groupBy(pair => pair._1)

    // turn each bucket into one rounded number
    grouped.view  // changes only the values of the Map, keys stay the same
      .mapValues(pairs => pairs.map(pair => pair._2).sum.setScale(2, BigDecimal.RoundingMode.HALF_UP))
      .toMap


}
