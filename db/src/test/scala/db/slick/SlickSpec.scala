package db.slick

import cats.data.OptionT
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.ScalacheckShapeless.*
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import sdk.db.RecordNotFound
import sdk.primitive.ByteArray
import slick.api.ValidatorDbApi
import slick.data.Validator

class SlickSpec extends AsyncFlatSpec with Matchers with ScalaCheckPropertyChecks {

  implicit val validatorArbitrary: Arbitrary[Validator] = Arbitrary {
    for {
      id     <- Gen.posNum[Long]
      pubKey <- Gen.alphaStr.map(_.getBytes)
    } yield Validator(id, ByteArray(pubKey))
  }

  "Validator insert function call" should "add the correct entry to the Validator table" in {
    forAll { (validator: Validator) =>
      def test(api: ValidatorDbApi[IO]) = for {
        id                <- api.insert(validator.pubKey)
        validatorById     <- OptionT(api.getById(id)).getOrRaise(new RecordNotFound)
        validatorByPubKey <- OptionT(api.getByPublicKey(validator.pubKey)).getOrRaise(new RecordNotFound)
      } yield {
        id shouldBe 1L

        validatorById.pubKey shouldBe validator.pubKey
        validatorById shouldBe validatorByPubKey
      }

      EmbeddedH2SlickDb[IO]
        .map(implicit x => new ValidatorDbApi[IO])
        .use(test)
        .unsafeRunSync()
    }
  }
}
