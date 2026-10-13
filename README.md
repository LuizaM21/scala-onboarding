# Scala onboarding

A short Scala path for a QA engineer who already writes test cases.

You will read a small lesson, run it, then make a red test turn green.
The domain is supplier invoices: amounts, currencies, due dates, and a short report.

Lessons are finished. Exercises are yours. `solutions/` is the answer key.

## Folders

| Path | Role |
|------|------|
| `lessons/` | Finished lessons, demos, and green tests |
| `exercises/` | Stubs you edit, the tests, and this guide |
| `solutions/` | Finished exercise code. The same tests run here |
| `lessons/src/main/resources/` | `invoices.csv` and `exchange_rates.json` |

The CSV columns are `number,supplier,currency,total,issued_on,due_date`.
A rate is "how many EUR is 1 unit of that currency". USD `92/100` means 1 USD = 0.92 EUR.

## Install

Use **JDK 17 or 21**. Scala 3.3 and sbt 1.13 are not a good match for newer JDKs such as 25 or 26.

1. Install a JDK 21 (Temurin or another OpenJDK 21).
2. Install [Coursier](https://get-coursier.io/docs/cli-installation), then:

```bash
cs setup --yes
```

That puts `sbt` on your PATH. Check with `sbt --version` and `java -version`.

This project pins Scala **3.3.8**, sbt **1.13.0**, and MUnit **1.3.6** in `build.sbt` and `project/build.properties`.

### IntelliJ IDEA (use this)

1. Install IntelliJ IDEA Community.
2. Install the Scala plugin.
3. File | Open. Choose this folder, not a parent folder.
4. Trust the project. Import it as an sbt project.
5. Settings | Build, Execution, Deployment | Build Tools | sbt. Set the JDK to 21.
6. Open a test. Use the gutter icon to run one test.

### VS Code (alternative)

Install VS Code, the Metals extension, and open this folder. Metals uses the same sbt build.

## First commands

Open a terminal in this folder.

```bash
sbt
```

The `sbt` shell stays open. Type commands there without the `sbt` prefix.
From a normal terminal, wrap the command in quotes.

```bash
sbt "lessons/run"          # list lessons
sbt "lessons/run 1"        # print lesson 1
sbt "lessons/test"         # green tests for the lessons
sbt "exercises/test"       # your work; red until you finish
sbt "ex *Ex01*"            # one exercise
sbt "solutions/test"       # answer key; this must be green
sbt test                   # lessons only
```

`sbt test` does **not** run the exercises. A red exercise must not hide a green lesson.

## How to read test output

Same idea as a test run in QA.

- **Green** — the check passed. Actual equals expected.
- **Red** — the check failed. Read the test name first.
- `NotImplementedError` / `an implementation is missing` — the body is still `???`. That is expected before you write the code.
- `assertion failed` — your code ran, and the result was wrong. The output shows actual and expected.

A test name continues the class sentence. `Ex01GreetShould` + `greet a person by name` reads as one line. Names do not start with "should".

Inside the test, three comments match a test case:

```text
// setup     build the inputs
// execute   call the code
// verify    check the result
```

## Learning path

| Lesson | Run | Then do |
|--------|-----|---------|
| 1 Basics | `sbt "lessons/run 1"` | Exercise 1, 2 |
| 2 Collections | `sbt "lessons/run 2"` | Exercise 4, 5, 8 |
| 3 Case classes | `sbt "lessons/run 3"` | Exercise 3, 6 |
| 4 Option and Either | `sbt "lessons/run 4"` | Exercise 7, 9, 10 |
| 5 Invoice report | `sbt "lessons/run 5"` | Exercise 11 |
| 6 How tests work | `sbt "lessons/run 6"` | Exercise 12 |

### Part 2 — writing code that is easy to change

Part 1 taught the language. Part 2 is about design: how to shape code so it is easy to test,
easy to change, and easy for the next person to read. Same invoices, same test style.

Each lesson file shows a **Before** version and an **After** version. Read the Before first and ask:
"How would I test this?" The answer is usually "with difficulty". The After fixes that.

| Lesson | Run | Then do |
|--------|-----|---------|
| 8 SOLID | `sbt "lessons/run 8"` | Exercise 13, 14, 15, 16 |
| 9 Creational and structural patterns | `sbt "lessons/run 9"` | Exercise 17, 18, 19 |
| 10 Behavioural patterns | `sbt "lessons/run 10"` | Exercise 20, 21, 22, 23 |
| 11 Putting it together | `sbt "lessons/run 11"` | Exercise 24 |

One-page summary of every principle and pattern: [CHEATSHEET.md](CHEATSHEET.md).

Details, files, and commands: [exercises/EXERCISES.md](exercises/EXERCISES.md).

## Try first

Write the exercise yourself. Run its tests. Read the red message.
Open `solutions/src` only when you are stuck. Then close it and type the code yourself.

## When a test stays red

1. Read the test name. It says what failed.
2. Read `// verify`. That line is the expected result.
3. Read the comment at the top of the stub. It states the rule.
4. Change only that stub. Run the one-exercise command again.

A compile error is different from a red test. The code did not build. Fix the message above `Failed` before you read assertions.

## Watch mode

Inside the `sbt` shell, `~` runs again when you save a file.

```text
~ex *Ex01*
```

Save `Greet.scala`. The Ex01 tests run again. Press Enter to stop the watch.

## Glossary

| Word | Meaning |
|------|---------|
| `val` | A name that does not change. |
| `var` | A name you can assign again. Prefer `val`. |
| type | The kind of value, such as `String`, `Int`, `BigDecimal`. |
| method | A function on an object. `money.plus(other)` is a method call. |
| expression | Code that produces a value. `if` and `match` are expressions. |
| `match` | Pick one branch by pattern. `_` means anything else. |
| `List` | Ordered values. Duplicates are allowed. |
| `Map` | Keys paired with values. A supplier name to a total. |
| case class | A small data holder with equality and `copy`. |
| `trait` | A contract. A type promises to have certain methods. |
| `enum` | A fixed set of values, such as `EUR` or `Overdue`. |
| `Option` | `Some(value)` or `None` when the value may be missing. |
| `Either` | `Right(success)` or `Left(error)`. |
| assertion | A check inside a test. `assertEquals(actual, expected)`. |
| compile | Turn Scala text into something the JVM can run. A compile error is not a failed test. |
| `sbt` | The build tool. It compiles code and runs tests. |
| principle | A rule of thumb for design. SOLID is five of them. Not a law. |
| design pattern | A named, well-known shape for a common problem. "Adapter" says a lot in one word. |
| dependency | Something your code needs to do its job: a clock, a database, an email server. |
| port | A small trait that names a dependency. `Clock`, `Mailer`. |
| fake | A test version of a dependency. A clock that always says 2026-10-09. |
| contract test | One set of tests that every implementation of a trait must pass. |
| refactor | Change the shape of code without changing what it does. Tests stay green. |

## Free reading

- [Scala 3 book](https://docs.scala-lang.org/scala3/book/introduction.html)
- [Tour of Scala](https://docs.scala-lang.org/tour/tour-of-scala.html)
- [Scala Exercises](https://www.scala-exercises.org/)
- [Rock the JVM](https://rockthejvm.com/) — free articles and YouTube lessons
- [Refactoring Guru](https://refactoring.guru/design-patterns) — every pattern with pictures, and a list of code smells
- *Clean Code* and *Clean Architecture* by Robert C. Martin — where the SOLID names come from
- *Refactoring* by Martin Fowler — small safe steps, the idea behind exercise 24
