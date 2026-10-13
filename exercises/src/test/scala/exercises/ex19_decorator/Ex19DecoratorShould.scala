package exercises.ex19_decorator

/** A fake sender. Fails `failures` times with "timeout #n", then succeeds. Counts every call. */
class FlakySender(failures: Int) extends PaymentSender {
  var calls: Int = 0

  def send(invoice: String, amount: BigDecimal): Either[String, String] = {
    calls += 1
    if (calls <= failures) Left(s"timeout #$calls") else Right(s"OK-$invoice")
  }
}

class Ex19DecoratorShould extends munit.FunSuite {
  test("refuse an amount over the limit without calling the inner sender") {
    // setup

    val inner = FlakySender(failures = 0)
    val sender = LimitCheck(inner, max = BigDecimal("1000"))

    // execute

    val result = sender.send("INV-001", BigDecimal("1000.01"))

    // verify

    assertEquals(result, Left("amount over limit"))
    assertEquals(inner.calls, 0)
  }

  test("pass an amount at the limit to the inner sender") {
    // setup

    val sender = LimitCheck(FlakySender(failures = 0), max = BigDecimal("1000"))

    // execute

    val result = sender.send("INV-001", BigDecimal("1000"))

    // verify

    assertEquals(result, Right("OK-INV-001"))
  }

  test("call once when the first try works") {
    // setup

    val inner = FlakySender(failures = 0)

    // execute

    Retry(inner, attempts = 3).send("INV-001", BigDecimal("10"))

    // verify

    assertEquals(inner.calls, 1)
  }

  test("retry until it works") {
    // setup

    val inner = FlakySender(failures = 2)

    // execute

    val result = Retry(inner, attempts = 3).send("INV-001", BigDecimal("10"))

    // verify

    assertEquals(result, Right("OK-INV-001"))
    assertEquals(inner.calls, 3)
  }

  test("give up after the last attempt and return the last error") {
    // setup

    val inner = FlakySender(failures = 5)

    // execute

    val result = Retry(inner, attempts = 3).send("INV-001", BigDecimal("10"))

    // verify

    assertEquals(result, Left("timeout #3"))
    assertEquals(inner.calls, 3)
  }

  test("stack both wrappers: the limit check stops a big amount before any retry") {
    // setup

    val inner = FlakySender(failures = 1)
    val sender = LimitCheck(Retry(inner, attempts = 3), max = BigDecimal("500"))

    // execute

    val big = sender.send("INV-001", BigDecimal("900"))
    val small = sender.send("INV-002", BigDecimal("100"))

    // verify

    assertEquals(big, Left("amount over limit"))
    assertEquals(small, Right("OK-INV-002"))
    assertEquals(inner.calls, 2)
  }
}
