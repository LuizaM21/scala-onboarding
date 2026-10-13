package lessons.l10_behavioural

class ApprovalShould extends munit.FunSuite {
  test("send a small payment to the accountant") {
    // execute

    val who = Approval.whoApproves(BigDecimal("1000"))

    // verify

    assertEquals(who, Some("accountant"))
  }

  test("send a payment just over the first limit to the finance manager") {
    // execute

    val who = Approval.whoApproves(BigDecimal("1000.01"))

    // verify

    assertEquals(who, Some("finance manager"))
  }

  test("send a large payment to the CFO") {
    // execute

    val who = Approval.whoApproves(BigDecimal("75000"))

    // verify

    assertEquals(who, Some("CFO"))
  }

  test("return None when no one in a custom chain can approve") {
    // setup

    val chain = List(UpTo("team lead", BigDecimal("500")))

    // execute

    val who = Approval.whoApproves(BigDecimal("501"), chain)

    // verify

    assertEquals(who, None)
  }

  test("report every broken rule, not only the first") {
    // execute

    val errors = InvoiceRules.check(" ", BigDecimal("-5"))

    // verify

    assertEquals(errors, List("number is blank", "amount must be positive"))
  }

  test("report nothing for a valid invoice") {
    // execute

    val errors = InvoiceRules.check("INV-001", BigDecimal("120.50"))

    // verify

    assertEquals(errors, Nil)
  }
}
