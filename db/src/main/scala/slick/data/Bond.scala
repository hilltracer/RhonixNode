package slick.data

final case class Bond(
  id: Long,          // primary key
  validatorId: Long, // pointer to a validator
  stake: Long,       // stake of a validator
) {
  override def equals(obj: Any): Boolean = obj match {
    case that: Bond => this.id == that.id
    case _          => false
  }

  override def hashCode(): Int = id.hashCode()
}
