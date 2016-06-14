package uk.org.lidalia.http.core.headerfields

import uk.org.lidalia.http.core.HeaderField

object UserAgent extends StringHeaderFieldName {

  override def name = "User-Agent"

  override def apply(userAgent: String) = new UserAgent(userAgent)
}

class UserAgent private (val userAgent: String) extends HeaderField(UserAgent.name, List(userAgent))
