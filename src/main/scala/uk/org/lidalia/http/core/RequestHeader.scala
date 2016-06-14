package uk.org.lidalia.http.core

import uk.org.lidalia
import lidalia.http.core.headerfields.{Host, Referer, UserAgent}
import lidalia.net.{HostAndPort, Uri}

import scala.collection.immutable
import scala.collection.immutable.Seq

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

  def withMethod(newMethod: Method) = RequestHeader(newMethod, requestUri, reqHeaderFieldsList)

  def withUri(newUri: RequestUri) = RequestHeader(method, newUri, reqHeaderFieldsList)

  def withHeaderField(headerField: HeaderField): RequestHeader = {
    val newHeaderFields: Seq[HeaderField] = headerField +: reqHeaderFieldsList.filterNot(_.isA(Host))
    new RequestHeader(method, requestUri, newHeaderFields)
  }

  lazy val referer: ?[Uri] = headerField(Referer)

  lazy val host: ?[HostAndPort] = headerField(Host)

  lazy val userAgent: ?[String] = headerField(UserAgent)

  override def toString = s"$method $requestUri HTTP/1.1\r\n${super.toString}"
}
