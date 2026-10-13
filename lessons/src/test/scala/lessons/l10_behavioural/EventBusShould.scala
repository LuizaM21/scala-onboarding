package lessons.l10_behavioural

class EventBusShould extends munit.FunSuite {
  test("deliver each event to every listener") {
    // setup

    val bus = EventBus()
    val first = AuditLog()
    val second = AuditLog()
    bus.subscribe(first.listener)
    bus.subscribe(second.listener)

    // execute

    InvoiceDesk(bus).create("INV-001", BigDecimal("120.50"))

    // verify

    assertEquals(first.lines.toList, List("Created(INV-001,120.50)"))
    assertEquals(second.lines.toList, first.lines.toList)
  }

  test("let a listener ignore the events it does not care about") {
    // setup

    val bus = EventBus()
    val counter = PaidCounter()
    bus.subscribe(counter.listener)
    val desk = InvoiceDesk(bus)

    // execute

    desk.create("INV-001", BigDecimal("10.00"))
    desk.markPaid("INV-001")
    desk.markPaid("INV-002")

    // verify

    assertEquals(counter.count, 2)
  }

  test("publish without error when nobody listens") {
    // setup

    val bus = EventBus()

    // execute & verify

    InvoiceDesk(bus).markPaid("INV-001")
  }
}
