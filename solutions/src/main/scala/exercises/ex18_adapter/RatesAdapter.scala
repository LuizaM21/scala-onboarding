package exercises.ex18_adapter

class LegacyRatesFeed(raw: String) {
  def fetchAll(): String = raw
}

trait RateProvider {
  def rateFor(currency: String): Option[BigDecimal]
}

class LegacyRatesAdapter(feed: LegacyRatesFeed) extends RateProvider {
  def rateFor(currency: String): Option[BigDecimal] = {
    val code = currency.trim.toUpperCase
    if (code == "EUR") Some(BigDecimal(1))
    else parse(feed.fetchAll()).get(code)
  }

  private def parse(raw: String): Map[String, BigDecimal] =
    raw.split(";").toList.flatMap { part =>
      part.split("=", -1).map(_.trim) match {
        case Array(code, rate) =>
          try Some(code.toUpperCase -> BigDecimal(rate))
          catch {
            case _: NumberFormatException => None
          }
        case _ => None
      }
    }.toMap
}
