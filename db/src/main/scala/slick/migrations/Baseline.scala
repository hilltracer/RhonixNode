package slick.migrations

import slick.migration.api.{Dialect, ReversibleMigrationSeq, TableMigration}
import slick.qValidator

// Initial version of the database
object Baseline {
  def apply()(implicit dialect: Dialect[?]): ReversibleMigrationSeq = {
    val validatorTable = TableMigration(qValidator).create
      .addColumns(_.id, _.pubKey)
      .addIndexes(_.idx)

    val bondTable = TableMigration(slick.qBond).create
      .addColumns(_.id, _.validatorId, _.stake)

    validatorTable & bondTable
  }
}
