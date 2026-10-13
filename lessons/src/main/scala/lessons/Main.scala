package lessons

import lessons.l01_basics.Demo as BasicsDemo
import lessons.l02_collections.Demo as CollectionsDemo
import lessons.l03_case_classes_traits.Demo as TypesDemo
import lessons.l04_option_either_errors.Demo as ErrorsDemo
import lessons.l05_invoice_report.Demo as ReportDemo
import lessons.l06_how_tests_work.Demo as TestsDemo
import lessons.l08_solid.Demo as SolidDemo
import lessons.l09_creational_structural.Demo as StructureDemo
import lessons.l10_behavioural.Demo as BehaviourDemo
import lessons.l11_putting_it_together.Demo as TogetherDemo

/** `sbt "lessons/run"` lists lessons. `sbt "lessons/run 1"` runs one demo. */
object Main {
  private val lessons = List(
    1 -> ("basics — val, var, functions, if, match, recursion", () => BasicsDemo.run()),
    2 -> ("collections — List, Map, Set, filter, fold, groupBy", () => CollectionsDemo.run()),
    3 -> ("case classes and traits — Money, Invoice, equality, copy", () => TypesDemo.run()),
    4 -> ("errors — Option, Either, Try, validation", () => ErrorsDemo.run()),
    5 -> ("invoice report — CSV, rates, totals", () => ReportDemo.run()),
    6 -> ("how tests work — MUnit, assert, intercept", () => TestsDemo.run()),
    7 -> ("Practice for lesson 1", () => BasicsDemo.runMyBasics()),
    8 -> ("SOLID — five principles, each as before and after", () => SolidDemo.run()),
    9 -> ("patterns I — Factory, Builder, Singleton, Adapter, Decorator", () => StructureDemo.run()),
    10 -> ("patterns II — Strategy, Observer, Chain, State", () => BehaviourDemo.run()),
    11 -> ("putting it together — layers, ports, and a use case", () => TogetherDemo.run())
  )

  def main(args: Array[String]): Unit = {
    args.headOption match {
      case None =>
        println("Lessons:")
        lessons.foreach { case (n, (title, _)) =>
          println(f"  $n%2d  $title")
        }
        println()
        println("""Run one lesson: sbt "lessons/run 1"""")
      case Some(raw) =>
        val found = raw.toIntOption.flatMap(n => lessons.find(_._1 == n))
        found match {
          case Some((_, (_, run))) => run()
          case None =>
            println(s"Unknown lesson: $raw")
            println(s"Use a number from 1 to ${lessons.size}.")
        }
    }
  }
}
