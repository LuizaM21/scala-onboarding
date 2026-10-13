package exercises.ex22_state

/** An approval workflow for incoming invoices. Lesson 10, State.
  *
  * This is a state transition table. You have tested tables like this before.
  * Now you write the code behind it.
  *
  *   from \ step     Review       Approve     Dispute     Resolve      Pay
  *   Received        UnderReview  -           -           -            -
  *   UnderReview     -            Approved    Disputed    -            -
  *   Disputed        -            -           -           UnderReview  -
  *   Approved        -            -           Disputed    -            Paid
  *   Paid            -            -           -           -            -
  *
  * A `-` is not allowed. Return `Left("cannot <step> when <state>")`,
  * for example `Left("cannot Pay when Received")`.
  *
  * 1. Write `next`: one step.
  * 2. Write `run`: many steps. Stop at the first refused step and return its error.
  *
  * Hint: match on the pair `(state, step)`. The last case catches everything else.
  * Hint: `run` is a `foldLeft` that starts at `Right(start)` and uses `flatMap`.
  *       Lesson 10 `StateMachine.scala` has the same shape.
  */
enum InvoiceState {
  case Received, UnderReview, Disputed, Approved, Paid
}

enum Step {
  case Review, Approve, Dispute, Resolve, Pay
}

object Workflow {
  def next(state: InvoiceState, step: Step): Either[String, InvoiceState] = ???

  def run(start: InvoiceState, steps: List[Step]): Either[String, InvoiceState] = ???
}
