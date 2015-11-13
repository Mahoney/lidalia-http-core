package uk.org.lidalia.http.core

import uk.org.lidalia
import lidalia.http.core.headerfields.{Age, Date, Etag, LastModified, Location}
import org.joda.time.{DateTime, Duration}
import uk.org.lidalia.net.Url

import scala.collection.immutable.Seq

object ResponseHeader {
    def apply(
               status: Code,
               reason: Reason,
               headerFields: Seq[HeaderField]
               ): ResponseHeader = new ResponseHeader(status, reason, headerFields)

    def apply(
             status: Code,
             headerFields: Seq[HeaderField]
             ): ResponseHeader = new ResponseHeader(status, status.defaultReason or Reason(""), headerFields)
}

class ResponseHeader private(@Identity val code: Code,
                             @Identity val reason: Reason,
                             headerFields: Seq[HeaderField]) extends MessageHeader(headerFields) {

  def requiresRedirect: Boolean = code.requiresRedirect

  lazy val location: ?[Url] = headerField(Location)

  lazy val date: ?[DateTime] = headerField(Date)

  lazy val age: ?[Duration] = headerField(Age)

  lazy val lastModified: ?[DateTime] = headerField(LastModified)

  lazy val etag: ?[String] = headerField(Etag)

  override def toString = s"HTTP/1.1 $code $reason\r\n${super.toString}"

  def isNotError: Boolean = code.isNotError
  def isInformational: Boolean = code.isInformational
  def isSuccessful: Boolean = code.isSuccessful
  def isRedirection: Boolean = code.isRedirection
  def isClientError: Boolean = code.isClientError
  def isServerError: Boolean = code.isServerError
  def isError: Boolean = code.isError
}
