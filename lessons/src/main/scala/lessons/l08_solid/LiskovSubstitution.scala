package lessons.l08_solid

import scala.collection.mutable

/** L — Liskov Substitution.
  *
  * If code works with a parent type, it must also work with every child type.
  * A child may do more. It must not do less, and it must not surprise the caller.
  *
  * For a tester this is easy to say: every implementation must pass the parent's tests.
  * See `InvoiceReaderContract` in the test folder. The same tests run twice,
  * once per implementation.
  *
  * Before: an archive is read only, but it extends a store that promises `save`.
  * Code that receives a `StoreBefore` calls `save` and gets an exception.
  */
trait StoreBefore {
  def save(number: String, amount: BigDecimal): Unit
  def find(number: String): Option[BigDecimal]
}

class ArchiveBefore(data: Map[String, BigDecimal]) extends StoreBefore {
  // Surprise! The parent promised this works.
  def save(number: String, amount: BigDecimal): Unit =
    throw new UnsupportedOperationException("the archive is read only")

  def find(number: String): Option[BigDecimal] = data.get(number)
}

/** After: promise only what every child can keep.
  * Reading is one contract. Writing is an extra contract on top.
  */
trait InvoiceReader {
  def find(number: String): Option[BigDecimal]
}

trait InvoiceStore extends InvoiceReader {
  def save(number: String, amount: BigDecimal): Unit
}

/** Read only. It does not pretend it can save. */
class InvoiceArchive(data: Map[String, BigDecimal]) extends InvoiceReader {
  def find(number: String): Option[BigDecimal] = data.get(number)
}

/** Read and write. Usable anywhere an `InvoiceReader` is expected. */
class InMemoryInvoiceStore extends InvoiceStore {
  private val data = mutable.Map.empty[String, BigDecimal]

  def save(number: String, amount: BigDecimal): Unit = data.update(number, amount)

  def find(number: String): Option[BigDecimal] = data.get(number)
}

/** Works with both. It only needs what `InvoiceReader` promises. */
object Lookup {
  def amountOrZero(reader: InvoiceReader, number: String): BigDecimal =
    reader.find(number).getOrElse(BigDecimal("0.00"))
}
