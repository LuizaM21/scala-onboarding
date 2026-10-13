package exercises.ex23_chain

/** Pick a payment channel with a chain of handlers. Lesson 10, Chain of Responsibility.
  *
  * Each handler looks at a payment and either takes it (`Some(channel)`) or passes (`None`).
  * The router asks each handler in order. The first `Some` wins.
  *
  * Write three handlers and the router:
  * - `internal`: the IBAN starts with `RO` and the bank code (characters 5 to 8) is `FINS`.
  *               Channel `"INTERNAL"`. Example IBAN: `RO49FINS0000123456789012`.
  * - `sepa`:     currency is `EUR` and the IBAN country (first 2 letters) is in `SepaCountries`.
  *               Channel `"SEPA"`.
  * - `swift`:    the payment has a BIC (`bic` is not empty). Channel `"SWIFT"`.
  * - `route`:    ask each handler in `chain`, in order. Nobody takes it: `"MANUAL"`.
  *
  * Order matters. An internal EUR payment would also match `sepa`.
  * Because `internal` comes first in `defaultChain`, it wins.
  *
  * Hint: `iban.slice(4, 8)` gives characters 5 to 8.
  * Hint: `Option.when(condition)(value)` is `Some(value)` when the condition is true.
  * Hint: `chain.view.flatMap(handler => handler(payment)).headOption` stops at the first `Some`.
  */
case class Payment(iban: String, currency: String, bic: Option[String])

object PaymentRouting {
  type Handler = Payment => Option[String]

  val SepaCountries: Set[String] = Set("RO", "DE", "FR", "IT", "ES", "NL", "AT", "BE")

  def internal: Handler = ???

  def sepa: Handler = ???

  def swift: Handler = ???

  def defaultChain: List[Handler] = List(internal, sepa, swift)

  def route(payment: Payment, chain: List[Handler] = defaultChain): String = ???
}
