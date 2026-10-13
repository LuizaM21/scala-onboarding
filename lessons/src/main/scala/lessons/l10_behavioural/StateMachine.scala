package lessons.l10_behavioural

/** State — what an object may do depends on the state it is in.
  *
  * A draft can be sent. A sent invoice can be paid or cancelled.
  * A paid invoice cannot be cancelled. A cancelled one cannot be paid.
  *
  * Java often writes one class per state. In Scala an `enum` and a `match` are enough.
  * Each move returns `Either`: the new state, or why the move is not allowed.
  *
  * For a tester this is a state transition table. Each row is a test.
  *
  *   from \ action   send      pay       cancel
  *   Draft           Sent      error     Cancelled
  *   Sent            error     Paid      Cancelled
  *   Paid            error     error     error
  *   Cancelled       error     error     error
  */
enum Lifecycle {
  case Draft, Sent, Paid, Cancelled
}

enum Action {
  case Send, Pay, Cancel
}

object Lifecycle {
  import Lifecycle.*
  import Action.*

  def next(state: Lifecycle, action: Action): Either[String, Lifecycle] =
    (state, action) match {
      case (Draft, Send)   => Right(Sent)
      case (Draft, Cancel) => Right(Cancelled)
      case (Sent, Pay)     => Right(Paid)
      case (Sent, Cancel)  => Right(Cancelled)
      case (from, act)     => Left(s"cannot $act when $from")
    }

  /** Apply many actions. The first refused move stops the rest.
    * `foldLeft` carries the current `Either` from one action to the next.
    */
  def run(start: Lifecycle, actions: List[Action]): Either[String, Lifecycle] =
    actions.foldLeft[Either[String, Lifecycle]](Right(start)) { (current, action) =>
      current.flatMap(state => next(state, action))
    }
}
