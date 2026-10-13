package exercises.ex15_liskov_contract

import scala.collection.mutable

trait RateStore {
  def put(currency: String, rate: BigDecimal): Unit
  def get(currency: String): Option[BigDecimal]
  def currencies: Set[String]
}

class MapRateStore extends RateStore {
  private val rates = mutable.Map.empty[String, BigDecimal]

  def put(currency: String, rate: BigDecimal): Unit = rates.update(currency, rate)

  def get(currency: String): Option[BigDecimal] = rates.get(currency)

  def currencies: Set[String] = rates.keySet.toSet
}

class HistoryRateStore extends RateStore {
  private val entries = mutable.ListBuffer.empty[(String, BigDecimal)]

  def history(currency: String): List[BigDecimal] =
    entries.filter(_._1 == currency).map(_._2).toList

  def put(currency: String, rate: BigDecimal): Unit = entries += (currency -> rate)

  def get(currency: String): Option[BigDecimal] =
    entries.findLast(_._1 == currency).map(_._2)

  def currencies: Set[String] = entries.map(_._1).toSet
}
