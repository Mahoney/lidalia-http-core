package uk.org.lidalia.http.core.headerfields

import SingleValueHeaderFieldNameTests.firstParseableValueReturned
import org.scalatest.PropSpec
import org.scalatest.prop.TableDrivenPropertyChecks
import uk.org.lidalia.net.Url

class RefererTests extends PropSpec with TableDrivenPropertyChecks {
  property("First Parseable Value Returned") {
    firstParseableValueReturned[Url](
      Referer,
      "http://example.com/1", Url("http://example.com/1"),
      "http://example.com/2", Url("http://example.com/2"),
      "not a uri")
  }
}
