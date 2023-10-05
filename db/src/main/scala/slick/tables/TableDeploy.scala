package slick.tables

import sdk.primitive.ByteArray
import slick.data.Deploy
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.ProvenShape

class TableDeploy(tag: Tag) extends Table[Deploy](tag, "Deploy") {
  def id: Rep[Long]         = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def sig: Rep[Array[Byte]] = column[Array[Byte]]("sig", O.Unique)
  def deployerId: Rep[Long] = column[Long]("deployerId")
  def shardId: Rep[Long]    = column[Long]("shardId")
  def program: Rep[String]  = column[String]("program")
  def phloPrice: Rep[Long]  = column[Long]("phloPrice")
  def phloLimit: Rep[Long]  = column[Long]("phloLimit")
  def nonce: Rep[Long]      = column[Long]("nonce")

  foreignKey("fk_deployer_id", deployerId, slick.qDeployer)(_.id)
  foreignKey("fk_shard_id", shardId, slick.qShard)(_.id)

  def idx = index("idx", sig, unique = true)

  private def into(tuple: (Long, Array[Byte], Long, Long, String, Long, Long, Long)): Deploy     =
    Deploy(tuple._1, ByteArray(tuple._2), tuple._3, tuple._4, tuple._5, tuple._6, tuple._7, tuple._8)
  private def from(d: Deploy): Option[(Long, Array[Byte], Long, Long, String, Long, Long, Long)] =
    Some((d.id, d.sig.bytes, d.deployerId, d.shardId, d.program, d.phloPrice, d.phloLimit, d.nonce))

  def * : ProvenShape[Deploy] = (id, sig, deployerId, shardId, program, phloPrice, phloLimit, nonce).<>(into, from)
}
