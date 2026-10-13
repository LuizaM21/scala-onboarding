package exercises.ex20_strategy

import PaymentTerms.*

import java.time.LocalDate

class Ex20StrategyShould extends munit.FunSuite {
  private def date(text: String): LocalDate = LocalDate.parse(text)

  test("add the days for a net term") {
    // execute

    val due = net(30)(date("2026-01-15"))

    // verify

    assertEquals(due, date("2026-02-14"))
  }

  test("move to the last day of the month, including a short February") {
    // execute

    val due = endOfMonth(date("2026-02-10"))

    // verify

    assertEquals(due, date("2026-02-28"))
  }

  test("add days after the end of the month") {
    // execute

    val due = endOfMonthPlus(15)(date("2026-01-10"))

    // verify

    assertEquals(due, date("2026-02-15"))
  }

  test("move a Saturday due date to Monday") {
    // setup

    val term = nextWorkingDay(net(0))

    // execute

    val due = term(date("2026-10-10"))

    // verify

    assertEquals(due, date("2026-10-12"))
  }

  test("move a Sunday due date to Monday") {
    // execute

    val due = nextWorkingDay(net(1))(date("2026-10-10"))

    // verify

    assertEquals(due, date("2026-10-12"))
  }

  test("keep a weekday due date as it is") {
    // execute

    val due = nextWorkingDay(net(30))(date("2026-01-13"))

    // verify

    assertEquals(due, date("2026-02-12"))
  }

  test("let each contract use its own term") {
    // setup

    val acme = Contract("Acme SRL", net(14))
    val beta = Contract("Beta SA", endOfMonth)
    val issued = date("2026-10-09")

    // execute

    val dueDates = List(acme, beta).map(_.dueDate(issued))

    // verify

    assertEquals(dueDates, List(date("2026-10-23"), date("2026-10-31")))
  }
}
