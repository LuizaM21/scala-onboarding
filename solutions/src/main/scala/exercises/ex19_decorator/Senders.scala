package exercises.ex19_decorator

import scala.annotation.tailrec

trait PaymentSender {
  def send(invoice: String, amount: BigDecimal): Either[String, String]
}

class LimitCheck(inner: PaymentSender, max: BigDecimal) extends PaymentSender {
  def send(invoice: String, amount: BigDecimal): Either[String, String] =
    if (amount > max) Left("amount over limit")
    else inner.send(invoice, amount)
}

class Retry(inner: PaymentSender, attempts: Int) extends PaymentSender {
  def send(invoice: String, amount: BigDecimal): Either[String, String] = {
    @tailrec
    def loop(left: Int): Either[String, String] =
      inner.send(invoice, amount) match {
        case Left(_) if left > 1 => loop(left - 1)
        case result              => result
      }
    loop(attempts)
  }
}
