package lessons.l09_creational_structural

/** Singleton — exactly one instance in the whole program.
  *
  * In Java you write a private constructor and a static `getInstance()`.
  * In Scala you write `object`. That is already a singleton.
  * You have used it since lesson 1: `object Basics`, `object Money`.
  *
  * Good uses: pure helpers with no state, like `Money.eur` or `Iban.from`.
  *
  * Be careful with an `object` that holds state you can change.
  * Every test shares it. One test changes it, the next test sees the change.
  * The order of tests starts to matter. That is a flaky test waiting to happen.
  * Prefer a class, and pass one instance in (lesson 8, letter D).
  */
object VatRates {
  // Fine: fixed data, nobody changes it.
  val standard: BigDecimal = BigDecimal("0.19")
  val reduced: BigDecimal = BigDecimal("0.09")
}

/** Risky: shared, changing state. Shown so you recognise it. */
object GlobalCounterBefore {
  var invoicesProcessed: Int = 0
}

/** Better: each owner gets its own counter. A test makes a fresh one. */
class Counter {
  private var value = 0

  def increment(): Unit = value += 1

  def current: Int = value
}
