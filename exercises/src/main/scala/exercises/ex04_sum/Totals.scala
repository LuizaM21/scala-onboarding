package exercises.ex04_sum

/** Add a list of amounts.
  *
  * Task: sum the list. An empty list returns `0.00`.
  * Keep two decimal places.
  *
  * Hint: `amounts.foldLeft(BigDecimal("0.00"))((sum, next) => sum + next)`.
  * Hint: lesson 2 does this in `Collections.total`.
  */
object Totals {
  def sumAmounts(amounts: List[BigDecimal]): BigDecimal =
    amounts.foldLeft(BigDecimal("0.00"))(_ + _).setScale(2, BigDecimal.RoundingMode.HALF_UP)
}
