package exercises.ex08_group

/** Sum amounts per supplier.
  *
  * Task: each pair is (supplier, amount). Add the amounts that share a name.
  * An empty list returns an empty map. Use two decimal places.
  *
  * Hint: `rows.groupBy(_._1)` buckets rows by the name.
  * Hint: lesson 2 `totalsByName` is this function.
  */
object GroupTotals {
  def bySupplier(rows: List[(String, BigDecimal)]): Map[String, BigDecimal] =
    
    // groupBy: put the rows in groups using the first part of each pair (_._1)
    rows.groupBy(_._1).map{case (supplier , amount) =>
      // case (supplier, amount): unpack each map entry into its key
      // and its value (the list of that supplier's rows).

      // amount.map(_._2): keep only the second part of each pair, the numbers.
      // foldLeft(BigDecimal("0.00"))(_ + _): add them up, starting from 0.00.
      val sum = amount.map(_._2).foldLeft(BigDecimal("0.00"))(_+_)

      // Build the (supplier -> total) pair, rounded to 2 decimals (0.005 goes up).
      // Because map returns pairs, the final result is a Map[String, BigDecimal].
      supplier -> sum.setScale(2, BigDecimal.RoundingMode.HALF_UP)
    }
}
