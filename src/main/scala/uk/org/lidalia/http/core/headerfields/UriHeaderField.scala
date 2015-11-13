package uk.org.lidalia.http.core.headerfields

import uk.org.lidalia
import lidalia.http.core.HeaderField
import uk.org.lidalia.net.Url

import scala.util.Try

abstract class UrlHeaderFieldName extends SingleValueHeaderFieldName[Url] {

  def parse(headerFieldValue: String) = Try(Url(headerFieldValue)).toOption

}

abstract class UriHeaderField protected(name: String, val url: Url) extends HeaderField(name, List(url.toString))
