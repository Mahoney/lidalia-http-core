package uk.org.lidalia.http.core

import uk.org.lidalia
import lidalia.http.core.headerfields.Referer
import lidalia.net.Uri

import scala.collection.immutable

object RequestHeader {
  def apply(
    method: Method,
    uri: RequestUri,
    headerFields: immutable.Seq[HeaderField]
  ) = new RequestHeader(method, uri, headerFields)
}

class RequestHeader private(
  @Identity val method: Method,
  @Identity val requestUri: RequestUri,
  private val reqHeaderFieldsList: immutable.Seq[HeaderField]
) extends MessageHeader(reqHeaderFieldsList) {

  lazy val referer: ?[Uri] = headerField(Referer)

  override def toString = s"$method $requestUri HTTP/1.1\r\n${super.toString}"
}
