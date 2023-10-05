package slick

import slick.jdbc.JdbcProfile

final case class QueryDeploySetBind()(implicit val profile: JdbcProfile) {
  import profile.api.*

  def getDeployIds(deploySetId: Long) =
    qDeploySetBind.filter(_.deploySetId === deploySetId).map(_.deployId).result
}
