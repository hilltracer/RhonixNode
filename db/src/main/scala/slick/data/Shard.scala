package slick.data

final case class Shard(
  id: Long,    // primary key
  name: String,// shard name
) {
  override def equals(obj: Any): Boolean = obj match {
    case that: Shard => this.id == that.id
    case _           => false
  }

  override def hashCode(): Int = id.hashCode()
}
