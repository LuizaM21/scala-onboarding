package exercises.ex10_validate
import scala.util.Try

/** Check three fields. Stop at the first problem.
  *
  * Return `Right(number)` when all of these are true:
  * - number is not blank (trim spaces)
  * - supplier is not blank
  * - total is a number and is >= 0
  *
  * Otherwise return `Left` with exactly one of:
  * - `number is blank`
  * - `supplier is blank`
  * - `total is not a number`
  * - `total is negative`
  *
  * Check in that order. Zero is allowed.
  *
  * Hint: a `for` comprehension stops at the first `Left`. Lesson 4 does this.
  * Hint: `BigDecimal(text)` throws `NumberFormatException` on bad text. Catch it.
  */
object Check {

  // One check per parameter. Each returns Right(valid value) or Left(message).
  private def checkNumber(number: String): Either[String, String] =
    Either.cond(number.trim.nonEmpty, number,"number is blank")

  private def checkSupplier(supplier: String): Either[String, String] =
    Either.cond(supplier.trim.nonEmpty, supplier.trim, "supplier is blank")

  private def checkTotal(total: String): Either[String, String] =
    Try(BigDecimal(total.trim)).toOption
      .toRight("total is not a number")
      .flatMap(t => Either.cond(t >= 0, t.toString(), "total is negative"))



  def validate(number: String, supplier: String, total: String): Either[String, String] =
      for n <- checkNumber(number)
          _ <- checkSupplier(supplier)
          _ <- checkTotal(total)
      yield n


}
