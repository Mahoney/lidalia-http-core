package uk.org.lidalia.http.core

import uk.org.lidalia.http.core.headerfields.Host
import uk.org.lidalia.scalalang.ByteSeq
import uk.org.lidalia.net.{HostAndPort, SchemeWithDefaultPort, Uri}

import scala.collection.immutable
import scala.collection.immutable.Seq

object Request {

  def apply(
    method: Method,
    uri: RequestUri,
    headerFields: Seq[HeaderField]
  ): Request[ByteSeq, None.type] = {
    new Request(
      RequestHeader(
        method,
        uri,
        headerFields
      ),
      BytesUnmarshaller,
      EmptyEntity
    )
  }

  def apply[C](
    method: Method,
    uri: RequestUri,
    headerFields: immutable.Seq[HeaderField],
    entity: Entity[C]
  ) = {
    new Request(
      RequestHeader(
        method,
        uri,
        headerFields
      ),
      NoopEntityUnmarshaller,
      entity
    )
  }

  def apply[A, C](
    method: Method,
    uri: RequestUri,
    accept: Accept[A],
    headerFields: immutable.Seq[HeaderField],
    entity: Entity[C]
  ) = {
    new Request(
      RequestHeader(
        method,
        uri,
        List(accept) ++ headerFields
      ),
      accept,
      entity
    )
  }

  def apply[A](
    method: Method,
    uri: RequestUri,
    accept: Accept[A],
    headerFields: immutable.Seq[HeaderField]
  ) = {
    new Request(
      RequestHeader(
        method,
        uri,
        List(accept) ++ headerFields
      ),
      accept,
      EmptyEntity
    )
  }

  def apply(reqStr: String): Request[ByteSeq, String] = ???
}

class Request[+A, +C] private(
  override val header: RequestHeader,
  val unmarshaller: EntityUnmarshaller[A],
  entity: Entity[C]
) extends Message[C](header, entity) {

  def originalUrl(scheme: SchemeWithDefaultPort) = {

  }

  def withMethod(method: Method) = new Request(header.withMethod(method), unmarshaller, entity)

  def withUri(newUri: RequestUri) = new Request(header.withUri(newUri), unmarshaller, entity)

  def withHeaderField(headerField: HeaderField) = new Request(header.withHeaderField(headerField), unmarshaller, entity)

  val method = header.method

  val requestUri = header.requestUri

  lazy val referer: ?[Uri] = header.referer

  lazy val host: ?[HostAndPort] = header.host

  lazy val userAgent: ?[String] = header.userAgent
}
