package slick.data

import sdk.primitive.ByteArray

final case class BondSet(
  id: Long,         // primary key
  hash: ByteArray,  // strong hash of a bonds set content (global identifier)
  bonds: List[Long],// pointers to bonds
) {
  override def equals(obj: Any): Boolean = obj match {
    case that: BondSet => this.id == that.id
    case _             => false
  }

  override def hashCode(): Int = id.hashCode()
}
