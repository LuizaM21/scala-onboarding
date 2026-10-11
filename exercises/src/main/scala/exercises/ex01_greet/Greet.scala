package exercises.ex01_greet

/** Build a greeting.
  *
  * Task: `hello("Ana")` returns `Hello, Ana`.
  * Keep the comma and the space. An empty name returns `Hello, `.
  *
  * Hint: `s"Hello, $name"` inserts the name into the text.
  * Hint: replace `???`. That marker crashes on purpose until you do.
  */
object Greet {
  def hello(name: String): Either[String, String] =
      //validate the name input. Names with hyphens are allowed.
      if name.matches("""[\p{L}][\p{L} '\-]*""")
        then Right(s"`Hello, $name`")
      else if name.isEmpty then Right("`Hello, `")
        else Left(s"Invalid name $name")


}
