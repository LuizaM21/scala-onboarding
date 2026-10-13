package lessons.l10_behavioural

/** Chain of Responsibility — pass a request along a line of handlers.
  * The first handler that can deal with it, does. The rest never see it.
  *
  * Here: who must approve a payment?
  * - up to 1 000: the accountant,
  * - up to 10 000: the finance manager,
  * - more: the CFO.
  *
  * Each approver is a small object. The chain is a plain `List`.
  * A new level ("team lead, up to 500") is one more item in the list.
  */
trait Approver {
  def name: String
  def canApprove(amount: BigDecimal): Boolean
}

case class UpTo(name: String, limit: BigDecimal) extends Approver {
  def canApprove(amount: BigDecimal): Boolean = amount <= limit
}

case class Anyone(name: String) extends Approver {
  def canApprove(amount: BigDecimal): Boolean = true
}

object Approval {
  val defaultChain: List[Approver] = List(
    UpTo("accountant", BigDecimal("1000")),
    UpTo("finance manager", BigDecimal("10000")),
    Anyone("CFO")
  )

  /** `find` walks the list and stops at the first match. That is the chain. */
  def whoApproves(amount: BigDecimal, chain: List[Approver] = defaultChain): Option[String] =
    chain.find(_.canApprove(amount)).map(_.name)
}

/* A second flavour: run every handler and collect what they say.
 * Validation works like that. Each rule returns `Some(error)` or `None`.
 * You saw a first version in lesson 4, which stopped at the first error.
 * This version reports all of them, which is nicer for the person fixing the data.
 */
object InvoiceRules {
  type Rule = (String, BigDecimal) => Option[String]

  val numberNotBlank: Rule = (number, _) => Option.when(number.trim.isEmpty)("number is blank")
  val amountPositive: Rule = (_, amount) => Option.when(amount <= 0)("amount must be positive")
  val amountNotHuge: Rule = (_, amount) => Option.when(amount > 1_000_000)("amount looks too big")

  val all: List[Rule] = List(numberNotBlank, amountPositive, amountNotHuge)

  def check(number: String, amount: BigDecimal): List[String] =
    all.flatMap(rule => rule(number, amount))
}
