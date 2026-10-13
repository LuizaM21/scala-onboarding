package exercises.ex17_factory

import scala.math.BigDecimal.RoundingMode

final case class InvoiceNumber private (value: String)

object InvoiceNumber {
  private val Shape = "INV-[0-9]{3,}".r

  def from(text: String): Either[String, InvoiceNumber] = {
    val cleaned = text.trim.toUpperCase
    if (cleaned.isEmpty) Left("invoice number is blank")
    else if (!Shape.matches(cleaned)) Left(s"bad invoice number: $text")
    else Right(new InvoiceNumber(cleaned))
  }
}

trait AmountFormat {
  def format(amount: BigDecimal, currency: String): String
}

object PlainFormat extends AmountFormat {
  def format(amount: BigDecimal, currency: String): String =
    s"${amount.setScale(2, RoundingMode.HALF_UP)} $currency"
}

object AccountingFormat extends AmountFormat {
  def format(amount: BigDecimal, currency: String): String = {
    val digits = amount.abs.setScale(2, RoundingMode.HALF_UP)
    if (amount < 0) s"$currency ($digits)" else s"$currency $digits"
  }
}

object AmountFormat {
  def forName(name: String): Either[String, AmountFormat] =
    name.trim.toLowerCase match {
      case "plain"      => Right(PlainFormat)
      case "accounting" => Right(AccountingFormat)
      case _            => Left(s"unknown format: $name")
    }
}
