package lessons.l10_behavioural

import scala.math.BigDecimal.RoundingMode

/** Strategy — choose an algorithm at run time, behind one shape.
  *
  * Lesson 8 already did this with `LateFeePolicy`: a trait and one class per rule.
  * In Scala a strategy is often just a function.
  * A function `BigDecimal => BigDecimal` is already "a thing that turns an amount into an amount".
  *
  * Use a trait when the strategy needs a name, several methods, or its own data.
  * Use a function when it is one small calculation.
  */
object Discounts {

  /** A type alias gives the function shape a readable name. */
  type Discount = BigDecimal => BigDecimal

  val none: Discount = amount => amount

  def percentOff(percent: BigDecimal): Discount =
    amount => (amount * (100 - percent) / 100).setScale(2, RoundingMode.HALF_UP)

  def fixedOff(value: BigDecimal): Discount =
    amount => (amount - value).max(BigDecimal("0.00")).setScale(2, RoundingMode.HALF_UP)

  /** Functions combine. `andThen` runs the first, then the second on its result. */
  def both(first: Discount, second: Discount): Discount = first.andThen(second)
}

/** The code that uses a strategy does not know which one it got. */
class Checkout(discount: Discounts.Discount) {
  def payable(amount: BigDecimal): BigDecimal = discount(amount)
}

/* Template Method, a close relative.
 *
 * Java often writes an abstract class with fixed steps and one abstract step:
 *
 *   abstract class Report { def run() = header() + body() + footer(); def body(): String }
 *
 * Each child overrides `body`. That works, but it ties the steps to inheritance.
 * In Scala, pass the changing step in as a function or a strategy instead.
 * "Prefer composition over inheritance" is the short version.
 */
