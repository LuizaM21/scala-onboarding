package exercises.ex03_overdue

import java.time.LocalDate

class Ex03OverdueShould extends munit.FunSuite {
  // setup
  val today: LocalDate = LocalDate.of(2026, 10, 11)
  test("mark a due date before today as overdue") {
    assert(Due.isOverdue(today.minusDays(1),today))}

  test("keep a due date of today as not overdue") {
    assert(!Due.isOverdue(today,today))}

  test("keep a future due date as not overdue") {
    assert(!Due.isOverdue(today.plusDays(10), today))}

  test("mark the last day of the previous month as overdue") {
    assert(Due.isOverdue(today.minusDays(10), today))}
}
