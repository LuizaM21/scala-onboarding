package lessons.l10_behavioural

/** Run this lesson with: sbt "lessons/run 10"
  *
  * Behavioural patterns decide how objects share work and talk to each other.
  */
object Demo {
  def run(): Unit = {
    println("Lesson 10 — Behavioural patterns")
    println()

    println("Strategy — a discount is just a function:")
    val amount = BigDecimal("200.00")
    List(
      "none" -> Discounts.none,
      "10% off" -> Discounts.percentOff(10),
      "30 off" -> Discounts.fixedOff(30),
      "10% then 30 off" -> Discounts.both(Discounts.percentOff(10), Discounts.fixedOff(30))
    ).foreach { case (label, discount) =>
      println(s"  $label: ${Checkout(discount).payable(amount)}")
    }
    println()

    println("Observer — two listeners, one publisher that knows neither:")
    val bus = EventBus()
    val audit = AuditLog()
    val paid = PaidCounter()
    bus.subscribe(audit.listener)
    bus.subscribe(paid.listener)
    val desk = InvoiceDesk(bus)
    desk.create("INV-001", BigDecimal("120.50"))
    desk.markPaid("INV-001")
    audit.lines.foreach(line => println(s"  audit: $line"))
    println(s"  paid count: ${paid.count}")
    println()

    println("Chain of Responsibility — who approves?")
    List("250", "4800", "75000").map(BigDecimal(_)).foreach { value =>
      println(s"  $value -> ${Approval.whoApproves(value).getOrElse("nobody")}")
    }
    println(s"  rules for ('', -5): ${InvoiceRules.check("", BigDecimal(-5))}")
    println()

    println("State — the invoice lifecycle:")
    import Action.*
    println(s"  Draft + Send, Pay        -> ${Lifecycle.run(Lifecycle.Draft, List(Send, Pay))}")
    println(s"  Draft + Send, Pay, Cancel -> ${Lifecycle.run(Lifecycle.Draft, List(Send, Pay, Cancel))}")
  }
}
