package uk.org.lidalia.http.core

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.charset.StandardCharsets.UTF_8

import org.apache.commons.io.IOUtils
import uk.org.lidalia.http.core.MediaType.`application/octet-stream`
import uk.org.lidalia.scalalang.ByteSeq

trait Entity[+T] {

  val entity: T

  def marshall(as: MediaType): InputStream

  def toString(as: ?[MediaType]) = {
    val contentType = as.getOrElse(`application/octet-stream`)
    IOUtils.toString(
      marshall(contentType),
      contentType.charset.getOrElse(UTF_8)
    )
  }

  def bytes(as: ?[MediaType] = None): ByteSeq = {
    val contentType = as.getOrElse(`application/octet-stream`)
    ByteSeq(marshall(contentType))
  }

  override def toString = {
    toString(None)
  }
}

class AnyEntity[+T](val entity: T) extends Entity[T] {

  override def marshall(as: MediaType): InputStream = new ByteArrayInputStream(
    entity.toString.getBytes(
      as.charset.getOrElse(UTF_8)
    )
  )

  override def toString(as: ?[MediaType]) = entity.toString
}

class StringEntity(entity: String) extends AnyEntity[String](entity)

class ByteEntity(override val entity: ByteSeq) extends Entity[ByteSeq] {

  override def marshall(as: MediaType): InputStream = entity.toInputStream
}

object EmptyEntity extends Entity[None.type ] {
  val entity = None
  override def marshall(as: MediaType): InputStream = new ByteArrayInputStream(Array())
}

class EitherEntity[+A, +B](
  val eitherEntity: Either[Entity[A], Entity[B]]
) extends Entity[Either[A, B]] {

  override def marshall(as: MediaType): InputStream = {
    eitherEntity.fold(_.marshall(as), _.marshall(as))
  }

  override val entity: Either[A, B] = {
    eitherEntity.fold(_.entity, _.entity)
  }
}
