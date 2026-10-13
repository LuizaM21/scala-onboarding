package exercises.ex09_convert

/** Convert an amount to EUR.
  *
  * `rates` maps a currency code to "how many EUR is 1 unit".
  * Example: `USD -> 0.92` means 1 USD = 0.92 EUR.
  *
  * Return `Some(eur)` when the code is in the map.
  * Return `None` when the code is missing. Do not throw.
  * Use two decimal places, half up.
  *
  * Hint: `rates.get(currency)` is already an `Option`.
  * Hint: `option.map(rate => amount * rate)` changes the value inside `Some`.
  */
object Convert {
  def toEur(amount: BigDecimal, currency: String, rates: Map[String, BigDecimal]): Option[BigDecimal] =
    // rates.get(currency) looks up the currency code in the map.
    // It already returns an Option:
    //   Some(rate) when the code exists (e.g. "USD" -> Some(0.92))
    //   None       when the code is missing (e.g. "GBP" -> None)
    rates.get(currency)


      // map runs the function only when there is a Some.
      // If the lookup gave None, map skips it and the result stays None,
      // so the "missing currency" case needs no extra code
      .map{ rate =>

        // rate = how many EUR is 1 unit, so we MULTIPLY (100 USD * 0.92 = 92 EUR).
        // setScale(2, HALF_UP) keeps two decimals and rounds half up (0.005 -> 0.01).
        // The parentheses make setScale apply to the whole product.
        (amount * rate).setScale(2, BigDecimal.RoundingMode.HALF_UP)
    }
}
