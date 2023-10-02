package slick.data

import sdk.primitive.ByteArray

final case class DeploySet(
  id: Long,            // primary key
  hash: ByteArray,     // strong hash of a signatures of deploys in a set (global identifier)
  deployIds: Set[Long],// pointers to deploys
) {
  override def equals(obj: Any): Boolean = obj match {
    case that: DeploySet => this.id == that.id
    case _               => false
  }

  override def hashCode(): Int = id.hashCode()
}
