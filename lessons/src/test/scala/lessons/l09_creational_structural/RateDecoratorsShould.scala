package lessons.l09_creational_structural

import scala.collection.mutable.ListBuffer

class RateDecoratorsShould extends munit.FunSuite {
  private val rates = Map("USD" -> BigDecimal("0.92"))

  test("call the inner source only once for the same currency") {
    // setup

    val real = FixedRateSource(rates)
    val cached = CachingRateSource(real)

    // execute

    cached.rate("USD")
    cached.rate("USD")

    // verify

    assertEquals(real.calls, 1)
  }

  test("return the same answer as the source it wraps") {
    // setup

    val cached = CachingRateSource(FixedRateSource(rates))

    // execute

    val usd = cached.rate("USD")
    val chf = cached.rate("CHF")

    // verify

    assertEquals(usd, Some(BigDecimal("0.92")))
    assertEquals(chf, None)
  }

  test("write one log line per call") {
    // setup

    val lines = ListBuffer.empty[String]
    val logging = LoggingRateSource(FixedRateSource(rates), line => lines += line)

    // execute

    logging.rate("USD")
    logging.rate("CHF")

    // verify

    assertEquals(lines.toList, List("rate(USD) = 0.92", "rate(CHF) = missing"))
  }

  test("log only cache misses when the cache is the outer layer") {
    // setup

    val lines = ListBuffer.empty[String]
    val source = CachingRateSource(LoggingRateSource(FixedRateSource(rates), line => lines += line))

    // execute

    source.rate("USD")
    source.rate("USD")

    // verify

    assertEquals(lines.size, 1)
  }
}
