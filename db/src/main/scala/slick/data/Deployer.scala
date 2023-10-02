package slick.data

import sdk.primitive.ByteArray

final case class Deployer(
  id: Long,         // primary key
  pubKey: ByteArray,// public key of a deployer
) {
  override def equals(obj: Any): Boolean = obj match {
    case that: Deployer => this.id == that.id
    case _              => false
  }

  override def hashCode(): Int = id.hashCode()
}
