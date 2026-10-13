package exercises.ex14_open_closed

import scala.math.BigDecimal.RoundingMode

/** Add new discount rules without touching the engine. Lesson 8, letter O.
  *
  * `DiscountRule` and `DiscountEngine` are done. Do not edit them.
  * `NoDiscount` is a finished example rule.
  *
  * Write three rules. Each returns the percent off for one order, or `0`.
  * - `BulkDiscount(minAmount, off)`: `off` percent when the amount is at least `minAmount`.
  * - `LoyalSupplier(suppliers, off)`: `off` percent when the supplier is in the set.
  * - `EarlyPayment(off)`: `off` percent when the order is paid early.
  *
  * The engine asks every rule and uses the biggest percent. Rules do not add up.
  * When you are done, the engine works with your rules and you never opened it.
  *
  * Hint: `if (condition) off else BigDecimal(0)`.
  */
case class Order(supplier: String, amount: BigDecimal, paidEarly: Boolean)

trait DiscountRule {
  def percentOff(order: Order): BigDecimal
}

object NoDiscount extends DiscountRule {
  def percentOff(order: Order): BigDecimal = BigDecimal(0)
}

case class BulkDiscount(minAmount: BigDecimal, off: BigDecimal) extends DiscountRule {
  def percentOff(order: Order): BigDecimal = ???
}

case class LoyalSupplier(suppliers: Set[String], off: BigDecimal) extends DiscountRule {
  def percentOff(order: Order): BigDecimal = ???
}

case class EarlyPayment(off: BigDecimal) extends DiscountRule {
  def percentOff(order: Order): BigDecimal = ???
}

/** Closed for modification. Done. */
object DiscountEngine {
  def payable(order: Order, rules: List[DiscountRule]): BigDecimal = {
    val best = (NoDiscount :: rules).map(_.percentOff(order)).max
    (order.amount * (100 - best) / 100).setScale(2, RoundingMode.HALF_UP)
  }
}
