package coop.rchain.rholang.normalizer2

import cats.effect.Sync
import cats.syntax.all.*
import coop.rchain.rholang.interpreter.errors.UnrecognizedNormalizerError
import coop.rchain.rholang.normalizer2.env.*
import coop.rchain.rholang.syntax.*
import io.rhonix.rholang.ast.rholang.Absyn.{Case, CaseImpl, PMatch}
import io.rhonix.rholang.{MatchCaseN, MatchN}

import scala.jdk.CollectionConverters.*

object MatchNormalizer {
  def normalizeMatch[
    F[_]: Sync: NormalizerRec: BoundVarScope: FreeVarScope: NestingInfoWriter,
    T: BoundVarWriter: FreeVarReader,
  ](p: PMatch): F[MatchN] = {
    def normalizeCase(c: Case): F[MatchCaseN] = c match {
      case ci: CaseImpl =>
        val (pattern, caseBody) = (ci.proc_1, ci.proc_2)

        for {
          // Normalize pattern in a fresh bound and free variables scope
          patternTuple <- NormalizerRec[F].normalize(pattern).withinPatternGetFreeVars()

          (patternResult, freeVars) = patternTuple

          // Normalize body in the copy of bound scope with added free variables as bounded
          caseBodyResult <- NormalizerRec[F].normalize(caseBody).withAbsorbedFreeVars(freeVars)

        } yield MatchCaseN(patternResult, caseBodyResult, freeVars.length)

      case c => UnrecognizedNormalizerError(s"Unexpected Case implementation `${c.getClass}`.").raiseError
    }

    (NormalizerRec[F].normalize(p.proc_), p.listcase_.asScala.toVector.traverse(normalizeCase)).mapN(MatchN.apply)
  }
}
