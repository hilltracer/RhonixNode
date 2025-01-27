package squeryl

import sdk.CustomTypeMode.*
import sdk.RhonixNodeDb.{blockDeploysTable, deployTable}

/**
 * Collection of DB queries using `squeryl` library.
 */
object DbQuery {
  def getBlockById(blockId: Long) =
    blockDeploysTable.where(_.blockId === blockId)

  def getDeployById(id: Long) =
    deployTable.where(_.id === id)

  def getDeploysByBlockId(id: Long) =
    from(deployTable, blockDeploysTable)((deploy, db) =>
      where(deploy.id === db.deployId and db.blockId === id)
      select deploy,
    )

}
