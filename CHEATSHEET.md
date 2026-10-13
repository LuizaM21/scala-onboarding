# SOLID and patterns — one page

Keep this open while you do lessons 8 to 11. Each row points to the lesson file that shows it.

## SOLID

| Letter | In one line | Smell that it is missing | Scala tool | Lesson file |
|--------|-------------|--------------------------|------------|-------------|
| **S** Single Responsibility | One reason to change. | One test checks parsing, maths, and text at once. | Small `object`s, small functions | `l08_solid/SingleResponsibility.scala` |
| **O** Open/Closed | Add new code, do not edit working code. | A `match` on a string that grows every month. | `trait` + one class per case | `l08_solid/OpenClosed.scala` |
| **L** Liskov Substitution | A child passes the parent's tests. | A method that throws "not supported". | Split the trait; contract tests | `l08_solid/LiskovSubstitution.scala` |
| **I** Interface Segregation | Small traits. No fake methods. | `???` in a method you were forced to write. | Several small traits, mixed with `with` | `l08_solid/InterfaceSegregation.scala` |
| **D** Dependency Inversion | Pass the tools in. Do not grab them. | `LocalDate.now()` or `println` inside business code. | Constructor parameters typed as traits | `l08_solid/DependencyInversion.scala` |

The tester's view: **if it is hard to test, the design is telling you something.**
Hard to set up means too many jobs (S). Cannot control time or I/O means a hidden dependency (D).

## Patterns

| Pattern | Problem it solves | Scala shape | Lesson file | Exercise |
|---------|-------------------|-------------|-------------|----------|
| Factory (smart constructor) | A bad value must never exist. | `case class X private (...)` + `X.from(...): Either` | `l09_.../Factory.scala` | 17 |
| Factory method | The caller should not pick the class. | `def forName(name): Either[String, Trait]` | `l09_.../Factory.scala` | 17 |
| Builder | Big objects with many optional fields. | Named and default arguments, `copy`. Test data builders. | `l09_.../Builder.scala` | — |
| Singleton | Exactly one instance. | `object`. Avoid changing state inside it. | `l09_.../Singleton.scala` | — |
| Adapter | A class you cannot change has the wrong shape. | A class that implements your trait and calls theirs | `l09_.../Adapter.scala` | 18 |
| Decorator | Add behaviour (cache, log, retry) without editing. | A class that implements a trait and wraps the same trait | `l09_.../Decorator.scala` | 19 |
| Strategy | Choose an algorithm at run time. | A function value, or a small trait | `l10_behavioural/Strategy.scala` | 20 |
| Template Method | Fixed steps, one step changes. | Prefer passing the step as a function (composition) | `l10_behavioural/Strategy.scala` | — |
| Observer | Tell many listeners, know none of them. | A list of `Event => Unit` functions | `l10_behavioural/Observer.scala` | 21 |
| State | What is allowed depends on the current state. | `enum` + `match` on `(state, action)` returning `Either` | `l10_behavioural/StateMachine.scala` | 22 |
| Chain of Responsibility | Pass a request along until someone handles it. | A `List` of handlers + `find` / first `Some` | `l10_behavioural/ChainOfResponsibility.scala` | 23 |

## Ports and adapters (lesson 11)

```text
 Infrastructure            Application                 Domain
 (real tools)              (use cases)                 (business rules)

 SystemClock ──┐                                       OpenInvoice
 SqlRepository ├──implements──► ports ◄──uses── SendOverdueRemindersService
 EmailNotifier ┘               (Clock, InvoiceRepository, Notifier)
```

Arrows point inward. The domain does not know about databases or email.
Tests replace the left column with fakes. Many backend services, Java or Scala, use the same layout.

## Do not overdo it

Patterns are tools, not goals. Three `if`s do not need a Strategy.
Add the shape when the second or third case arrives, or when a test is painful to write.
Simple code you can read beats clever code with five patterns.

## Common code smells

| Smell | Usually fixed by |
|-------|------------------|
| Long method that does several things | Extract functions (S) |
| Same `match` on a type in many places | Trait + one class per case (O), or a sealed `enum` if the list is fixed |
| Magic numbers like `0.02` and `30` | Named `val`s |
| Many `var`s and a `for` loop | `map`, `filter`, `foldLeft` |
| `null` | `Option` |
| Throwing for a normal failure | `Either` |
| A test needs the real clock, disk, or network | Pass a trait in (D) and use a fake |
