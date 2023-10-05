package slick.tables

import sdk.primitive.ByteArray
import slick.data.DeploySet
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.ProvenShape

class TableDeploySet(tag: Tag) extends Table[DeploySet](tag, "DeploySet") {
  def id: Rep[Long]          = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def hash: Rep[Array[Byte]] = column[Array[Byte]]("hash", O.Unique)

  private def into(pair: (Long, Array[Byte])): DeploySet       = DeploySet(pair._1, ByteArray(pair._2))
  private def from(ds: DeploySet): Option[(Long, Array[Byte])] = Some((ds.id, ds.hash.bytes))

  def * : ProvenShape[DeploySet] = (id, hash).<>(into, from)
}
