package exercises.ex24_refactor

import java.time.LocalDate

/** Characterization tests. They are green before you start. Keep them green.
  *
  * They were written by running the old code and writing down what it printed.
  * They do not say the behaviour is right. They say it must not change by accident.
  * If you find a real bug, fix it in a separate step and update the test on purpose.
  *
  * Columns: number, supplier, currency, amount, dueDate, status
  */
class Ex24RefactorShould extends munit.FunSuite {
  private val today = LocalDate.parse("2026-10-09")

  test("plan an open EUR invoice that is not late") {
    // setup

    val lines = List("INV-001,Acme SRL,EUR,120.50,2026-10-20,OPEN")

    // execute

    val text = PaymentRun.plan(lines, today)

    // verify

    assertEquals(text, "INV-001 Acme SRL 120.50 EUR\nPayments: 1\nTotal: 120.50 EUR\nSkipped: 0")
  }

  test("convert USD and RON to EUR") {
    // setup

    val lines = List(
      "INV-002,Beta SA,USD,100.00,2026-10-20,OPEN",
      "INV-003,Gamma SRL,RON,500.00,2026-10-20,OPEN"
    )

    // execute

    val text = PaymentRun.plan(lines, today)

    // verify

    assertEquals(
      text,
      "INV-002 Beta SA 92.00 EUR\nINV-003 Gamma SRL 100.00 EUR\nPayments: 2\nTotal: 192.00 EUR\nSkipped: 0"
    )
  }

  test("add a flat fee of 10 when up to 30 days late") {
    // setup

    val lines = List("INV-004,Acme SRL,EUR,200.00,2026-09-09,OPEN")

    // execute

    val text = PaymentRun.plan(lines, today)

    // verify

    assertEquals(
      text,
      "INV-004 Acme SRL 210.00 EUR (late fee 10.00)\nPayments: 1\nTotal: 210.00 EUR\nSkipped: 0"
    )
  }

  test("add a 2 percent fee when more than 30 days late") {
    // setup

    val lines = List("INV-005,Acme SRL,EUR,1000.00,2026-09-08,OPEN")

    // execute

    val text = PaymentRun.plan(lines, today)

    // verify

    assertEquals(
      text,
      "INV-005 Acme SRL 1020.00 EUR (late fee 20.00)\nPayments: 1\nTotal: 1020.00 EUR\nSkipped: 0"
    )
  }

  test("charge no fee when due today") {
    // setup

    val lines = List("INV-006,Acme SRL,EUR,50.00,2026-10-09,OPEN")

    // execute

    val text = PaymentRun.plan(lines, today)

    // verify

    assertEquals(text, "INV-006 Acme SRL 50.00 EUR\nPayments: 1\nTotal: 50.00 EUR\nSkipped: 0")
  }

  test("ignore a paid invoice without counting it as skipped") {
    // setup

    val lines = List("INV-007,Acme SRL,EUR,50.00,2026-09-01,PAID")

    // execute

    val text = PaymentRun.plan(lines, today)

    // verify

    assertEquals(text, "Payments: 0\nTotal: 0.00 EUR\nSkipped: 0")
  }

  test("count bad rows, bad amounts, bad dates and unknown currencies as skipped") {
    // setup

    val lines = List(
      "not,enough,columns",
      "INV-008,Acme SRL,EUR,abc,2026-10-20,OPEN",
      "INV-009,Acme SRL,EUR,10.00,someday,OPEN",
      "INV-010,Acme SRL,CHF,10.00,2026-10-20,OPEN"
    )

    // execute

    val text = PaymentRun.plan(lines, today)

    // verify

    assertEquals(text, "Payments: 0\nTotal: 0.00 EUR\nSkipped: 4")
  }

  test("trim spaces around every column") {
    // setup

    val lines = List(" INV-011 , Acme SRL , USD , 10 , 2026-10-20 , OPEN ")

    // execute

    val text = PaymentRun.plan(lines, today)

    // verify

    assertEquals(text, "INV-011 Acme SRL 9.20 EUR\nPayments: 1\nTotal: 9.20 EUR\nSkipped: 0")
  }

  test("keep the input order and sum a mixed file") {
    // setup

    val lines = List(
      "INV-020,Beta SA,RON,1000.00,2026-08-01,OPEN",
      "INV-021,Acme SRL,EUR,99.99,2026-10-01,OPEN",
      "INV-022,Acme SRL,EUR,10.00,2026-10-01,PAID",
      "broken"
    )

    // execute

    val text = PaymentRun.plan(lines, today)

    // verify

    assertEquals(
      text,
      List(
        "INV-020 Beta SA 204.00 EUR (late fee 4.00)",
        "INV-021 Acme SRL 109.99 EUR (late fee 10.00)",
        "Payments: 2",
        "Total: 313.99 EUR",
        "Skipped: 1"
      ).mkString("\n")
    )
  }
}
