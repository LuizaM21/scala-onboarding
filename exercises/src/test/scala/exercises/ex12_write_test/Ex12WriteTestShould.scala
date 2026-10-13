package exercises.ex12_write_test

import java.time.LocalDate

/** Three tests are already written. They show the shape.
  *
  * Your task: write the ignored test at the bottom.
  * 1. Remove `.ignore` from the test name.
  * 2. Fill `// setup`, `// execute`, and `// verify`.
  * 3. Due date and today are both 2026-10-09. The result is `0L`.
  * 4. Run: sbt "ex *Ex12*"
  *
  * The function is already implemented. You are writing the check, not the code.
  */
class Ex12WriteTestShould extends munit.FunSuite {
  test("return a negative number when the due date is in the past") {
    // setup

    val due = LocalDate.of(2026,10,8)
    val today = LocalDate.of(2026,10,13)

    // execute

    val days = DueDate.daysUntilDue(due, today)

    // verify

    assertEquals(days, -5L)
  }

  test("return a positive number when the due date is in the future") {
    // setup

    val due = LocalDate.parse("2026-10-12")
    val today = LocalDate.parse("2026-10-09")

    // execute

    val days = DueDate.daysUntilDue(due, today)

    // verify

    assertEquals(days, 3L)
  }

  test("count a full week ahead") {
    // setup

    val due = LocalDate.parse("2026-10-16")
    val today = LocalDate.parse("2026-10-09")

    // execute

    val days = DueDate.daysUntilDue(due, today)

    // verify

    assertEquals(days, 7L)
  }

  // TODO: write this test. Delete `.ignore` when the body is ready.
  test("return zero when the invoice is due today") {
    // setup
    val due = LocalDate.of(2026,10,13)
    val today = LocalDate.of(2026,10,13)

    // execute
    val days = DueDate.daysUntilDue(due, today)

    // verify

    assertEquals(days ,0L)
  }
}
