package exercises.ex19_decorator

/** Wrap a payment sender with extra behaviour. Lesson 9, Decorator.
  *
  * `PaymentSender.send` returns `Right(confirmationId)` or `Left(error)`.
  * Write two wrappers. Each one IS a `PaymentSender` and HAS a `PaymentSender` inside.
  *
  * 1. `LimitCheck(inner, max)`
  *    - Amount above `max`: return `Left("amount over limit")` and do NOT call `inner`.
  *    - Otherwise: call `inner` and return its answer.
  *
  * 2. `Retry(inner, attempts)`
  *    - Call `inner`. On `Right`, stop and return it.
  *    - On `Left`, try again, up to `attempts` calls in total.
  *    - If every call fails, return the last `Left`.
  *    - `attempts` is at least 1. You do not need to check it.
  *
  * The tests use a fake sender that fails a fixed number of times, then succeeds.
  * They count how many times `inner` was called. That is how you test a retry.
  *
  * Hint: a small recursive helper `loop(left: Int)` is a clean way to retry. Lesson 1 shows recursion.
  */
trait PaymentSender {
  def send(invoice: String, amount: BigDecimal): Either[String, String]
}

class LimitCheck(inner: PaymentSender, max: BigDecimal) extends PaymentSender {
  def send(invoice: String, amount: BigDecimal): Either[String, String] = ???
}

class Retry(inner: PaymentSender, attempts: Int) extends PaymentSender {
  def send(invoice: String, amount: BigDecimal): Either[String, String] = ???
}
