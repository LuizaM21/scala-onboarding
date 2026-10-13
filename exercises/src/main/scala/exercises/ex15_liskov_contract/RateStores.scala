package exercises.ex15_liskov_contract

import scala.collection.mutable

/** Two implementations, one contract. Lesson 8, letter L.
  *
  * `RateStore` is the parent type. Its promises are written as tests in
  * `Ex15RateStoreContract`. Read that file first: it is the specification.
  *
  * The same contract runs against both classes below.
  * Liskov says: if one of them fails a contract test, it is not a real `RateStore`.
  *
  * 1. `MapRateStore` keeps rates in a mutable `Map`. This one is short.
  * 2. `HistoryRateStore` keeps every `put` in a list, so you can also read the history.
  *    This is where the trap is. A list keeps duplicates.
  *    `get` must still return the latest rate, and `currencies` must not repeat a code.
  *
  * Hint: `mutable.Map.update(key, value)` adds or replaces.
  * Hint: the newest item of a `ListBuffer` is the last one. `findLast` or `reverseIterator` help.
  * Hint: `.map(_._1).toSet` drops duplicates.
  */
trait RateStore {
  def put(currency: String, rate: BigDecimal): Unit
  def get(currency: String): Option[BigDecimal]
  def currencies: Set[String]
}

class MapRateStore extends RateStore {
  private val rates = mutable.Map.empty[String, BigDecimal]

  def put(currency: String, rate: BigDecimal): Unit = ???

  def get(currency: String): Option[BigDecimal] = ???

  def currencies: Set[String] = ???
}

class HistoryRateStore extends RateStore {
  private val entries = mutable.ListBuffer.empty[(String, BigDecimal)]

  /** Extra ability, only on this class. Already done. Oldest first. */
  def history(currency: String): List[BigDecimal] =
    entries.filter(_._1 == currency).map(_._2).toList

  def put(currency: String, rate: BigDecimal): Unit = ???

  def get(currency: String): Option[BigDecimal] = ???

  def currencies: Set[String] = ???
}
