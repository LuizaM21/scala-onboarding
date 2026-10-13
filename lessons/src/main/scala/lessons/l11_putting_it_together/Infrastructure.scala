package lessons.l11_putting_it_together

import java.time.LocalDate
import scala.collection.mutable.ListBuffer

/** Layer 3 — infrastructure. Real tools behind the ports.
  *
  * In a real service this is where SQL, HTTP, and email live.
  * Here they are kept tiny so the lesson stays about the shape.
  */
class InMemoryInvoiceRepository(rows: List[OpenInvoice]) extends InvoiceRepository {
  def openInvoices(): List[OpenInvoice] = rows
}

object ConsoleNotifier extends Notifier {
  def send(to: String, message: String): Unit = println(s"  to $to: $message")
}

object SystemClock extends Clock {
  def today(): LocalDate = LocalDate.now()
}

class FixedClock(day: LocalDate) extends Clock {
  def today(): LocalDate = day
}

/** Decorator (lesson 9). Wraps any notifier and keeps a copy of every message.
  * Useful in tests, and also in the app as an audit trail.
  */
class RecordingNotifier(inner: Notifier) extends Notifier {
  val sent: ListBuffer[(String, String)] = ListBuffer.empty

  def send(to: String, message: String): Unit = {
    sent += (to -> message)
    inner.send(to, message)
  }
}

object SilentNotifier extends Notifier {
  def send(to: String, message: String): Unit = ()
}
