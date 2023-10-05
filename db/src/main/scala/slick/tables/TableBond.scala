package slick.tables

import slick.data.Bond
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.ProvenShape

class TableBond(tag: Tag) extends Table[Bond](tag, "Bond") {
  def id: Rep[Long]          = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def validatorId: Rep[Long] = column[Long]("validatorId", O.Unique)
  def stake: Rep[Long]       = column[Long]("stake")

  def idx = index("idx", validatorId, unique = true)

  def * : ProvenShape[Bond] = (id, validatorId, stake).mapTo[Bond]
}
