package uk.org.lidalia.http.core.headerfields

import uk.org.lidalia.http.core.HeaderField
import uk.org.lidalia.net.HostAndPort

import scala.util.Try

object Host extends SingleValueHeaderFieldName[HostAndPort] {

  def parse(headerFieldValue: String) = Try(HostAndPort(headerFieldValue)).toOption

  def name = "Host"

  def apply(hostAndPort: HostAndPort) = new Host(hostAndPort)
}

class Host private (val hostAndPort: HostAndPort) extends HeaderField(Host.name, List(hostAndPort.toString))
