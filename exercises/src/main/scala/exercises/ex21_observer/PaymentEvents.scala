package exercises.ex21_observer

import scala.collection.mutable.ListBuffer

/** Publish payment events to listeners. Lesson 10, Observer.
  *
  * Part 1 — the bus.
  * - `subscribe(listener)` adds a listener.
  * - `unsubscribe(listener)` removes it. Later events do not reach it.
  * - `publish(event)` calls every listener once, in the order they subscribed.
  *
  * Part 2 — a listener.
  * `ReceivedTotal` keeps a running total of `Received` amounts, per currency.
  * It ignores `Failed` events.
  *
  * Hint: `listeners -= listener` removes an item from a `ListBuffer`.
  * Hint: `totals.updateWith(currency)(old => Some(old.getOrElse(BigDecimal(0)) + amount))`
  *       is one way to add to a value in a mutable map. A plain `getOrElse` and `update` works too.
  */
enum PaymentEvent {
  case Received(invoice: String, amount: BigDecimal, currency: String)
  case Failed(invoice: String, reason: String)
}

type Listener = PaymentEvent => Unit

class PaymentBus {
  private val listeners = ListBuffer.empty[Listener]

  def subscribe(listener: Listener): Unit = ???

  def unsubscribe(listener: Listener): Unit = ???

  def publish(event: PaymentEvent): Unit = ???
}

class ReceivedTotal {
  private val totals = scala.collection.mutable.Map.empty[String, BigDecimal]

  /** Pass this to `subscribe`. */
  def onEvent(event: PaymentEvent): Unit = ???

  /** Already done. Zero when nothing was received in that currency. */
  def total(currency: String): BigDecimal = totals.getOrElse(currency, BigDecimal(0))
}
