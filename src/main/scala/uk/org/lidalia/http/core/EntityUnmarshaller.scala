package uk.org.lidalia.http.core

import java.io.InputStream

import org.apache.commons.io.IOUtils
import uk.org.lidalia.scalalang.ByteSeq

object NoopEntityUnmarshaller extends EntityUnmarshaller[None.type] {
  override def unmarshal(request: Request[_, _], response: ResponseHeader, entityBytes: InputStream) = EmptyEntity
}

object BytesUnmarshaller extends EntityUnmarshaller[ByteSeq] {
  override def unmarshal(request: Request[_, _], response: ResponseHeader, entityBytes: InputStream): Entity[ByteSeq] = {
    new ByteEntity(ByteSeq(IOUtils.toByteArray(entityBytes)))
  }
}

trait EntityUnmarshaller[+T] {

  def unmarshal(request: Request[_, _], response: ResponseHeader, entityBytes: InputStream): Entity[T]

}
