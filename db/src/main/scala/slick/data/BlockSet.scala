package slick.data

import sdk.primitive.ByteArray

final case class BlockSet(
  id: Long,           // primary key
  hash: ByteArray,    // strong hash of a hashes of blocks in a set (global identifier)
  blockIds: Set[Long],// pointers to blocks
) {
  override def equals(obj: Any): Boolean = obj match {
    case that: BlockSet => this.id == that.id
    case _              => false
  }

  override def hashCode(): Int = id.hashCode()
}
