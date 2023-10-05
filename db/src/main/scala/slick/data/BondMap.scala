package slick.data

import cats.effect.Async
import sdk.primitive.ByteArray
import slick.jdbc.JdbcProfile
import slick.syntax.all.*
import slick.{QueryBondMapBind, SlickDb}

final case class BondMap(
  id: Long,       // primary key
  hash: ByteArray,// strong hash of a bonds map content (global identifier)
) {
  // pointers to bonds
  def bonds[F[_]: Async: SlickDb]: F[Seq[Long]] = {
    implicit val p: JdbcProfile = SlickDb[F].profile
    QueryBondMapBind().getBondIds(this.id).run
  }

  override def equals(obj: Any): Boolean = obj match {
    case that: BondMap => this.id == that.id
    case _             => false
  }

  override def hashCode(): Int = id.hashCode()
}
