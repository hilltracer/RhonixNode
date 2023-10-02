package slick.data

import sdk.primitive.ByteArray

final case class Block(
  id: Long,        // primary key
  version: Int,    // protocol versions
  hash: ByteArray, // strong hash of block content

  // Signature
  sigAlg: String,       // signature of a hash
  signature: ByteArray, // signing algorithm

  // On Chain state
  finalStateHash: ByteArray, // proof of final state
  postStateHash: ByteArray,  // proof of pre state

  // pointers to inner data
  validatorId: Long,        // pointer to validator
  shardId: Long,            // pointer to shard
  justificationSetId: Long, // pointer to justification set
  seqNum: Long,             // sequence number
  offencesSet: Long,        // pointer to offences set

  // these are optimisations/data to short circuit validation
  bondsMapId: Long,              // pointer to bonds map
  finalFringe: Long,             // pointer to final fringe set
  deploySetId: Long,             // pointer to deploy set in the block
  mergeSetId: Long,              // pointer to deploy set merged into pre state
  dropSetId: Long,               // pointer to deploy set rejected from pre state
  mergeSetFinalId: Option[Long], // pointer to deploy set finally accepted
  dropSetFinalId: Option[Long],  // pointer to deploy set finally rejected
) {
  override def equals(obj: Any): Boolean = obj match {
    case that: Block => this.id == that.id
    case _           => false
  }

  override def hashCode(): Int = id.hashCode()
}
