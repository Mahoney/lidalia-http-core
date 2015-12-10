package uk.org.lidalia.http.core

import scala.language.higherKinds

trait Http[+Result[_]] {

  def execute[A, C](request: Request[A, C]): Result[A]

}
