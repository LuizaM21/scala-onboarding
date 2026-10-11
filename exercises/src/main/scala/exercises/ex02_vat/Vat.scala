package exercises.ex02_vat

/** Work out the VAT part of a net amount.
  *
  * Task: `vatOf(100, 19)` is the 19% VAT on 100, which is `19.00`.
  * Use two decimal places. Round half up. `vatOf(1.01, 19)` is `0.19`.
  * A percent of 0 returns `0.00`.
  *
  * Hint: `(net * percent) / 100`, then `setScale(2, BigDecimal.RoundingMode.HALF_UP)`.
  * Hint: lesson 1 shows a function that returns a value.
  */
object Vat {
  def vatOf(net: BigDecimal, percent: Int): BigDecimal =
    (net * percent / 100).setScale(2, BigDecimal.RoundingMode.HALF_UP)

}
