package lessons.l08_solid

import java.time.LocalDate
import scala.collection.mutable.ListBuffer

/** A fake clock: always the same day. The test decides what "today" is. */
class FixedClock(day: LocalDate) extends Clock {
  def today(): LocalDate = day
}

/** A fake outbox: remembers each message instead of sending it. */
class RecordingOutbox extends Outbox {
  val sent: ListBuffer[String] = ListBuffer.empty

  def send(message: String): Unit = sent += message
}

class RemindersShould extends munit.FunSuite {
  private val today = LocalDate.parse("2026-10-09")

  test("send one reminder per overdue invoice") {
    // setup

    val outbox = RecordingOutbox()
    val dueDates = Map(
      "INV-002" -> LocalDate.parse("2026-10-01"),
      "INV-001" -> LocalDate.parse("2026-09-15")
    )
    val reminders = Reminders(dueDates, FixedClock(today), outbox)

    // execute

    reminders.sendReminders()

    // verify

    assertEquals(
      outbox.sent.toList,
      List("Reminder: INV-001 is overdue", "Reminder: INV-002 is overdue")
    )
  }

  test("send nothing for an invoice due today") {
    // setup

    val outbox = RecordingOutbox()
    val reminders = Reminders(Map("INV-003" -> today), FixedClock(today), outbox)

    // execute

    val overdue = reminders.sendReminders()

    // verify

    assertEquals(overdue, Nil)
    assert(outbox.sent.isEmpty)
  }

  test("treat the same invoice as overdue when the clock moves on") {
    // setup

    val outbox = RecordingOutbox()
    val dueDates = Map("INV-003" -> today)
    val tomorrow = FixedClock(today.plusDays(1))

    // execute

    val overdue = Reminders(dueDates, tomorrow, outbox).sendReminders()

    // verify

    assertEquals(overdue, List("INV-003"))
  }
}
