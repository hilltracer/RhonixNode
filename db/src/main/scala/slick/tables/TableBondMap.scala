package slick.tables

import sdk.primitive.ByteArray
import slick.data.BondMap
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.ProvenShape

class TableBondMap(tag: Tag) extends Table[BondMap](tag, "BondMap") {
  def id: Rep[Long]          = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def hash: Rep[Array[Byte]] = column[Array[Byte]]("hash", O.Unique)

  private def into(pair: (Long, Array[Byte])): BondMap       = BondMap(pair._1, ByteArray(pair._2))
  private def from(ds: BondMap): Option[(Long, Array[Byte])] = Some((ds.id, ds.hash.bytes))

  def * : ProvenShape[BondMap] = (id, hash).<>(into, from)
}
