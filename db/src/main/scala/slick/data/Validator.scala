package slick.data

import sdk.primitive.ByteArray

final case class Validator(
  id: Long,         // primary key
  pubKey: ByteArray,// validator public key
) {
  override def equals(obj: Any): Boolean = obj match {
    case that: Validator => this.id == that.id
    case _               => false
  }

  override def hashCode(): Int = id.hashCode()
}
