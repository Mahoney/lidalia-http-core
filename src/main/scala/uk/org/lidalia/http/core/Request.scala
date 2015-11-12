package uk.org.lidalia.http.core

import uk.org.lidalia.scalalang.ByteSeq
import uk.org.lidalia.net2.Uri

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

class Request[A, +C] private(
  override val header: RequestHeader,
  val unmarshaller: EntityUnmarshaller[A],
  entity: Entity[C]
) extends Message[C](header, entity) {

  def withMethod(method: Method) = new Request(RequestHeader(method, requestUri, header.headerFields), unmarshaller, entity)

  def withUri(newUri: RequestUri) = new Request(RequestHeader(method, newUri, header.headerFields), unmarshaller, entity)

  val method = header.method

  val requestUri = header.requestUri

  def referer: ?[Uri] = header.referer
}
