package slick.tables

import slick.jdbc.PostgresProfile.api.*

class TableBondMapBind(tag: Tag) extends Table[(Long, Long)](tag, "BondMapBind") {
  def bondId: Rep[Long]    = column[Long]("bondId")
  def bondMapId: Rep[Long] = column[Long]("bondMapId")

  def pk = primaryKey("bond_Map_bind_pk", (bondId, bondMapId))

  foreignKey("fk_bond_id", bondId, slick.qBond)(_.id)
  foreignKey("fk_bond_Map_id", bondMapId, slick.qBondMap)(_.id)

  def idx = index("idx", bondMapId, unique = false)

  def * = (bondId, bondMapId)
}
