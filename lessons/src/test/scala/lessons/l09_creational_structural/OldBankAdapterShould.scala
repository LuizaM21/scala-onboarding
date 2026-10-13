package lessons.l09_creational_structural

class OldBankAdapterShould extends munit.FunSuite {
  private val gateway: PaymentGateway = OldBankAdapter(OldBankClient())

  test("turn code 0 into Right") {
    // execute

    val result = gateway.pay("RO49AAAA", BigDecimal("100.00"), "EUR")

    // verify

    assertEquals(result, Right(()))
  }

  test("turn code 51 into an insufficient funds message") {
    // execute

    val result = gateway.pay("RO49AAAA", BigDecimal("10000.01"), "EUR")

    // verify

    assertEquals(result, Left("insufficient funds"))
  }

  test("turn code 14 into an invalid account message") {
    // execute

    val result = gateway.pay("", BigDecimal("1.00"), "EUR")

    // verify

    assertEquals(result, Left("invalid account"))
  }

  test("send the amount to the old client in cents") {
    // setup

    var sentCents = 0L
    val spy = new OldBankClient {
      override def transfer(accountId: String, amountInCents: Long, currencyCode: String): Int = {
        sentCents = amountInCents
        0
      }
    }

    // execute

    OldBankAdapter(spy).pay("RO49AAAA", BigDecimal("12.34"), "EUR")

    // verify

    assertEquals(sentCents, 1234L)
  }
}
