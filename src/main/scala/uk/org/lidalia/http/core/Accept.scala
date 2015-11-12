package uk.org.lidalia.http.core

import scala.collection.immutable.Seq

abstract class Accept[T](mediaRangePrefs: Seq[MediaRangePref])
  extends uk.org.lidalia.http.core.headerfields.Accept(mediaRangePrefs)
  with EntityUnmarshaller[T] {
}
