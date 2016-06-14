package uk.org.lidalia.http.core

import org.joda.time.DateTime
import uk.org.lidalia.net.Url

import scala.collection.immutable

object Response {

  def apply[T](
    status: Code,
    headerFields: List[HeaderField],
    body: Entity[T]
  ): Response[T] = {
    new Response(ResponseHeader(status, headerFields), body)
  }

  def apply[T](
    header: ResponseHeader,
    body: Entity[T]
  ): Response[T] = {
    new Response(header, body)
  }

  def apply(
    header: ResponseHeader,
    body: String
  ): Response[String] = {
    new Response(header, new AnyEntity(body))
  }

  def apply(
    status: Code,
    headerFields: List[HeaderField]
  ): Response[None.type] = {
    new Response(ResponseHeader(status, headerFields), EmptyEntity)
  }

  def apply(
    code: Int,
    headerFields: HeaderField*
  )(
    entity: String
  ): Response[String] = {
    new Response[String](
      ResponseHeader(
        Code(code),
        immutable.Seq(headerFields:_*)
      ),
      new AnyEntity(entity)
    )
  }

  def apply(
    status: Code,
    headerFields: HeaderField*
  )(
    entity: String
  ): Response[String] = {
    new Response[String](
      ResponseHeader(
        status,
        immutable.Seq(headerFields:_*)
      ),
      new AnyEntity(entity)
    )
  }

//  def apply(
//    code: Int,
//    headerFields: (String, String)*
//  )(entity: String): Response[String] = {
//    apply(
//      code,
//      headerFields.map(header => HeaderField(header._1, header._2)):_*
//    )(entity)
//  }
}

class Response[+T] private(override val header: ResponseHeader, entity: Entity[T]) extends Message(header, entity) {

  val code = header.code

  def requiresRedirect: Boolean = header.requiresRedirect

  def location: ?[Url] = header.location

  def date: ?[DateTime] = header.date

  def isNotError: Boolean = header.isNotError
  def isInformational: Boolean = header.isInformational
  def isSuccessful: Boolean = header.isSuccessful
  def isRedirection: Boolean = header.isRedirection
  def isClientError: Boolean = header.isClientError
  def isServerError: Boolean = header.isServerError
  def isError: Boolean = header.isError
}
