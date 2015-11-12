package uk.org.lidalia.http.core

import java.io.InputStream

import Method.GET
import uk.org.lidalia.net2.Scheme.HTTP
import uk.org.lidalia.net2.{HostAndPort, Scheme, Url}

object RequestBuilder {

  def request[T](
    method: Method = GET,
    scheme: Scheme = HTTP,
    hostAndPort: HostAndPort = HostAndPort("localhost"),
    uri: RequestUri = RequestUri("/mypath"),
    accept: Accept[T] = new Accept[None.type](List()) {
      override def unmarshal(request: Request[None.type, _], response: ResponseHeader, entityBytes: InputStream) = EmptyEntity
    }
  ): Request[T, None.type] = {
    Request(method, uri, accept, Nil)
  }

  def get(uri: RequestUri = RequestUri("/mypath")) = request(GET, uri = uri)

  def get(url: Url) = request(GET, url.scheme, url.hostAndPort, uri = RequestUri(url.pathAndQuery))
}
