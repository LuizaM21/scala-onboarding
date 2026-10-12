package exercises.ex05_filter

/** Keep the large amounts.
  *
  * Task: return every amount that is greater than or equal to `limit`.
  * Keep the original order. Do not change the input list.
  *
  * Hint: `amounts.filter(amount => amount >= limit)`.
  * Hint: lesson 2 `atLeast` is the same idea.
  */
object Filters {
  def atLeast(amounts: List[BigDecimal], limit: BigDecimal): List[BigDecimal] =
    amounts.filter(_ >= limit)
}
