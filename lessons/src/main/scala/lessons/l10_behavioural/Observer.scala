package lessons.l10_behavioural

import scala.collection.mutable.ListBuffer

/** Observer — tell many listeners that something happened, without knowing who they are.
  *
  * The invoice code publishes "INV-001 was paid".
  * The email module, the audit log, and the dashboard each listen.
  * Adding a fourth listener does not touch the invoice code. (Open/Closed again.)
  *
  * Also called publish/subscribe, or events.
  */
enum InvoiceEvent {
  case Created(number: String, amount: BigDecimal)
  case Paid(number: String)
  case Cancelled(number: String, reason: String)
}

/** A listener is a function from an event to nothing (`Unit`). */
type Listener = InvoiceEvent => Unit

class EventBus {
  private val listeners = ListBuffer.empty[Listener]

  def subscribe(listener: Listener): Unit = listeners += listener

  /** Every listener gets every event, in the order they subscribed. */
  def publish(event: InvoiceEvent): Unit = listeners.foreach(listener => listener(event))
}

/** Two listeners. Each cares about different events. `match` picks what it wants. */
class AuditLog {
  val lines: ListBuffer[String] = ListBuffer.empty

  val listener: Listener = event => lines += event.toString
}

class PaidCounter {
  var count: Int = 0

  val listener: Listener = {
    case InvoiceEvent.Paid(_) => count += 1
    case _                    => () // not interested
  }
}

/** The publisher. It knows the bus. It does not know the audit log or the counter. */
class InvoiceDesk(bus: EventBus) {
  def create(number: String, amount: BigDecimal): Unit =
    bus.publish(InvoiceEvent.Created(number, amount))

  def markPaid(number: String): Unit =
    bus.publish(InvoiceEvent.Paid(number))
}
