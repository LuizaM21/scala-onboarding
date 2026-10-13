package lessons.l09_creational_structural

/** Factory — one place that decides how an object is built.
  *
  * Two kinds you will meet in Scala:
  *
  * 1. Smart constructor. The normal constructor is private.
  *    The only way in is a method that checks the input first.
  *    So a bad value cannot exist. Tests for "invalid input" go in one place.
  *
  * 2. Factory method. The caller says what it wants ("csv"). The factory picks the class.
  *    The caller never writes `new CsvExporter`. It only knows the `Exporter` trait.
  */

/** `private` on the constructor hides `new Iban(...)`, `Iban(...)` and `copy`.
  * Outside this file, `Iban.from` is the only door.
  */
final case class Iban private (value: String) {
  def country: String = value.take(2)
}

object Iban {
  private val Shape = "[A-Z]{2}[0-9]{2}[A-Z0-9]{10,30}".r

  def from(text: String): Either[String, Iban] = {
    val cleaned = text.replace(" ", "").toUpperCase
    if (Shape.matches(cleaned)) Right(new Iban(cleaned))
    else Left(s"not an IBAN: $text")
  }
}

/** Factory method. */
trait Exporter {
  def render(rows: List[(String, BigDecimal)]): String
}

class CsvExporter extends Exporter {
  def render(rows: List[(String, BigDecimal)]): String =
    rows.map { case (name, total) => s"$name,$total" }.mkString("\n")
}

class JsonExporter extends Exporter {
  def render(rows: List[(String, BigDecimal)]): String =
    rows.map { case (name, total) => s"""{"supplier":"$name","total":$total}""" }
      .mkString("[", ",", "]")
}

object Exporter {

  /** An unknown format is a normal answer, not a crash. So the result is `Either`. */
  def forFormat(format: String): Either[String, Exporter] =
    format.trim.toLowerCase match {
      case "csv"  => Right(CsvExporter())
      case "json" => Right(JsonExporter())
      case other  => Left(s"unknown format: $other")
    }
}
