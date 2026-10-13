package exercises.ex23_chain

import PaymentRouting.*

class Ex23ChainShould extends munit.FunSuite {
  private val internalIban = "RO49FINS0000123456789012"
  private val germanIban = "DE89370400440532013000"
  private val usIban = "US00BANK0000000000000000"

  test("take a payment to our own bank as internal") {
    // execute

    val channel = internal(Payment(internalIban, "RON", None))

    // verify

    assertEquals(channel, Some("INTERNAL"))
  }

  test("pass on a Romanian IBAN from another bank in the internal handler") {
    // execute

    val channel = internal(Payment("RO49AAAA1B31007593840000", "RON", None))

    // verify

    assertEquals(channel, None)
  }

  test("take a EUR payment to a SEPA country as SEPA") {
    // execute

    val channel = sepa(Payment(germanIban, "EUR", None))

    // verify

    assertEquals(channel, Some("SEPA"))
  }

  test("pass on a USD payment in the SEPA handler") {
    // execute

    val channel = sepa(Payment(germanIban, "USD", None))

    // verify

    assertEquals(channel, None)
  }

  test("take a payment with a BIC as SWIFT") {
    // execute

    val channel = swift(Payment(usIban, "USD", Some("BOFAUS3N")))

    // verify

    assertEquals(channel, Some("SWIFT"))
  }

  test("pass on an empty BIC in the SWIFT handler") {
    // execute

    val channel = swift(Payment(usIban, "USD", Some("")))

    // verify

    assertEquals(channel, None)
  }

  test("let internal win over SEPA because it comes first") {
    // execute

    val channel = route(Payment(internalIban, "EUR", None))

    // verify

    assertEquals(channel, "INTERNAL")
  }

  test("fall back to manual when no handler takes the payment") {
    // execute

    val channel = route(Payment(usIban, "USD", None))

    // verify

    assertEquals(channel, "MANUAL")
  }

  test("follow the order of a custom chain") {
    // setup

    val payment = Payment(germanIban, "EUR", Some("COBADEFF"))

    // execute

    val channel = route(payment, List(swift, sepa))

    // verify

    assertEquals(channel, "SWIFT")
  }
}
