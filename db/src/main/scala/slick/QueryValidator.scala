package slick

import sdk.primitive.ByteArray
import slick.data.Validator
import slick.jdbc.JdbcProfile

final case class QueryValidator()(implicit val profile: JdbcProfile) {
  import profile.api.*

  def getById(id: Long) =
    qValidator.filter(_.id === id).result.headOption

  def insert(pubKey: ByteArray) =
    (qValidator.map(_.pubKey) returning qValidator.map(_.id)) += pubKey.bytes

  def update(validator: Validator) = qValidator.update(validator)

  def getByPublicKey(pubKey: ByteArray) =
    qValidator.filter(_.pubKey === pubKey.bytes).result.headOption
}
