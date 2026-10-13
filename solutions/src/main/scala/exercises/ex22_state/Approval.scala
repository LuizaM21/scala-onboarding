package exercises.ex22_state

enum InvoiceState {
  case Received, UnderReview, Disputed, Approved, Paid
}

enum Step {
  case Review, Approve, Dispute, Resolve, Pay
}

object Workflow {
  import InvoiceState.*
  import Step.*

  def next(state: InvoiceState, step: Step): Either[String, InvoiceState] =
    (state, step) match {
      case (Received, Review)     => Right(UnderReview)
      case (UnderReview, Approve) => Right(Approved)
      case (UnderReview, Dispute) => Right(Disputed)
      case (Disputed, Resolve)    => Right(UnderReview)
      case (Approved, Dispute)    => Right(Disputed)
      case (Approved, Pay)        => Right(Paid)
      case (from, action)         => Left(s"cannot $action when $from")
    }

  def run(start: InvoiceState, steps: List[Step]): Either[String, InvoiceState] =
    steps.foldLeft[Either[String, InvoiceState]](Right(start)) { (current, step) =>
      current.flatMap(next(_, step))
    }
}
