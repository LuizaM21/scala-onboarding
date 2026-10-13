package exercises.ex14_open_closed

import scala.math.BigDecimal.RoundingMode

case class Order(supplier: String, amount: BigDecimal, paidEarly: Boolean)

trait DiscountRule {
  def percentOff(order: Order): BigDecimal
}

object NoDiscount extends DiscountRule {
  def percentOff(order: Order): BigDecimal = BigDecimal(0)
}

case class BulkDiscount(minAmount: BigDecimal, off: BigDecimal) extends DiscountRule {
  def percentOff(order: Order): BigDecimal =
    if (order.amount >= minAmount) off else BigDecimal(0)
}

case class LoyalSupplier(suppliers: Set[String], off: BigDecimal) extends DiscountRule {
  def percentOff(order: Order): BigDecimal =
    if (suppliers.contains(order.supplier)) off else BigDecimal(0)
}

case class EarlyPayment(off: BigDecimal) extends DiscountRule {
  def percentOff(order: Order): BigDecimal =
    if (order.paidEarly) off else BigDecimal(0)
}

object DiscountEngine {
  def payable(order: Order, rules: List[DiscountRule]): BigDecimal = {
    val best = (NoDiscount :: rules).map(_.percentOff(order)).max
    (order.amount * (100 - best) / 100).setScale(2, RoundingMode.HALF_UP)
  }
}
