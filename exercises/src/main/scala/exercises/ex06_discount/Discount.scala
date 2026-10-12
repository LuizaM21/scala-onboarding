package exercises.ex06_discount

/** Apply a discount with `match`.
  *
  * Rules, in plain words:
  * - status `OPEN` and amount >= 100: the customer pays 90% (10% off).
  * - status `OPEN` and amount < 100: no discount.
  * - status `PAID`: no discount. It is already paid.
  * - status `OVERDUE`: no discount. Do not reward a late invoice.
  * - any other status: no discount.
  *
  * Return the payable amount with two decimal places, half up.
  * `payable("OPEN", 200)` is `180.00`. `payable("OPEN", 100)` is `90.00`.
  *
  * Hint: `status match { case "OPEN" if amount >= 100 => ... case _ => amount }`.
  * Hint: lesson 1 shows `match`.
  */
object Discount {
  def payable(status: String, amount: BigDecimal): BigDecimal =
    status match
    case "OPEN" if amount >= 100.00 => (amount * 0.9).setScale(2, BigDecimal.RoundingMode.HALF_UP)
    case "OPEN" if amount < 100.00 => amount.setScale(2, BigDecimal.RoundingMode.HALF_UP)
    case "PAID" if amount > 100.00 => amount.setScale(2, BigDecimal.RoundingMode.HALF_UP)
    case "OVERDUE" => amount.setScale(2, BigDecimal.RoundingMode.HALF_UP)
    case _ => amount.setScale(2, BigDecimal.RoundingMode.HALF_UP)

}
