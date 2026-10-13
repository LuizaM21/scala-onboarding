package lessons.l10_behavioural

import Action.*
import Lifecycle.*

/** One test per interesting row of the transition table in `StateMachine.scala`. */
class LifecycleShould extends munit.FunSuite {
  test("move a draft to sent") {
    // execute

    val state = Lifecycle.next(Draft, Send)

    // verify

    assertEquals(state, Right(Sent))
  }

  test("move a sent invoice to paid") {
    // execute

    val state = Lifecycle.next(Sent, Pay)

    // verify

    assertEquals(state, Right(Paid))
  }

  test("refuse to pay a draft") {
    // execute

    val state = Lifecycle.next(Draft, Pay)

    // verify

    assertEquals(state, Left("cannot Pay when Draft"))
  }

  test("refuse to cancel a paid invoice") {
    // execute

    val state = Lifecycle.next(Paid, Cancel)

    // verify

    assertEquals(state, Left("cannot Cancel when Paid"))
  }

  test("follow a list of actions to the end") {
    // execute

    val state = Lifecycle.run(Draft, List(Send, Pay))

    // verify

    assertEquals(state, Right(Paid))
  }

  test("stop at the first refused action") {
    // execute

    val state = Lifecycle.run(Draft, List(Cancel, Send, Pay))

    // verify

    assertEquals(state, Left("cannot Send when Cancelled"))
  }
}
