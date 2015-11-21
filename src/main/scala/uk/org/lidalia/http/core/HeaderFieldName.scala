package uk.org.lidalia.http.core

import scala.collection.immutable.Seq

trait HeaderFieldName[T] {
  def parse(headerFieldValues: Seq[String]): T
  def name: String
  override val toString = name

  final def :=[C <: HeaderField](headerField: C): C = headerField
}
