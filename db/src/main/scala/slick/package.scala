import slick.lifted.TableQuery
import slick.tables.*

package object slick {

  // all queries
  val qBond          = TableQuery[TableBond]
  val qBondMap       = TableQuery[TableBondMap]
  val qBondMapBind   = TableQuery[TableBondMapBind]
  val qDeploy        = TableQuery[TableDeploy]
  val qDeployer      = TableQuery[TableDeployer]
  val qDeploySet     = TableQuery[TableDeploySet]
  val qDeploySetBind = TableQuery[TableDeploySetBind]
  val qShard         = TableQuery[TableShard]
  val qValidator     = TableQuery[TableValidator]
}
