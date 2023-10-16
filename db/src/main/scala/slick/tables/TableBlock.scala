package slick.tables

import slick.data.Block
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.ProvenShape

class TableBlock(tag: Tag) extends Table[Block](tag, "block") {
  def id: Rep[Long]          = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def version: Rep[Int]      = column[Int]("version")
  def hash: Rep[Array[Byte]] = column[Array[Byte]]("hash", O.Unique)

  def sigAlg: Rep[String]         = column[String]("sig_alg")
  def signature: Rep[Array[Byte]] = column[Array[Byte]]("signature")

  def finalStateHash: Rep[Array[Byte]] = column[Array[Byte]]("final_state_hash")
  def postStateHash: Rep[Array[Byte]]  = column[Array[Byte]]("post_state_hash")

  def validatorId: Rep[Long]                = column[Long]("validator_id")
  def shardId: Rep[Long]                    = column[Long]("shard_id")
  def justificationSetId: Rep[Option[Long]] = column[Option[Long]]("justification_set_id")
  def seqNum: Rep[Long]                     = column[Long]("seq_num")
  def offencesSet: Rep[Option[Long]]        = column[Option[Long]]("offences_set")

  def bondsMapId: Rep[Long]              = column[Long]("bonds_map_id")
  def finalFringe: Rep[Option[Long]]     = column[Option[Long]]("final_fringe")
  def deploySetId: Rep[Option[Long]]     = column[Option[Long]]("deploy_set_id")
  def mergeSetId: Rep[Option[Long]]      = column[Option[Long]]("merge_set_id")
  def dropSetId: Rep[Option[Long]]       = column[Option[Long]]("drop_set_id")
  def mergeSetFinalId: Rep[Option[Long]] = column[Option[Long]]("merge_set_final_id")
  def dropSetFinalId: Rep[Option[Long]]  = column[Option[Long]]("drop_set_final_id")

  def fk1 = foreignKey("fk_block_validator_id", validatorId, slick.qValidator)(_.id)
  def fk2 = foreignKey("fk_block_shard_id", shardId, slick.qShard)(_.id)
  def fk3 = foreignKey("fk_block_justification_set_id", justificationSetId, slick.qBlockSet)(_.id.?)
  def fk4 = foreignKey("fk_block_offences_set_id", offencesSet, slick.qBlockSet)(_.id.?)

  def fk5  = foreignKey("fk_block_bonds_map_id", bondsMapId, slick.qBondSet)(_.id)
  def fk6  = foreignKey("fk_block_final_fringe_id", finalFringe, slick.qBlockSet)(_.id.?)
  def fk7  = foreignKey("fk_block_deploy_set_id", deploySetId, slick.qDeploySet)(_.id.?)
  def fk8  = foreignKey("fk_block_merge_set_id", mergeSetId, slick.qDeploySet)(_.id.?)
  def fk9  = foreignKey("fk_block_drop_set_id", dropSetId, slick.qDeploySet)(_.id.?)
  def fk10 = foreignKey("fk_block_merge_set_final_id", mergeSetFinalId, slick.qDeploySet)(_.id.?)
  def fk11 = foreignKey("fk_block_drop_set_final_id", dropSetFinalId, slick.qDeploySet)(_.id.?)

  def idx = index("idx_block", hash, unique = true)

  def * : ProvenShape[Block] = (
    id,
    version,
    hash,
    sigAlg,
    signature,
    finalStateHash,
    postStateHash,
    validatorId,
    shardId,
    justificationSetId,
    seqNum,
    offencesSet,
    bondsMapId,
    finalFringe,
    deploySetId,
    mergeSetId,
    dropSetId,
    mergeSetFinalId,
    dropSetFinalId,
  ).mapTo[Block]
}
