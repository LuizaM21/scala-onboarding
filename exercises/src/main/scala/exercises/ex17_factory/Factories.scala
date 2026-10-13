package exercises.ex17_factory

import scala.math.BigDecimal.RoundingMode

/** Two factories. Lesson 9, Factory.
  *
  * Part 1 — a smart constructor.
  * `InvoiceNumber` has a private constructor. `InvoiceNumber.from` is the only way in.
  * - Trim spaces and turn the text to upper case.
  * - Blank text: `Left("invoice number is blank")`.
  * - It must look like `INV-` and then at least 3 digits: `INV-001`, `INV-12345`.
  *   Anything else: `Left("bad invoice number: <the text you received>")`.
  * - Otherwise: `Right(new InvoiceNumber(cleaned))`.
  *
  * Part 2 — a factory method.
  * `AmountFormat.forName` picks a formatter by name. Ignore case and spaces.
  * - `plain`      -> `PlainFormat`       `120.50 EUR`
  * - `accounting` -> `AccountingFormat`  `EUR 120.50`, and a negative amount as `EUR (120.50)`
  * - anything else: `Left("unknown format: <name>")`
  *
  * `PlainFormat` is done. Write `AccountingFormat` and `forName`.
  *
  * Hint: `"INV-[0-9]{3,}".r.matches(text)` is true when the whole text fits.
  * Hint: `amount.abs` drops the minus sign.
  */
final case class InvoiceNumber private (value: String)

object InvoiceNumber {
  def from(text: String): Either[String, InvoiceNumber] = ???
}

trait AmountFormat {
  def format(amount: BigDecimal, currency: String): String
}

object PlainFormat extends AmountFormat {
  def format(amount: BigDecimal, currency: String): String =
    s"${amount.setScale(2, RoundingMode.HALF_UP)} $currency"
}

object AccountingFormat extends AmountFormat {
  def format(amount: BigDecimal, currency: String): String = ???
}

object AmountFormat {
  def forName(name: String): Either[String, AmountFormat] = ???
}
