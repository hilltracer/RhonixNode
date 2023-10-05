package slick.tables

import slick.jdbc.PostgresProfile.api.*

class TableDeploySetBind(tag: Tag) extends Table[(Long, Long)](tag, "DeploySetBind") {
  def deployId: Rep[Long]    = column[Long]("deployId")
  def deploySetId: Rep[Long] = column[Long]("deploySetId")

  def pk = primaryKey("deploy_set_bind_pk", (deployId, deploySetId))

  foreignKey("fk_deploy_id", deployId, slick.qDeploy)(_.id)
  foreignKey("fk_deploy_set_id", deploySetId, slick.qDeploySet)(_.id)

  def idx = index("idx", deploySetId, unique = false)

  def * = (deployId, deploySetId)
}
