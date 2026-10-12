package lessons.l01_basics

import java.util.Locale

object MyBasics {

  /** 1. Return "Good morning, <name>!"*/
  def goodMorning(name: String):  Either[String, String] =
    if name.matches("""[\p{L}][\p{L} '\-]*""")
      then Right(s"Good morning $name!")
    else Left(s"Invalid name $name")

  /** 2. Return the bigger of two numbers using `if`*/
  def maxValue(x: Int, y: Int): Either[String, Int] =
    if x < y then Right(y)
    else if x > y then Right(x)
    else Left("The numbers are equal!")

  /** 3. Return true if n is even.*/
  def isEven(input: String): Either[String, Boolean] =
    input.trim.toIntOption match
      case Some(input) => Right(input % 2 == 0)
      case None => Left(s"Not an integer: $input")

  /** 4. Grade a score using if / else if / else:
   * 90 or more -> "A", 70 or more -> "B", 50 or more -> "C", otherwise "F"
   */
  def grade(score: Int): String =
    if score >= 90 then "A"
    else if score >= 70  then "B"
    else if score >= 50 then "C"
    else "F"

  /** 5. Use `match`: "red" -> "stop", "yellow" -> "wait",
   * "green" -> "go", anything else -> "broken light"
   */
  def trafficLight(color: String): String =
    color.trim.toLowerCase(Locale.ROOT) match
      case "red" => "stop"
      case "yellow" => "wait"
      case "green" => "go"
      case _ => "broken light"

  /** 6. Recursion: sum of all numbers from 1 to n.
   * sumTo(4) = 4 + 3 + 2 + 1 = 10. Base case: sumTo(0) = 0
   */
  def sumTo(n: Int): Either[String, Int] =
    if n < 0 then Left("The number must not be negative!")
    else Right(sumRec(n))
  
  private def sumRec(n: Int): Int =
    if n == 0 then 0
    else n + sumRec(n-1)

}
