package lessons.l01_basics
/**
 * $env:Path += ";$env:LOCALAPPDATA\Coursier\data\bin"
 * sbt "lessons/run"
 * this is in case the sbt is not recognised as a building tool*/



/** Run this lesson with: sbt "lessons/run 1" */
object Demo {
  def run(): Unit = {
    println("Lesson 1 — Basics")
    println()

    // val is a fixed name. You cannot assign it again.
    val course = "Scala"
    // var can change. Prefer val. Use var only when a value must move.
    var step = 1
    step = step + 1

    println(s"Course: $course, step: $step")
    println(Basics.greet("Ana"))
    println(s"2 + 3 = ${Basics.add(2, 3)}")
    println(s"-4 is ${Basics.describeSize(-4)}")
    println(s"day 5 is ${Basics.weekday(5)}")
    println(s"4! = ${Basics.factorial(4)}")
  }

  def runMyBasics(): Unit = {
    println("----------Practice for Lesson_01---------------")
    println(s"${MyBasics.goodMorning("Ionut")}")
    val compare1 = 3
    val compare2 = 7
    println(s"The greater number between $compare1 and $compare2 is ${MyBasics.maxValue(3, 7)}")
    val testNumber = "7"
    println(s"Is the number $testNumber even or not: ${MyBasics.isEven(testNumber)}")
    val score = 90
    println(s"The obtained score  of $score is ${MyBasics.grade(score)}")
    val light =  "red"
    println(s"The light is $light so you must ${MyBasics.trafficLight(light)}")
    val recursionNumber = 4
    println(s"$recursionNumber! is ${MyBasics.sumTo(4)}")
  }
}