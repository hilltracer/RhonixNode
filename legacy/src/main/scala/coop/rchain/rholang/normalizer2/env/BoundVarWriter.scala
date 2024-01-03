package coop.rchain.rholang.normalizer2.env

import cats.effect.Sync
import coop.rchain.rholang.interpreter.compiler.{FreeContext, IdContext}

trait BoundVarWriter[T] {
  // Bound variables operations

  /** Inserts new variables in bound map  */
  def putBoundVars(bindings: Seq[IdContext[T]]): Seq[Int]

  // Scope operations

  /** Runs functions in an empty bound variables scope (preserving history) */
  def withNewBoundVarScope[F[_]: Sync, R](scopeFn: () => F[R]): F[R]

  /** Runs functions in an copy of this bound variables scope (preserving history) */
  def withCopyBoundVarScope[F[_]: Sync, R](scopeFn: () => F[R]): F[R]
}

object BoundVarWriter {
  def apply[T](implicit instance: BoundVarWriter[T]): BoundVarWriter[T] = instance
}
