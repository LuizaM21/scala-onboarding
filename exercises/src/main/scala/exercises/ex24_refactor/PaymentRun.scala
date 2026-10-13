package exercises.ex24_refactor

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import scala.math.BigDecimal.RoundingMode

/** Capstone: refactor while green.
  *
  * This exercise is different again. The code below WORKS. Its tests are already green.
  * They are "characterization tests": they pin down what the code does today,
  * so you can change its shape without changing its behaviour.
  *
  * Your job: make this code clean, one small step at a time.
  * Run `sbt "ex *Ex24*"` after every step. Green means you did not break anything.
  * Red means undo the last step and try a smaller one.
  *
  * Keep `PaymentRun.plan(lines, today)` with the same signature. The tests call it.
  * Everything inside is yours to change. Add classes, traits, objects, files.
  *
  * Suggested steps. Each one is a lesson you have done:
  * 1. Name the magic values. What are 0.92, 0.20, 30, 0.02, 10? (lesson 1)
  * 2. Give a row a type: a `case class` instead of `parts(3)`. (lesson 3)
  * 3. Pull parsing into its own function that returns `Either`. (lesson 4, letter S)
  * 4. Pull the late fee into a `LateFee` trait or function. (letter O, Strategy)
  * 5. Pull currency conversion into a `Rates` trait, passed in. (letter D)
  *    `plan(lines, today)` can call a new `plan(lines, today, rates, fee)` with the defaults.
  * 6. Replace the `var`s and `for` loop with `map`, `filter`, `collect`, `foldLeft`. (lesson 2)
  * 7. Pull the text output into its own function. (letter S)
  *
  * There is no single right answer. `solutions/` shows one possible result.
  * Read it only after you are green and happy with yours. Compare. Discuss.
  */
object PaymentRun {
  def plan(lines: List[String], today: LocalDate): String = {
    var out = ""
    var total = BigDecimal(0)
    var count = 0
    var bad = 0
    for (l <- lines) {
      val p = l.split(",", -1)
      if (p.length == 6) {
        if (p(5).trim == "OPEN") {
          var amt = BigDecimal(0)
          var ok = true
          try {
            amt = BigDecimal(p(3).trim)
          } catch {
            case e: Exception => ok = false
          }
          var d: LocalDate = null
          try {
            d = LocalDate.parse(p(4).trim)
          } catch {
            case e: Exception => ok = false
          }
          if (ok) {
            var eur = BigDecimal(0)
            if (p(2).trim == "EUR") eur = amt
            else if (p(2).trim == "USD") eur = amt * 0.92
            else if (p(2).trim == "RON") eur = amt * 0.20
            else ok = false
            if (ok) {
              val late = ChronoUnit.DAYS.between(d, today)
              var fee = BigDecimal(0)
              if (late > 0) {
                if (late > 30) fee = eur * 0.02
                else fee = 10
              }
              val pay = (eur + fee).setScale(2, RoundingMode.HALF_UP)
              out = out + p(0).trim + " " + p(1).trim + " " + pay + " EUR" +
                (if (fee > 0) " (late fee " + fee.setScale(2, RoundingMode.HALF_UP) + ")" else "") + "\n"
              total = total + pay
              count = count + 1
            } else bad = bad + 1
          } else bad = bad + 1
        }
      } else bad = bad + 1
    }
    out + "Payments: " + count + "\n" + "Total: " + total.setScale(2, RoundingMode.HALF_UP) + " EUR\n" +
      "Skipped: " + bad
  }
}
