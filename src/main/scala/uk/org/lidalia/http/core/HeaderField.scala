package uk.org.lidalia.http.core

import uk.org.lidalia.lang.RichObject

import scala.collection.immutable.Seq

object HeaderField {
  def apply(name: String, values: String*): HeaderField = apply(name, values.to[Seq])
  def apply(name: String, values: Seq[String]): HeaderField = new HeaderField(name, values)
}

class HeaderField protected(@Identity val name: String, @Identity val values: Seq[String]) extends RichObject {
  override def toString = s"$name: $valueString"
  def valueString = values.mkString(", ")

  def isA(otherName: HeaderFieldName[_]) = name.equalsIgnoreCase(otherName.name)
}
