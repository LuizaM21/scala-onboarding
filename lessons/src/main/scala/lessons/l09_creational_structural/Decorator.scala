package lessons.l09_creational_structural

import scala.collection.mutable

/** Decorator — wrap an object to add behaviour, keep the same trait.
  *
  * The wrapper implements the same trait as the thing it wraps.
  * It does its extra work, then calls the inner one.
  * Callers cannot tell the difference. You can stack wrappers like layers.
  *
  * Here: a source of exchange rates. The real one is slow (think: a web call).
  * We add logging and a cache without editing the real one.
  */
trait RateSource {
  def rate(currency: String): Option[BigDecimal]
}

/** The plain source. It counts calls so a test can see the cache working. */
class FixedRateSource(rates: Map[String, BigDecimal]) extends RateSource {
  var calls: Int = 0

  def rate(currency: String): Option[BigDecimal] = {
    calls += 1
    rates.get(currency)
  }
}

/** Adds a log line. Then asks the inner source. */
class LoggingRateSource(inner: RateSource, log: String => Unit) extends RateSource {
  def rate(currency: String): Option[BigDecimal] = {
    val result = inner.rate(currency)
    log(s"rate($currency) = ${result.getOrElse("missing")}")
    result
  }
}

/** Remembers answers. The second question for the same currency does not reach the inner source. */
class CachingRateSource(inner: RateSource) extends RateSource {
  private val cache = mutable.Map.empty[String, Option[BigDecimal]]

  def rate(currency: String): Option[BigDecimal] =
    cache.getOrElseUpdate(currency, inner.rate(currency))
}

/* Stacking:
 *
 *   val source = CachingRateSource(LoggingRateSource(FixedRateSource(rates), println))
 *
 * Read it from the inside out: real rates, then logging, then the cache on top.
 * Only cache misses are logged, because the cache is the outer layer.
 * Swap the two wrappers and every call is logged. The order is a design choice.
 */
