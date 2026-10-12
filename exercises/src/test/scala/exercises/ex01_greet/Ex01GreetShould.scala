package exercises.ex01_greet

class Ex01GreetShould extends munit.FunSuite {
  test("greet a person by name") {
    assertEquals(Greet.hello("Ana"), Right("`Hello, Ana`"))}

  test("keep the comma and the space when the name is empty") {
    assertEquals(Greet.hello(""), Right("`Hello, `"))}

  test("keep both parts of a two-word name") {
    assertEquals(Greet.hello("Ana Pop"), Right("`Hello, Ana Pop`"))}

  test("start the message with Hello") {
    assert(Greet.hello("QA").exists(_.startsWith("`Hello")))}
}
