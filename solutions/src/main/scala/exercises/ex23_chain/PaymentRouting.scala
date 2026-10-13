package exercises.ex23_chain

case class Payment(iban: String, currency: String, bic: Option[String])

object PaymentRouting {
  type Handler = Payment => Option[String]

  val SepaCountries: Set[String] = Set("RO", "DE", "FR", "IT", "ES", "NL", "AT", "BE")

  def internal: Handler = payment =>
    Option.when(payment.iban.startsWith("RO") && payment.iban.slice(4, 8) == "FINS")("INTERNAL")

  def sepa: Handler = payment =>
    Option.when(payment.currency == "EUR" && SepaCountries.contains(payment.iban.take(2)))("SEPA")

  def swift: Handler = payment =>
    Option.when(payment.bic.exists(_.nonEmpty))("SWIFT")

  def defaultChain: List[Handler] = List(internal, sepa, swift)

  def route(payment: Payment, chain: List[Handler] = defaultChain): String =
    chain.view.flatMap(handler => handler(payment)).headOption.getOrElse("MANUAL")
}
