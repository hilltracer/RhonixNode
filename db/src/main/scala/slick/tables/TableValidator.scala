package slick.tables

import slick.data.Validator
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.ProvenShape

class TableValidator(tag: Tag) extends Table[Validator](tag, "validator") {
  def id: Rep[Long]            = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def pubKey: Rep[Array[Byte]] = column[Array[Byte]]("pub_key", O.Unique)

  def idx = index("idx_validator", pubKey, unique = true)

  def * : ProvenShape[Validator] = (id, pubKey).mapTo[Validator]
}
