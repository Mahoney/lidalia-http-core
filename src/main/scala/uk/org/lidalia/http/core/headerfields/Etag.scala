package uk.org.lidalia.http.core.headerfields

import uk.org.lidalia.http.core.HeaderField

object Etag extends StringHeaderFieldName {

  override def name = "Etag"

  override def apply(etag: String) = new Etag(etag)
}

class Etag private (val etag: String) extends HeaderField(Etag.name, List(etag))
