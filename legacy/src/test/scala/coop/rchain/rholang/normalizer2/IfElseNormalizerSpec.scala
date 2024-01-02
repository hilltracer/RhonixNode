package coop.rchain.rholang.normalizer2

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import coop.rchain.rholang.interpreter.compiler.VarSort
import coop.rchain.rholang.normalizer2.util.Mock.*
import coop.rchain.rholang.normalizer2.util.MockNormalizerRec.mockADT
import io.rhonix.rholang.ast.rholang.Absyn.*
import io.rhonix.rholang.{GBoolN, MatchCaseN, MatchN}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class IfElseNormalizerSpec extends AnyFlatSpec with ScalaCheckPropertyChecks with Matchers {

  behavior of "IfElse normalizer"

  "IfElse normalizer" should "convert AST term to match ADT term" in {
    forAll { (targetStr: String, trueCaseStr: String, falseCaseStr: String) =>
      val targetTerm = new PVar(new ProcVarVar(targetStr))
      val trueCase   = new PGround(new GroundString(trueCaseStr))
      val falseCase  = new PGround(new GroundString(falseCaseStr))
      val inputTerm  = new PIfElse(targetTerm, trueCase, falseCase)

      implicit val (mockRec, _, _, _, _) = createMockDSL[IO, VarSort]()

      val adt = IfElseNormalizer.normalizeIfElse[IO](inputTerm).unsafeRunSync()

      val expectedAdt = MatchN(
        target = mockADT(targetTerm: Proc),
        cases = Seq(
          MatchCaseN(pattern = GBoolN(true), source = mockADT(trueCase: Proc)),
          MatchCaseN(pattern = GBoolN(false), source = mockADT(falseCase: Proc)),
        ),
      )

      adt shouldBe expectedAdt

      val terms = mockRec.extractData

      val expectedTerms = Seq(
        TermData(ProcTerm(targetTerm)),
        TermData(ProcTerm(trueCase)),
        TermData(ProcTerm(falseCase)),
      )
      terms shouldBe expectedTerms
    }
  }
}
