package uk.org.lidalia.http.core

import org.scalatest
import scalatest.PropSpec
import scalatest.prop.TableDrivenPropertyChecks
import uk.org.lidalia.scalalang.EqualsChecks.{equalsTest, possibleArgsFor, reflexiveTest}

class HeaderFieldTests extends PropSpec with TableDrivenPropertyChecks {

  property("HeaderField equals contract") {
    val argsForHeaderField = possibleArgsFor(
      List("Name1", "Name2"),
      List(
        List("value1"),
        List("value1", "value2"),
        List("value2")
      )
    )

    reflexiveTest(argsForHeaderField) { (args) =>
      HeaderField(args._1, args._2)
    }

    equalsTest(argsForHeaderField) { (args) =>
      HeaderField(args._1, args._2)
    }
  }

  val toStringProperties = Table(
    ("name",         "values",                 "result"),
    ("Header-Field", List(),                   "Header-Field: "),
    ("Header-Field", List("value1"),           "Header-Field: value1"),
    ("Header-Field", List("value1", "value2"), "Header-Field: value1, value2")
  )

  property("to string formats header field correctly") {
    forAll(toStringProperties) { (name, values, result) =>
      val headerField = HeaderField(name, values)
      assert( headerField.toString === result)
    }
  }
}
