package sdk.store

import cats.effect.Sync
import cats.syntax.all.*
import sdk.Codec
import sdk.data.ByteArray

/// KV typed store that uses ByteArray as a heap object to copy native memory
class ByteArrayKeyValueTypedStore[F[_]: Sync, K, V](
  store: KeyValueStore[F],
  kCodec: Codec[K, ByteArray, Throwable],
  vCodec: Codec[V, ByteArray, Throwable],
) extends KeyValueTypedStore[F, K, V] {

  def encodeKey(key: K): F[ByteArray]     = kCodec
    .encode(key)
    .fold(
      err => new Exception(err.getMessage).raiseError[F, ByteArray],
      _.pure[F],
    )
  def decodeKey(bytes: ByteArray): F[K]   = kCodec
    .decode(bytes)
    .fold(
      err => new Exception(err.getMessage).raiseError[F, K],
      _.pure[F],
    )
  def encodeValue(value: V): F[ByteArray] = vCodec
    .encode(value)
    .fold(
      err => new Exception(err.getMessage).raiseError[F, ByteArray],
      _.pure[F],
    )
  def decodeValue(bytes: ByteArray): F[V] = vCodec
    .decode(bytes)
    .fold(
      err => new Exception(err.getMessage).raiseError[F, V],
      _.pure[F],
    )

  import cats.instances.option.*
  import cats.instances.vector.*

  override def get(keys: Seq[K]): F[Seq[Option[V]]] =
    for {
      keysByteArray <- keys.toVector.traverse(encodeKey)
      keysBuf        = keysByteArray.map(_.toByteBuffer)
      valuesBytes   <- store.get(keysBuf, ByteArray(_))
      values        <- valuesBytes.toVector.traverse(_.traverse(decodeValue))
    } yield values

  override def put(kvPairs: Seq[(K, V)]): F[Unit] =
    for {
      pairsByteArray <- kvPairs.toVector.traverse { case (k, v) =>
                          encodeKey(k).map2(encodeValue(v))((x, y) => (x, y))
                        }
      pairs           = pairsByteArray.map { case (k, v) => (k.toByteBuffer, v) }
      _              <- store.put[ByteArray](pairs, _.toByteBuffer)
    } yield ()

  override def delete(keys: Seq[K]): F[Int] =
    for {
      keysByteArray <- keys.toVector.traverse(encodeKey)
      keysBuf        = keysByteArray.map(_.toByteBuffer)
      deletedCount  <- store.delete(keysBuf)
    } yield deletedCount

  override def contains(keys: Seq[K]): F[Seq[Boolean]] =
    for {
      keysByteArray <- keys.toVector.traverse(encodeKey)
      keysBuf        = keysByteArray.map(_.toByteBuffer)
      results       <- store.get(keysBuf, _ => ())
    } yield results.map(_.nonEmpty)

  override def collect[T](pf: PartialFunction[(K, () => V), T]): F[Seq[T]] = ??? // TODO: implement in the future
//    for {
//      values <- store.iterate(
//                  _.map { case (kBuff, vBuff) =>
//                    val kBytes = BitVector(kBuff)
//                    // Inside LMDB iterator can only be synchronous operation / unsafe decode
//                    val k      = kCodec.decodeValue(kBytes).require
//                    // Lazy evaluate/decode value
//                    val fv     = () => {
//                      val vBytes = BitVector(vBuff)
//                      vCodec.decodeValue(vBytes).require
//                    }
//                    (k, fv)
//                  }.collect(pf).toVector,
//                )
//    } yield values

  override def toMap: F[Map[K, V]] =
    for {
      valuesBytes <- store.iterate(
                       _.map { case (k, v) => (ByteArray(k), ByteArray(v)) }.toVector,
                     )
      values      <- valuesBytes.traverse { case (k, v) =>
                       for {
                         key   <- decodeKey(k)
                         value <- decodeValue(v)
                       } yield (key, value)

                     }
    } yield values.toMap
}