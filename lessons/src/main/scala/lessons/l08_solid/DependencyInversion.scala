package lessons.l08_solid

import java.time.LocalDate

/** D — Dependency Inversion.
  *
  * Business code should depend on small traits, not on concrete tools.
  * The tool (clock, database, email server) is passed in from outside.
  *
  * Before: the service reads the real clock and prints to the console.
  * What is the expected result of a test? It depends on the day you run it.
  * How do you check that a reminder was sent? You read the console. Not possible.
  */
class RemindersBefore(dueDates: Map[String, LocalDate]) {
  def sendReminders(): Unit = {
    val today = LocalDate.now() // hidden dependency on the real clock
    dueDates.foreach { case (number, due) =>
      if (due.isBefore(today)) println(s"Reminder: $number is overdue") // hidden dependency on the console
    }
  }
}

/** After: the service names what it needs as traits. */
trait Clock {
  def today(): LocalDate
}

trait Outbox {
  def send(message: String): Unit
}

/** The constructor lists every dependency. Nothing is hidden.
  * This is called constructor injection. Spring (Java) uses the same idea.
  */
class Reminders(dueDates: Map[String, LocalDate], clock: Clock, outbox: Outbox) {

  /** Returns the overdue numbers, sorted, so the caller and the test can see them. */
  def sendReminders(): List[String] = {
    val today = clock.today()
    val overdue = dueDates.collect { case (number, due) if due.isBefore(today) => number }.toList.sorted
    overdue.foreach(number => outbox.send(s"Reminder: $number is overdue"))
    overdue
  }
}

/** Real implementations. The app uses these. */
object SystemClock extends Clock {
  def today(): LocalDate = LocalDate.now()
}

object ConsoleOutbox extends Outbox {
  def send(message: String): Unit = println(message)
}

/* Test doubles (fakes) live in the test folder. See `RemindersShould`.
 * A FixedClock returns the same day every time.
 * A RecordingOutbox stores each message in a list so the test can read them.
 */
