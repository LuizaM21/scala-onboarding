package lessons.l09_creational_structural

/** Adapter — make a class you cannot change fit the trait you want.
  *
  * Think of a travel plug. The socket stays the same. The plug stays the same.
  * The adapter sits between them.
  *
  * Below, `OldBankClient` is a library from another team. We cannot edit it.
  * It speaks cents as `Long`, currency as a string, and answers with number codes.
  * Our code wants `PaymentGateway`: `BigDecimal` amounts and `Either` results.
  */
class OldBankClient {

  /** 0 = ok, 51 = not enough money, 14 = bad account. Any other code = unknown. */
  def transfer(accountId: String, amountInCents: Long, currencyCode: String): Int =
    if (accountId.isEmpty) 14
    else if (amountInCents > 1_000_000) 51
    else 0
}

/** The trait our code wants. */
trait PaymentGateway {
  def pay(account: String, amount: BigDecimal, currency: String): Either[String, Unit]
}

/** The adapter. It implements our trait and calls the old client inside.
  * All the translation lives here: euros to cents, codes to messages.
  * The rest of the app never sees a cent or a code.
  */
class OldBankAdapter(client: OldBankClient) extends PaymentGateway {
  def pay(account: String, amount: BigDecimal, currency: String): Either[String, Unit] = {
    val cents = (amount * 100).toLongExact
    client.transfer(account, cents, currency) match {
      case 0    => Right(())
      case 51   => Left("insufficient funds")
      case 14   => Left("invalid account")
      case code => Left(s"bank error $code")
    }
  }
}
