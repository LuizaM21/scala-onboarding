package exercises.ex21_observer

import scala.collection.mutable.ListBuffer

enum PaymentEvent {
  case Received(invoice: String, amount: BigDecimal, currency: String)
  case Failed(invoice: String, reason: String)
}

type Listener = PaymentEvent => Unit

class PaymentBus {
  private val listeners = ListBuffer.empty[Listener]

  def subscribe(listener: Listener): Unit = listeners += listener

  def unsubscribe(listener: Listener): Unit = listeners -= listener

  def publish(event: PaymentEvent): Unit = listeners.foreach(listener => listener(event))
}

class ReceivedTotal {
  private val totals = scala.collection.mutable.Map.empty[String, BigDecimal]

  def onEvent(event: PaymentEvent): Unit =
    event match {
      case PaymentEvent.Received(_, amount, currency) =>
        totals.update(currency, total(currency) + amount)
      case PaymentEvent.Failed(_, _) => ()
    }

  def total(currency: String): BigDecimal = totals.getOrElse(currency, BigDecimal(0))
}
