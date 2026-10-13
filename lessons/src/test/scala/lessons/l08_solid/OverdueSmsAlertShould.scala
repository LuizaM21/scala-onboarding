package lessons.l08_solid

import scala.collection.mutable.ListBuffer

/** The fake implements one method. With `MessengerBefore` it would need three. */
class OverdueSmsAlertShould extends munit.FunSuite {
  test("send one SMS that names the invoice") {
    // setup

    val sent = ListBuffer.empty[(String, String)]
    val fakeSms = new SmsSender {
      def sendSms(phone: String, text: String): Unit = sent += (phone -> text)
    }
    val alert = OverdueSmsAlert(fakeSms)

    // execute

    alert.alert("+40 700 000 000", "INV-001")

    // verify

    assertEquals(sent.toList, List("+40 700 000 000" -> "Invoice INV-001 is overdue"))
  }
}
