package lessons.l01_basics

/** Lesson 1 — names, decisions, and small functions.
  *
  * A function is a named recipe. You give it inputs. It gives back one result.
  * In a test, the result is what you assert.
  *
  * `if` and `match` are expressions. They produce a value. They do not only
  * branch. You can store that value in a `val`.
  */
object Basics {

  /** `s"..."` is string interpolation. `$name` is replaced by the value. */
  def greet(name: String): String = s"Hello, $name"


  def add(a: Int, b: Int): Int = a + b

  /** `if` returns a String here. Every branch has the same type. */
  def describeSize(n: Int): String =
    if (n < 0) "negative"
    else if (n == 0) "zero"
    else "positive"

  /** `match` picks one branch. `_` means "anything else". */
  def weekday(n: Int): String = n match {
    case 1 => "Monday"
    case 2 => "Tuesday"
    case 3 => "Wednesday"
    case 4 => "Thursday"
    case 5 => "Friday"
    case 6 => "Saturday"
    case 7 => "Sunday"
    case _ => "unknown"
  }

  /** Recursion: a function that calls itself.
    * The base case stops the calls. Here, 0 and 1 both return 1.
    * `factorial(4)` is `4 * factorial(3)`, and so on, down to 1.
    */
  def factorial(n: Int): Int =
    if (n < 0) throw new IllegalArgumentException("n must be >= 0")
    else if (n <= 1) 1
    else n * factorial(n - 1)
}
