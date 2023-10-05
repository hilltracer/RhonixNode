package slick.data

import cats.effect.Async
import sdk.primitive.ByteArray
import slick.jdbc.JdbcProfile
import slick.syntax.all.*
import slick.{QueryDeploySetBind, SlickDb}

final case class DeploySet(
  id: Long,       // primary key
  hash: ByteArray,// strong hash of a signatures of deploys in a set (global identifier)
) {
  // pointers to deploys
  def deploys[F[_]: Async: SlickDb]: F[Seq[Long]] = {
    implicit val p: JdbcProfile = SlickDb[F].profile
    QueryDeploySetBind().getDeployIds(this.id).run
  }

  override def equals(obj: Any): Boolean = obj match {
    case that: DeploySet => this.id == that.id
    case _               => false
  }

  override def hashCode(): Int = id.hashCode()
}
