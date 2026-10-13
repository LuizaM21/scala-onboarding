package lessons.l08_solid

/** I — Interface Segregation.
  *
  * Many small traits are better than one big trait.
  * No class should be forced to implement a method it cannot support.
  *
  * Before: one big `Messenger`. The SMS gateway cannot send an email or a PDF,
  * but it must still write those methods. It fills them with `???` or an exception.
  * A caller sees `sendEmail` on the type and trusts it. Then it crashes.
  */
trait MessengerBefore {
  def sendEmail(to: String, subject: String, body: String): Unit
  def sendSms(phone: String, text: String): Unit
  def attachPdf(name: String, bytes: Array[Byte]): Unit
}

class SmsGatewayBefore extends MessengerBefore {
  def sendEmail(to: String, subject: String, body: String): Unit = ???
  def sendSms(phone: String, text: String): Unit = println(s"SMS to $phone: $text")
  def attachPdf(name: String, bytes: Array[Byte]): Unit = ???
}

/** After: one small trait per ability. A class picks the ones it can keep. */
trait EmailSender {
  def sendEmail(to: String, subject: String, body: String): Unit
}

trait SmsSender {
  def sendSms(phone: String, text: String): Unit
}

/** Only SMS. Nothing to fake. */
class SmsGateway extends SmsSender {
  def sendSms(phone: String, text: String): Unit = println(s"SMS to $phone: $text")
}

/** A class may still offer several abilities. It mixes in more than one trait. */
class OfficeMailServer extends EmailSender with SmsSender {
  def sendEmail(to: String, subject: String, body: String): Unit =
    println(s"Email to $to: $subject")

  def sendSms(phone: String, text: String): Unit = println(s"SMS to $phone: $text")
}

/** The caller asks for exactly what it uses.
  * In a test you fake one method, not three.
  */
class OverdueSmsAlert(sms: SmsSender) {
  def alert(phone: String, invoiceNumber: String): Unit =
    sms.sendSms(phone, s"Invoice $invoiceNumber is overdue")
}
