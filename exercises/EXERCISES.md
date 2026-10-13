# Exercises

Do these after the lesson in the table. Edit only the file named here.
Run the test command. Red means the code is not done yet.

`???` throws `NotImplementedError` on purpose. Replace it with your code.

Exercise 12 is different: the code is done. You write the missing test.

Exercises 13 to 24 are Part 2: design. They are less about syntax and more about shape.
- Exercise 15 has its tests in an `abstract class` contract. The same tests run against two classes.
- Exercise 16 has two parts: the code, then one ignored test for you to write with the fakes.
- Exercise 24 starts **green**. The code works but it is messy. Refactor it in small steps and
  run the tests after each one. There is no single right answer.

Try first. Open `solutions/` only after a real attempt.

| # | Goal | Read first | Edit | Test |
|---|------|------------|------|------|
| 1 | Return `Hello, Ana` | Lesson 1 | `exercises/src/main/scala/exercises/ex01_greet/Greet.scala` | `sbt "ex *Ex01*"` |
| 2 | VAT on a net amount, two decimals, half up | Lesson 1 | `exercises/src/main/scala/exercises/ex02_vat/Vat.scala` | `sbt "ex *Ex02*"` |
| 3 | Due date before today is overdue. Today is not | Lesson 3 | `exercises/src/main/scala/exercises/ex03_overdue/Due.scala` | `sbt "ex *Ex03*"` |
| 4 | Sum a list of amounts. Empty list is `0.00` | Lesson 2 | `exercises/src/main/scala/exercises/ex04_sum/Totals.scala` | `sbt "ex *Ex04*"` |
| 5 | Keep amounts greater than or equal to a limit | Lesson 2 | `exercises/src/main/scala/exercises/ex05_filter/Filters.scala` | `sbt "ex *Ex05*"` |
| 6 | Discount rules with `match` | Lesson 1 and 3 | `exercises/src/main/scala/exercises/ex06_discount/Discount.scala` | `sbt "ex *Ex06*"` |
| 7 | Parse one CSV line into `Either` | Lesson 4 | `exercises/src/main/scala/exercises/ex07_parse_line/LineParser.scala` | `sbt "ex *Ex07*"` |
| 8 | Sum amounts grouped by supplier | Lesson 2 | `exercises/src/main/scala/exercises/ex08_group/GroupTotals.scala` | `sbt "ex *Ex08*"` |
| 9 | Convert to EUR. Missing rate is `None` | Lesson 4 | `exercises/src/main/scala/exercises/ex09_convert/Convert.scala` | `sbt "ex *Ex09*"` |
| 10 | Validate number, supplier, and total | Lesson 4 | `exercises/src/main/scala/exercises/ex10_validate/Check.scala` | `sbt "ex *Ex10*"` |
| 11 | Mini report: skip bad rows, convert, group | Lesson 5 | `exercises/src/main/scala/exercises/ex11_report/MiniReport.scala` | `sbt "ex *Ex11*"` |
| 12 | Write the missing "due today" test | Lesson 6 | `exercises/src/test/scala/exercises/ex12_write_test/Ex12WriteTestShould.scala` | `sbt "ex *Ex12*"` |
| 13 | S: split one job into parse, rule, and text | Lesson 8 | `exercises/src/main/scala/exercises/ex13_single_responsibility/Reminders.scala` | `sbt "ex *Ex13*"` |
| 14 | O: add discount rules without editing the engine | Lesson 8 | `exercises/src/main/scala/exercises/ex14_open_closed/Discounts.scala` | `sbt "ex *Ex14*"` |
| 15 | L: two rate stores pass one contract test | Lesson 8 | `exercises/src/main/scala/exercises/ex15_liskov_contract/RateStores.scala` | `sbt "ex *Ex15*"` |
| 16 | I + D: a use case with fakes, then write one test | Lesson 8 and 11 | `exercises/src/main/scala/exercises/ex16_dependency_inversion/ChaseUnpaid.scala` and its test | `sbt "ex *Ex16*"` |
| 17 | Factory: smart constructor and factory method | Lesson 9 | `exercises/src/main/scala/exercises/ex17_factory/Factories.scala` | `sbt "ex *Ex17*"` |
| 18 | Adapter: fit an old rates feed to a new trait | Lesson 9 | `exercises/src/main/scala/exercises/ex18_adapter/RatesAdapter.scala` | `sbt "ex *Ex18*"` |
| 19 | Decorator: limit check and retry around a sender | Lesson 9 | `exercises/src/main/scala/exercises/ex19_decorator/Senders.scala` | `sbt "ex *Ex19*"` |
| 20 | Strategy: payment terms as functions | Lesson 10 | `exercises/src/main/scala/exercises/ex20_strategy/PaymentTerms.scala` | `sbt "ex *Ex20*"` |
| 21 | Observer: a payment event bus and a listener | Lesson 10 | `exercises/src/main/scala/exercises/ex21_observer/PaymentEvents.scala` | `sbt "ex *Ex21*"` |
| 22 | State: an approval workflow table | Lesson 10 | `exercises/src/main/scala/exercises/ex22_state/Approval.scala` | `sbt "ex *Ex22*"` |
| 23 | Chain of Responsibility: route a payment | Lesson 10 | `exercises/src/main/scala/exercises/ex23_chain/PaymentRouting.scala` | `sbt "ex *Ex23*"` |
| 24 | Capstone: refactor messy code while tests stay green | Lesson 11 | `exercises/src/main/scala/exercises/ex24_refactor/PaymentRun.scala` | `sbt "ex *Ex24*"` |

Each stub has a short task comment and one or two hints.

Inside the `sbt` shell you can omit `sbt` and the quotes:

```text
ex *Ex01*
~ex *Ex01*
```

`~` runs the tests again each time you save.
