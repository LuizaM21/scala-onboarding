package exercises.ex22_state

import InvoiceState.*
import Step.*

class Ex22StateShould extends munit.FunSuite {
  test("move a received invoice under review") {
    // execute

    val state = Workflow.next(Received, Review)

    // verify

    assertEquals(state, Right(UnderReview))
  }

  test("approve an invoice under review") {
    // execute

    val state = Workflow.next(UnderReview, Approve)

    // verify

    assertEquals(state, Right(Approved))
  }

  test("send a resolved dispute back to review") {
    // execute

    val state = Workflow.next(Disputed, Resolve)

    // verify

    assertEquals(state, Right(UnderReview))
  }

  test("allow a dispute after approval") {
    // execute

    val state = Workflow.next(Approved, Dispute)

    // verify

    assertEquals(state, Right(Disputed))
  }

  test("refuse to pay an invoice that was not approved") {
    // execute

    val state = Workflow.next(Received, Pay)

    // verify

    assertEquals(state, Left("cannot Pay when Received"))
  }

  test("refuse every step once paid") {
    // execute

    val results = Step.values.toList.map(step => Workflow.next(Paid, step))

    // verify

    assert(results.forall(_.isLeft))
  }

  test("follow the happy path from received to paid") {
    // execute

    val state = Workflow.run(Received, List(Review, Approve, Pay))

    // verify

    assertEquals(state, Right(Paid))
  }

  test("follow a path with a dispute in the middle") {
    // execute

    val state = Workflow.run(Received, List(Review, Dispute, Resolve, Approve, Pay))

    // verify

    assertEquals(state, Right(Paid))
  }

  test("stop at the first refused step") {
    // execute

    val state = Workflow.run(Received, List(Review, Pay, Approve))

    // verify

    assertEquals(state, Left("cannot Pay when UnderReview"))
  }

  test("stay in the start state when there are no steps") {
    // execute

    val state = Workflow.run(Disputed, Nil)

    // verify

    assertEquals(state, Right(Disputed))
  }
}
