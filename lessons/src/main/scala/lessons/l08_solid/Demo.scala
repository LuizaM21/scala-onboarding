package lessons.l08_solid

import java.time.LocalDate

/** Run this lesson with: sbt "lessons/run 8"
  *
  * Each file in this folder shows one letter: a "Before" version and an "After" version.
  * Read the Before first. Ask: how would I test this? Then read the After.
  */
object Demo {
  def run(): Unit = {
    println("Lesson 8 — SOLID")
    println()
    println("S  Single Responsibility  one reason to change")
    println("O  Open/Closed            add new code, do not edit working code")
    println("L  Liskov Substitution    a child passes the parent's tests")
    println("I  Interface Segregation  small traits, no fake methods")
    println("D  Dependency Inversion   pass the tools in, do not grab them")
    println()

    println("S — the same report, split into three jobs:")
    val csv = "Acme SRL,100.00\nBeta SA,50.00\nAcme SRL,20.50"
    println(ReportAfter.run(csv))
    println()

    println("O — three fee policies, one calculator that never changed:")
    List(FlatFee(BigDecimal("25")), PercentFee(BigDecimal("2")), DailyFee(BigDecimal("1.50")))
      .foreach { policy =>
        val total = LateFeeCalculator(policy).totalDue(BigDecimal("1000.00"), daysLate = 10)
        println(s"  $policy -> $total")
      }
    println()

    println("L — the same lookup works on a read-only archive and on a store:")
    val archive = InvoiceArchive(Map("INV-001" -> BigDecimal("120.50")))
    val store = InMemoryInvoiceStore()
    store.save("INV-002", BigDecimal("75.00"))
    println(s"  archive INV-001 = ${Lookup.amountOrZero(archive, "INV-001")}")
    println(s"  store   INV-002 = ${Lookup.amountOrZero(store, "INV-002")}")
    println()

    println("I — the alert needs only an SmsSender:")
    OverdueSmsAlert(SmsGateway()).alert("+40 700 000 000", "INV-001")
    println()

    println("D — the clock is passed in, so 'today' is whatever we choose:")
    val fixedDay = new Clock { def today(): LocalDate = LocalDate.parse("2026-10-09") }
    val dueDates = Map(
      "INV-001" -> LocalDate.parse("2026-10-01"),
      "INV-002" -> LocalDate.parse("2026-10-20")
    )
    Reminders(dueDates, fixedDay, ConsoleOutbox).sendReminders()
  }
}
