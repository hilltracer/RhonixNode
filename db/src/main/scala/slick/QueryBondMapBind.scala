package slick

import slick.jdbc.JdbcProfile

final case class QueryBondMapBind()(implicit val profile: JdbcProfile) {
  import profile.api.*

  def getBondIds(bondMapId: Long) =
    qBondMapBind.filter(_.bondMapId === bondMapId).map(_.bondId).result
}
