package exercises.ex15_liskov_contract

/** The contract every `RateStore` must keep.
  *
  * `abstract` means MUnit does not run this class by itself.
  * Each class at the bottom says how to build one store. Then all these tests run for it.
  * Lesson 8 has the same idea in `InvoiceReaderContract`.
  */
abstract class Ex15RateStoreContract extends munit.FunSuite {
  def emptyStore(): RateStore

  test("return a rate after it was put") {
    // setup

    val store = emptyStore()

    // execute

    store.put("USD", BigDecimal("0.92"))

    // verify

    assertEquals(store.get("USD"), Some(BigDecimal("0.92")))
  }

  test("return None for a currency that was never put") {
    // setup

    val store = emptyStore()

    // execute

    val rate = store.get("CHF")

    // verify

    assertEquals(rate, None)
  }

  test("return the latest rate when the same currency is put twice") {
    // setup

    val store = emptyStore()

    // execute

    store.put("USD", BigDecimal("0.92"))
    store.put("USD", BigDecimal("0.95"))

    // verify

    assertEquals(store.get("USD"), Some(BigDecimal("0.95")))
  }

  test("list each currency once") {
    // setup

    val store = emptyStore()

    // execute

    store.put("USD", BigDecimal("0.92"))
    store.put("RON", BigDecimal("0.20"))
    store.put("USD", BigDecimal("0.95"))

    // verify

    assertEquals(store.currencies, Set("USD", "RON"))
  }

  test("start with no currencies") {
    // execute & verify

    assertEquals(emptyStore().currencies, Set.empty[String])
  }
}

class Ex15MapRateStoreShould extends Ex15RateStoreContract {
  def emptyStore(): RateStore = MapRateStore()
}

class Ex15HistoryRateStoreShould extends Ex15RateStoreContract {
  def emptyStore(): RateStore = HistoryRateStore()

  test("keep every rate in the history, oldest first") {
    // setup

    val store = HistoryRateStore()

    // execute

    store.put("USD", BigDecimal("0.92"))
    store.put("USD", BigDecimal("0.95"))

    // verify

    assertEquals(store.history("USD"), List(BigDecimal("0.92"), BigDecimal("0.95")))
  }
}
