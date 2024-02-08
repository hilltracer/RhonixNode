package coop.rchain.rholang.normalizer2

import cats.effect.Sync
import cats.syntax.all.*
import coop.rchain.rholang.interpreter.compiler.SourcePosition
import coop.rchain.rholang.interpreter.errors.{InterpreterError, UnexpectedBundleContent}
import coop.rchain.rholang.normalizer2.env.NestingWriter
import coop.rchain.rholang.syntax.*
import io.rhonix.rholang.*
import io.rhonix.rholang.ast.rholang.Absyn.*

object BundleNormalizer {
  def normalizeBundle[F[_]: Sync: NormalizerRec: NestingWriter](p: PBundle): F[BundleN] = {
    def connectivesExistOnTop(p: ParN): F[Boolean] =
      p match {
        case _: ConnectiveN  => true.pure
        case _               => false.pure
        case pProc: ParProcN => pProc.ps.existsM(connectivesExistOnTop)
      }

    def raiseError: F[Unit] = UnexpectedBundleContent(
      s"Illegal top level connective in bundle at: ${SourcePosition(p.line_num, p.col_num)}.",
    ).raiseError

    for {
      // Inside bundle target it is prohibited to have free variables and wildcards.
      target <- NormalizerRec[F].normalize(p.proc_).withinBundle()
      // Inside bundle target it is prohibited to have connectives on top level.
      _      <- connectivesExistOnTop(target).ifM(raiseError, Sync[F].unit)

      outermostBundle = p.bundle_ match {
                          case _: BundleReadWrite => BundleN(target, writeFlag = true, readFlag = true)
                          case _: BundleRead      => BundleN(target, writeFlag = false, readFlag = true)
                          case _: BundleWrite     => BundleN(target, writeFlag = true, readFlag = false)
                          case _: BundleEquiv     => BundleN(target, writeFlag = false, readFlag = false)
                        }

    } yield target match {
      case b: BundleN => outermostBundle.merge(b)
      case _          => outermostBundle
    }
  }
}
