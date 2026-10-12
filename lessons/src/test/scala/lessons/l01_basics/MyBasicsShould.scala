package lessons.l01_basics

class MyBasicsShould extends munit.FunSuite  {
  /*---------------Tests Function goodMorning() ------------------- */
  test("greet a person by name") {
    assertEquals(MyBasics.goodMorning("Ionut"), Right("Good morning Ionut!"))}

  test("greet a person with a hyphenated name") {
    assertEquals(MyBasics.goodMorning("Anne-Marie"), Right("Good morning Anne-Marie!"))
  }
  test("not compile when goodMorning receives a Char"){
    assert(compileErrors("""MyBasics.goodMorning('@')""").nonEmpty)}

  test("reject a name containing digits"){
    assertEquals(MyBasics.goodMorning("R2D2"), Left("Invalid name R2D2"))}

  test("reject an empty name"){
    assert(MyBasics.goodMorning("").isLeft)}


  /*---------------Tests Function maxValue() ------------------- */
  test("return the max value between two inputs"){
    assertEquals(MyBasics.maxValue(4,5),Right(5))}

  test("return an error if the input is not an integer"){
    assert(compileErrors("""MyBasics.maxValue('#')""").nonEmpty)}

  test("return an error when the two numbers are equal"){
    assertEquals(MyBasics.maxValue(3,3),Left("The numbers are equal!"))}


  /*---------------Tests Function grade() ------------------- */
  test("give A for score 90 or above"){
    assertEquals(MyBasics.grade(90), "A")
    assertEquals(MyBasics.grade(100), "A")
  }

  test("give B for 70 to 89"){
    assertEquals(MyBasics.grade(70), "B")
    assertEquals(MyBasics.grade(75), "B")
    assertEquals(MyBasics.grade(89), "B")
  }

  test ("give C for 50 to 69"){
    assertEquals(MyBasics.grade(50), "C")
    assertEquals(MyBasics.grade(65), "C")
    assertEquals(MyBasics.grade(69), "C")
  }

  test("give F for below 50") {
    assertEquals(MyBasics.grade(49), "F")
    assertEquals(MyBasics.grade(0), "F")
  }

  /*---------------Tests Function trafficLight() ------------------- */
  val validLightCombination: List[(String, String)] = List(
    "red" -> "stop",
    "RED" -> "stop",
    "rEd" -> "stop",
    "Yellow" -> "wait",
    "yElLow" -> "wait",
    "green" -> "go")

  val invalidLightCombination: List[(String, String)] = List(
    "r ed" -> "broken light",
    "yell ow" -> "broken light",
    "blue" -> "broken light",
  )

  validLightCombination.foreach: (color, expected) =>
    test(s"return $expected for valid color $color"){
      assertEquals(MyBasics.trafficLight(color), expected)}

  invalidLightCombination.foreach: (color, expected) =>
    test(s"return $expected for invalid $color"){
      assertEquals(MyBasics.trafficLight(color), expected)}


    /*---------------Tests Function sumTo() ------------------- */
  test("return the factorial addition results for the input number"){
    assertEquals(MyBasics.sumTo(5), Right(15))
    assertEquals(MyBasics.sumTo(4),Right(10))
    assertEquals(MyBasics.sumTo(1), Right(1))
    assertEquals(MyBasics.sumTo(0), Right(0))}

  test("return error if the number is negative"){
    assertEquals(MyBasics.sumTo(-2),Left("The number must not be negative!"))}
}
