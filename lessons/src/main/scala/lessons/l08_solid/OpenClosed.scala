package lessons.l08_solid

import scala.math.BigDecimal.RoundingMode

/** O — Open/Closed.
  *
  * Open for extension, closed for modification.
  * You add a new behaviour by adding new code, not by editing code that already works.
  *
  * `LateFeeBefore` grows a new `case` every time finance invents a fee.
  * Each edit can break the cases that were fine. Each edit means a full retest.
  */
object LateFeeBefore {
  def fee(kind: String, amount: BigDecimal, daysLate: Int): BigDecimal =
    kind match {
      case "flat"    => BigDecimal("25.00")
      case "percent" => (amount * BigDecimal("0.02")).setScale(2, RoundingMode.HALF_UP)
      // next month: case "daily" => ...
      // the month after: case "capped" => ...
      case _ => BigDecimal("0.00")
    }
}

/** After: a small contract. Each fee is its own type.
  * A new fee is a new class. `LateFeeCalculator` does not change.
  */
trait LateFeePolicy {
  def fee(amount: BigDecimal, daysLate: Int): BigDecimal
}

/** Always the same fee, no matter how late. */
case class FlatFee(value: BigDecimal) extends LateFeePolicy {
  def fee(amount: BigDecimal, daysLate: Int): BigDecimal =
    if (daysLate <= 0) BigDecimal("0.00") else value.setScale(2, RoundingMode.HALF_UP)
}

/** A share of the invoice amount. `percent = 2` means 2%. */
case class PercentFee(percent: BigDecimal) extends LateFeePolicy {
  def fee(amount: BigDecimal, daysLate: Int): BigDecimal =
    if (daysLate <= 0) BigDecimal("0.00")
    else (amount * percent / 100).setScale(2, RoundingMode.HALF_UP)
}

/** Added later. Nothing above was edited. That is the whole point. */
case class DailyFee(perDay: BigDecimal) extends LateFeePolicy {
  def fee(amount: BigDecimal, daysLate: Int): BigDecimal =
    (perDay * daysLate.max(0)).setScale(2, RoundingMode.HALF_UP)
}

/** Closed for modification: it works with any policy, even one written next year. */
class LateFeeCalculator(policy: LateFeePolicy) {
  def totalDue(amount: BigDecimal, daysLate: Int): BigDecimal =
    (amount + policy.fee(amount, daysLate)).setScale(2, RoundingMode.HALF_UP)
}

/* A note for Scala.
 *
 * `enum Currency` with a `match` is fine. The list of currencies is fixed and the
 * compiler warns you when a `match` forgets a case.
 * Open/Closed matters when the list keeps growing, or when other teams add to it.
 * Ask: "Will someone add a new kind next month?" If yes, use a trait.
 */
