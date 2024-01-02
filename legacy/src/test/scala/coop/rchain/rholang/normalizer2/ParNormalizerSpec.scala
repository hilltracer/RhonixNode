package coop.rchain.rholang.normalizer2

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import coop.rchain.rholang.interpreter.compiler.VarSort
import coop.rchain.rholang.normalizer2.util.Mock.*
import coop.rchain.rholang.normalizer2.util.MockNormalizerRec
import coop.rchain.rholang.normalizer2.util.MockNormalizerRec.mockADT
import io.rhonix.rholang.{ParN, ParProcN}
import io.rhonix.rholang.ast.rholang.Absyn.{GroundString, PGround, PPar, Proc}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class ParNormalizerSpec extends AnyFlatSpec with ScalaCheckPropertyChecks with Matchers {

  it should "covert AST term to ADT term" in {
    forAll { (s1: String, s2: String) =>
      val left  = new PGround(new GroundString(s1))
      val right = new PGround(new GroundString(s2))
      // term1 | term2
      val term  = new PPar(left, right)

      implicit val (mockRec, _, _, _, _) = createMockDSL[IO, VarSort]()

      // Run Par normalizer
      val adt = ParNormalizer.normalizePar[IO](term).unsafeRunSync()

      // Expect right converted ADT term
      val expectedAdt = ParProcN(Seq(mockADT(left: Proc), mockADT(right: Proc)))

      adt shouldBe expectedAdt

      val terms         = mockRec.extractData
      // Expect both sides of par to be normalized in sequence
      val expectedTerms = Seq(TermData(ProcTerm(left)), TermData(ProcTerm(right)))

      terms shouldBe expectedTerms
    }
  }
}
