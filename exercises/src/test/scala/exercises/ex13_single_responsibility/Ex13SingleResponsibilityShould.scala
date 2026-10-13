package exercises.ex13_single_responsibility

import java.time.LocalDate

class Ex13SingleResponsibilityShould extends munit.FunSuite {
  private val today = LocalDate.parse("2026-10-09")

  test("parse a line into a row") {
    // execute

    val row = DueCsv.parse(" INV-001 , Acme SRL , 2026-10-01 ")

    // verify

    assertEquals(row, Some(DueRow("INV-001", "Acme SRL", LocalDate.parse("2026-10-01"))))
  }

  test("return None for a line with a bad date") {
    // execute

    val row = DueCsv.parse("INV-001,Acme SRL,tomorrow")

    // verify

    assertEquals(row, None)
  }

  test("return None for a line with the wrong number of columns") {
    // execute

    val row = DueCsv.parse("INV-001,2026-10-01")

    // verify

    assertEquals(row, None)
  }

  test("keep only rows due before today, oldest first") {
    // setup

    val rows = List(
      DueRow("INV-002", "Beta SA", LocalDate.parse("2026-10-05")),
      DueRow("INV-003", "Gamma", today),
      DueRow("INV-001", "Acme SRL", LocalDate.parse("2026-09-30"))
    )

    // execute

    val overdue = OverdueRule.overdue(rows, today)

    // verify

    assertEquals(overdue.map(_.number), List("INV-001", "INV-002"))
  }

  test("write the reminder text with the days late") {
    // setup

    val row = DueRow("INV-001", "Acme SRL", LocalDate.parse("2026-10-01"))

    // execute

    val text = ReminderText.line(row, today)

    // verify

    assertEquals(text, "Acme SRL: INV-001 is 8 days late")
  }

  test("give the same answer as the old version") {
    // setup

    val lines = List(
      "INV-001,Acme SRL,2026-10-01",
      "broken line",
      "INV-002,Beta SA,2026-09-20",
      "INV-003,Gamma,2026-12-01"
    )

    // execute

    val after = RemindersAfter.run(lines, today)

    // verify

    assertEquals(after, RemindersBefore.run(lines, today))
  }
}
