package node.api.http.routes

import cats.effect.Sync
import cats.syntax.all.*
import node.api.http.ApiPath
import org.http4s.{EntityEncoder, HttpRoutes}

import scala.util.Try

object HttpGet {
  def apply[F[_]: Sync, A](
    methodName: String,
    getApi: F[Option[A]],
  )(implicit eeC: EntityEncoder[F, A]): HttpRoutes[F] = {
    val dsl = org.http4s.dsl.Http4sDsl[F]
    import dsl.*
    HttpRoutes.of[F] { case GET -> ApiPath / s"$methodName" =>
      getApi.flatMap(_.map(Ok(_)).getOrElse(NotFound()))
    }
  }

  def blocks[F[_]: Sync, A, B](
    getApi: A => F[Option[B]],
  )(implicit decA: String => Try[A], eeC: EntityEncoder[F, B]): HttpRoutes[F] = {
    val dsl = org.http4s.dsl.Http4sDsl[F]
    import dsl.*
    HttpRoutes.of[F] { case GET -> ApiPath / "block" / a =>
      decA(a)
        .map(a => getApi(a).flatMap(_.map(Ok(_)).getOrElse(NotFound())))
        .getOrElse(BadRequest())
    }
  }

  def transactions[F[_]: Sync, A, B](
    getApi: A => F[Option[B]],
  )(implicit decA: String => Try[A], eeC: EntityEncoder[F, B]): HttpRoutes[F] = {
    val dsl = org.http4s.dsl.Http4sDsl[F]
    import dsl.*
    HttpRoutes.of[F] { case GET -> ApiPath / "deploy" / a =>
      decA(a)
        .map(a => getApi(a).flatMap(_.map(Ok(_)).getOrElse(NotFound())))
        .getOrElse(BadRequest())
    }
  }

  def apply[F[_]: Sync, A, B, C](
    methodName: String,
    getApi: (A, B) => F[Option[C]],
  )(implicit decA: String => Try[A], decB: String => Try[B], eeC: EntityEncoder[F, C]): HttpRoutes[F] = {
    val dsl = org.http4s.dsl.Http4sDsl[F]
    import dsl.*
    HttpRoutes.of[F] { case GET -> ApiPath / s"$methodName" / a / b =>
      (decA(a), decB(b))
        .mapN { case (a, b) => getApi(a, b).flatMap(_.map(Ok(_)).getOrElse(NotFound())) }
        .getOrElse(BadRequest())
    }
  }
}
