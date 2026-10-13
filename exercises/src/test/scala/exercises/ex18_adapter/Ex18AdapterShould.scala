package exercises.ex18_adapter

class Ex18AdapterShould extends munit.FunSuite {
  private def adapter(raw: String): RateProvider = LegacyRatesAdapter(LegacyRatesFeed(raw))

  test("find a rate listed in the feed") {
    // execute

    val rate = adapter("USD=0.92;RON=0.20;GBP=1.17").rateFor("RON")

    // verify

    assertEquals(rate, Some(BigDecimal("0.20")))
  }

  test("ignore case and spaces") {
    // execute

    val rate = adapter(" usd = 0.92 ; RON=0.20").rateFor("Usd")

    // verify

    assertEquals(rate, Some(BigDecimal("0.92")))
  }

  test("return None for a currency the feed does not list") {
    // execute

    val rate = adapter("USD=0.92").rateFor("CHF")

    // verify

    assertEquals(rate, None)
  }

  test("skip broken parts and keep the good ones") {
    // execute

    val provider = adapter("USD=abc;RON;GBP=1.17;")

    // verify

    assertEquals(provider.rateFor("USD"), None)
    assertEquals(provider.rateFor("GBP"), Some(BigDecimal("1.17")))
  }

  test("always know that 1 EUR is 1 EUR") {
    // execute

    val rate = adapter("").rateFor("EUR")

    // verify

    assertEquals(rate, Some(BigDecimal(1)))
  }
}
