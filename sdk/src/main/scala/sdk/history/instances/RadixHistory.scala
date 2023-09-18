package sdk.history.instances

import cats.Parallel
import cats.effect.Sync
import cats.syntax.all.*
import scodec.bits.ByteVector
import sdk.data.Blake2b256Hash
import sdk.history.RadixTree.*
import sdk.history.{History, HistoryAction, KeySegment, RadixTree}
import sdk.store.{KeyValueStore, KeyValueTypedStore}
import sdk.syntax.all.sharedSyntaxKeyValueStore

/**
  * History implementation with radix tree
  */
object RadixHistory {
  val emptyRootHash: Blake2b256Hash = RadixTree.emptyRootHash
  val codecBlakeHash                =
    scodec.codecs.bytes.xmap[Blake2b256Hash](bv => Blake2b256Hash.fromByteVector(bv), _.bytes)

  def apply[F[_]: Sync: Parallel](
    root: Blake2b256Hash,
    store: KeyValueTypedStore[F, Blake2b256Hash, ByteVector],
  ): F[RadixHistory[F]] =
    for {
      impl <- Sync[F].delay(new RadixTreeImpl[F](store))
      node <- impl.loadNode(root, noAssert = true)
    } yield RadixHistory(root, node, impl, store)

  def createStore[F[_]: Sync](
    store: KeyValueStore[F],
  ): KeyValueTypedStore[F, Blake2b256Hash, ByteVector] =
    store.toTypedStore(codecBlakeHash, scodec.codecs.bytes)
}

final case class RadixHistory[F[_]: Sync: Parallel](
  rootHash: Blake2b256Hash,
  rootNode: Node,
  impl: RadixTreeImpl[F],
  store: KeyValueTypedStore[F, Blake2b256Hash, ByteVector],
) extends History[F] {
  override type HistoryF = History[F]

  override def root: Blake2b256Hash = rootHash

  override def reset(root: Blake2b256Hash): F[History[F]] =
    for {
      impl <- Sync[F].delay(new RadixTreeImpl[F](store))
      node <- impl.loadNode(root, noAssert = true)
    } yield this.copy(root, node, impl, store)

  override def read(key: KeySegment): F[Option[Blake2b256Hash]] =
    impl.read(rootNode, key)

  override def process(actions: List[HistoryAction]): F[History[F]] =
    for {

      /** TODO: To improve time, it is possible to implement this check into the [[RadixTreeImpl.saveAndCommit()]]. */
      _ <- new RuntimeException("Cannot process duplicate actions on one key.").raiseError
             .unlessA(hasNoDuplicates(actions))

      newRootDataOpt <- impl.saveAndCommit(rootNode, actions)
      newHistoryOpt   = newRootDataOpt.map { newRootData =>
                          val (newRootNode, newRootHash) = newRootData
                          this.copy(newRootHash, newRootNode, impl, store)
                        }
      _               = impl.clearReadCache()
    } yield newHistoryOpt.getOrElse(this)

  private def hasNoDuplicates(actions: List[HistoryAction]) =
    actions.map(_.key).distinct.sizeIs == actions.size
}
