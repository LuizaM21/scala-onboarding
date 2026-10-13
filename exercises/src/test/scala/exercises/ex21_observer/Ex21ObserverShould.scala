package exercises.ex21_observer

import scala.collection.mutable.ListBuffer

class Ex21ObserverShould extends munit.FunSuite {
  private val paid = PaymentEvent.Received("INV-001", BigDecimal("120.50"), "EUR")
  private val failed = PaymentEvent.Failed("INV-002", "card declined")

  test("deliver an event to every listener in subscribe order") {
    // setup

    val bus = PaymentBus()
    val seen = ListBuffer.empty[String]
    bus.subscribe(_ => seen += "first")
    bus.subscribe(_ => seen += "second")

    // execute

    bus.publish(paid)

    // verify

    assertEquals(seen.toList, List("first", "second"))
  }

  test("stop delivering to a listener after it unsubscribes") {
    // setup

    val bus = PaymentBus()
    val seen = ListBuffer.empty[PaymentEvent]
    val listener: Listener = event => seen += event
    bus.subscribe(listener)
    bus.publish(paid)

    // execute

    bus.unsubscribe(listener)
    bus.publish(failed)

    // verify

    assertEquals(seen.toList, List(paid))
  }

  test("sum received amounts per currency") {
    // setup

    val bus = PaymentBus()
    val totals = ReceivedTotal()
    bus.subscribe(totals.onEvent)

    // execute

    bus.publish(paid)
    bus.publish(PaymentEvent.Received("INV-003", BigDecimal("9.50"), "EUR"))
    bus.publish(PaymentEvent.Received("INV-004", BigDecimal("75"), "USD"))

    // verify

    assertEquals(totals.total("EUR"), BigDecimal("130.00"))
    assertEquals(totals.total("USD"), BigDecimal("75"))
  }

  test("ignore failed payments in the total") {
    // setup

    val totals = ReceivedTotal()

    // execute

    totals.onEvent(failed)

    // verify

    assertEquals(totals.total("EUR"), BigDecimal(0))
  }
}
