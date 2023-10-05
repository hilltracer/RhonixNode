package slick.tables

import sdk.primitive.ByteArray
import slick.data.Validator
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.ProvenShape

class TableValidator(tag: Tag) extends Table[Validator](tag, "Validator") {
  def id: Rep[Long]            = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def pubKey: Rep[Array[Byte]] = column[Array[Byte]]("publicKey", O.Unique)

  def idx = index("idx", pubKey, unique = true)

  private def into(pair: (Long, Array[Byte])): Validator      = Validator(pair._1, ByteArray(pair._2))
  private def from(v: Validator): Option[(Long, Array[Byte])] = Some((v.id, v.pubKey.bytes))

  def * : ProvenShape[Validator] = (id, pubKey).<>(into, from)
}
