package uk.org.lidalia.http.core

import java.util.concurrent.ConcurrentHashMap

import scala.collection.convert.Wrappers.JConcurrentMapWrapper
import scala.collection.immutable.Set

object Method {

  private val methods = new JConcurrentMapWrapper(new ConcurrentHashMap[String, Method])

  val GET     = registerSafeMethod(            "GET",     requestMayHaveEntity = false, responseMayHaveEntity = true )
  val HEAD    = registerSafeMethod(            "HEAD",    requestMayHaveEntity = false, responseMayHaveEntity = false)

  val PUT     = registerUnsafeIdempotentMethod("PUT",     requestMayHaveEntity = true,  responseMayHaveEntity = true )
  val DELETE  = registerUnsafeIdempotentMethod("DELETE",  requestMayHaveEntity = false, responseMayHaveEntity = true )

  val POST    = registerNonIdempotentMethod(   "POST",    requestMayHaveEntity = true,  responseMayHaveEntity = true )
  val PATCH   = registerNonIdempotentMethod(   "PATCH",   requestMayHaveEntity = true,  responseMayHaveEntity = true )

  val OPTIONS = registerSafeMethod(            "OPTIONS", requestMayHaveEntity = true,  responseMayHaveEntity = true )
  val TRACE   = registerSafeMethod(            "TRACE",   requestMayHaveEntity = false, responseMayHaveEntity = true )

  def registerSafeMethod(name: String, requestMayHaveEntity: Boolean, responseMayHaveEntity: Boolean): SafeMethod = {
    register(new SafeMethod(name, requestMayHaveEntity, responseMayHaveEntity))
  }

  def registerUnsafeIdempotentMethod(name: String, requestMayHaveEntity: Boolean, responseMayHaveEntity: Boolean): UnsafeIdempotentMethod = {
    register(new UnsafeIdempotentMethod(name, requestMayHaveEntity, responseMayHaveEntity))
  }

  def registerNonIdempotentMethod(name: String, requestMayHaveEntity: Boolean, responseMayHaveEntity: Boolean): NonIdempotentMethod = {
    register(new NonIdempotentMethod(name, requestMayHaveEntity, responseMayHaveEntity))
  }

  def register[T <: Method](method: T): T = {
    val existing: ?[Method] = methods.putIfAbsent(method.name, method)
    if (existing.isDefined) throw new IllegalStateException("Only one instance of a method may exist! Trying to create duplicate of "+method)
    method
  }

  def values(): Set[Method] = methods.values.to[Set]

  def apply(name: String): Method = {
    methods.get(name).or(new NonIdempotentMethod(name, true, true))
  }
}

sealed abstract class Method(
  val name: String,
  val requestMayHaveEntity: Boolean,
  val responseMayHaveEntity: Boolean
) {

  def isSafe: Boolean
  final def isUnsafe: Boolean = !isSafe

  def isIdempotent: Boolean
  def isNotIdempotent: Boolean = !isIdempotent

  final def requestMayNotHaveEntity: Boolean = !requestMayHaveEntity

  final def responseMayNotHaveEntity: Boolean = !responseMayHaveEntity

  override val toString = name
}

sealed trait IdempotentMethod extends Method {
  final override def isIdempotent = true
}

sealed trait UnsafeMethod extends Method {
  final override def isSafe = false
}

final class NonIdempotentMethod private[core](
  name: String,
  requestMayHaveEntity: Boolean,
  responseMayHaveEntity: Boolean
) extends Method(
  name,
  requestMayHaveEntity,
  responseMayHaveEntity
) with UnsafeMethod {
  override def isIdempotent = false
}

final class SafeMethod private[core](
  name: String,
  requestMayHaveEntity: Boolean,
  responseMayHaveEntity: Boolean
) extends Method(
  name,
  requestMayHaveEntity,
  responseMayHaveEntity
) with IdempotentMethod {
  override def isSafe = true
}

final class UnsafeIdempotentMethod private[core](
  name: String,
  requestMayHaveEntity: Boolean,
  responseMayHaveEntity: Boolean
) extends Method(
  name,
  requestMayHaveEntity,
  responseMayHaveEntity
) with UnsafeMethod with IdempotentMethod
