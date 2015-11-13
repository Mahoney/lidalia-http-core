package uk.org.lidalia.http.core.headerfields

import uk.org.lidalia.net.Url

object Location extends UrlHeaderFieldName {

  def apply(url: Url) = new Location(url)

  def name: String = "Location"

}

class Location private (url: Url) extends UriHeaderField(Location.name, url)
