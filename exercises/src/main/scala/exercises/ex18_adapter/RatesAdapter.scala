package exercises.ex18_adapter

/** Make an old class fit a new trait. Lesson 9, Adapter.
  *
  * `LegacyRatesFeed` comes from another team. You cannot change it.
  * It returns every rate in one string, like this:
  *
  *   "USD=0.92;RON=0.20;GBP=1.17"
  *
  * Our code wants `RateProvider`: ask for one currency, get `Option[BigDecimal]`.
  *
  * Write `LegacyRatesAdapter.rateFor`:
  * - Split the feed on `;`, then each part on `=`.
  * - Trim spaces. Currency codes are compared in upper case: `usd` finds `USD`.
  * - Skip broken parts (no `=`, or a rate that is not a number). Do not crash.
  * - `EUR` is always `1`, even when the feed does not list it.
  * - Unknown currency: `None`.
  *
  * Hint: `text.split("=", -1)` gives an `Array`. Check its length.
  * Hint: `BigDecimal(text)` throws `NumberFormatException` on bad input. Lesson 4 shows how to catch it.
  */
class LegacyRatesFeed(raw: String) {
  def fetchAll(): String = raw
}

trait RateProvider {
  def rateFor(currency: String): Option[BigDecimal]
}

class LegacyRatesAdapter(feed: LegacyRatesFeed) extends RateProvider {
  def rateFor(currency: String): Option[BigDecimal] = ???
}
