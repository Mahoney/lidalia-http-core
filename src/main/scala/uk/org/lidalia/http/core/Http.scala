package uk.org.lidalia.http.core

trait Http[+Result[_]] {

  def execute[A, C](request: Request[A, C]): Result[A]

}
