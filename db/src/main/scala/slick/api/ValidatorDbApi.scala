package slick.api

import cats.effect.Async
import sdk.primitive.ByteArray
import slick.data.Validator
import slick.jdbc.JdbcProfile
import slick.{QueryValidator, SlickDb}
import slick.syntax.all.*

class ValidatorDbApi[F[_]: Async: SlickDb] {
  implicit val p: JdbcProfile = SlickDb[F].profile
  val queries: QueryValidator = QueryValidator()

  def insert(pubKey: ByteArray): F[Long] = queries.insert(pubKey).run

  def update(validator: Validator): F[Int] = queries.update(validator).run

  def getById(id: Long): F[Option[Validator]] = queries.getById(id).run

  def getByPublicKey(pubKey: ByteArray): F[Option[Validator]] = queries.getByPublicKey(pubKey).run
}
