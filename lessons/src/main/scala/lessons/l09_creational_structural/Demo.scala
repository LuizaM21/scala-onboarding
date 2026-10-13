package lessons.l09_creational_structural

import AnInvoice.*

/** Run this lesson with: sbt "lessons/run 9"
  *
  * Creational patterns decide how objects are built: Factory, Builder, Singleton.
  * Structural patterns decide how objects fit together: Adapter, Decorator.
  */
object Demo {
  def run(): Unit = {
    println("Lesson 9 — Creational and structural patterns")
    println()

    println("Factory — smart constructor:")
    println(s"  ${Iban.from("RO49 AAAA 1B31 0075 9384 0000")}")
    println(s"  ${Iban.from("hello")}")
    println("Factory — factory method:")
    val rows = List("Acme SRL" -> BigDecimal("120.50"), "Beta SA" -> BigDecimal("100.00"))
    Exporter.forFormat("json").foreach(exporter => println(s"  ${exporter.render(rows)}"))
    println(s"  ${Exporter.forFormat("xml")}")
    println()

    println("Builder — named arguments and defaults instead of a builder class:")
    println(s"  ${PaymentRequest(invoiceNumber = "INV-001", amount = BigDecimal("10.00"), urgent = true)}")
    println("Builder — test data builder:")
    val invoice = AnInvoice.default.from("Beta SA").worth("250.00").paid
    println(s"  ${invoice.number} ${invoice.supplier} ${invoice.total.show} ${invoice.status}")
    println()

    println("Singleton — `object` is already one:")
    println(s"  standard VAT ${VatRates.standard}")
    println()

    println("Adapter — the old bank speaks cents and codes, we speak Either:")
    val gateway: PaymentGateway = OldBankAdapter(OldBankClient())
    println(s"  pay 100.00   -> ${gateway.pay("RO49AAAA", BigDecimal("100.00"), "EUR")}")
    println(s"  pay 20000.00 -> ${gateway.pay("RO49AAAA", BigDecimal("20000.00"), "EUR")}")
    println()

    println("Decorator — cache on top of logging on top of the real source:")
    val real = FixedRateSource(Map("USD" -> BigDecimal("0.92")))
    val source = CachingRateSource(LoggingRateSource(real, line => println(s"  log: $line")))
    source.rate("USD")
    source.rate("USD")
    source.rate("USD")
    println(s"  asked 3 times, the real source was called ${real.calls} time(s)")
  }
}
