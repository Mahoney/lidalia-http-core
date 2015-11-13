package uk.org.lidalia.http.core

import uk.org.lidalia.lang.RichObject
import uk.org.lidalia.net.{PathAndQuery, Uri}

import scala.util.Try

object RequestUri {
  def apply(requestUri: String): RequestUri = {
    apply(Try(Uri(requestUri)).map(Left(_)).getOrElse(Right(PathAndQuery(requestUri))))
  }
  def apply(requestUri: Either[Uri, PathAndQuery]): RequestUri = new RequestUri(requestUri)
}

class RequestUri private(@Identity val requestUri: Either[Uri, PathAndQuery]) extends RichObject {

  override lazy val toString = requestUri.fold(left => left.toString, right => right.toString)
}
