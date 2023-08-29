// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package coop.rchain.models
import coop.rchain.models.BitSetBytesMapper.bitSetBytesMapper
import coop.rchain.models.ParSetTypeMapper.parSetESetTypeMapper
import coop.rchain.models.ParMapTypeMapper.parMapEMapTypeMapper
import coop.rchain.models.BigIntTypeMapper.bigIntBytesTypeMapper

@SerialVersionUID(0L)
final case class VarRef(
    index: _root_.scala.Int = 0,
    depth: _root_.scala.Int = 0
    ) extends coop.rchain.models.StacksafeMessage[VarRef] with scalapb.lenses.Updatable[VarRef] {
    
    override def equals(x: Any): Boolean = {
    
      import coop.rchain.catscontrib.effect.implicits.sEval
    
     coop.rchain.models.EqualM[coop.rchain.models.VarRef].equals[cats.Eval](this, x).value
    
    }
    
    override def hashCode(): Int = {
    
      import coop.rchain.catscontrib.effect.implicits.sEval
    
     coop.rchain.models.HashM[coop.rchain.models.VarRef].hash[cats.Eval](this).value
    
    }
    
    
    def mergeFromM[F[_]: cats.effect.Sync](`_input__`: _root_.com.google.protobuf.CodedInputStream): F[coop.rchain.models.VarRef] = {
      
      import cats.effect.Sync
      import cats.syntax.all._
      
      Sync[F].defer {
        var __index = this.index
        var __depth = this.depth
        var _done__ = false
        
        Sync[F].whileM_ (Sync[F].delay { !_done__ }) {
          for {
            _tag__ <- Sync[F].delay { _input__.readTag() }
            _ <- _tag__ match {
              case 0 => Sync[F].delay { _done__ = true }
              case 8 =>
                for {
                  readValue       <- Sync[F].delay { _input__.readSInt32() }
                  customTypeValue =  readValue
                  _               <- Sync[F].delay { __index = customTypeValue }
                } yield ()
              case 16 =>
                for {
                  readValue       <- Sync[F].delay { _input__.readSInt32() }
                  customTypeValue =  readValue
                  _               <- Sync[F].delay { __depth = customTypeValue }
                } yield ()
            case tag => Sync[F].delay { _input__.skipField(tag) }
            }
          } yield ()
        }
        .map { _ => coop.rchain.models.VarRef(
          index = __index,
          depth = __depth
        )}
      }
    }
    
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = index
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeSInt32Size(1, __value)
        }
      };
      
      {
        val __value = depth
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeSInt32Size(2, __value)
        }
      };
      __size
    }
    override def serializedSize: _root_.scala.Int = {
      var __size = __serializedSizeMemoized
      if (__size == 0) {
        __size = __computeSerializedSize() + 1
        __serializedSizeMemoized = __size
      }
      __size - 1
      
    }
    
    @transient var _serializedSizeM: coop.rchain.models.Memo[Int] = null
    
    def serializedSizeM: coop.rchain.models.Memo[Int] = synchronized {
      if(_serializedSizeM == null) {
        _serializedSizeM = new coop.rchain.models.Memo(coop.rchain.models.ProtoM.serializedSize(this))
        _serializedSizeM
      } else _serializedSizeM
    }
    def writeTo(`_output__`: _root_.com.google.protobuf.CodedOutputStream): _root_.scala.Unit = {
      {
        val __v = index
        if (__v != 0) {
          _output__.writeSInt32(1, __v)
        }
      };
      {
        val __v = depth
        if (__v != 0) {
          _output__.writeSInt32(2, __v)
        }
      };
    }
    def withIndex(__v: _root_.scala.Int): VarRef = copy(index = __v)
    def withDepth(__v: _root_.scala.Int): VarRef = copy(depth = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = index
          if (__t != 0) __t else null
        }
        case 2 => {
          val __t = depth
          if (__t != 0) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(index)
        case 2 => _root_.scalapb.descriptors.PInt(depth)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: coop.rchain.models.VarRef.type = coop.rchain.models.VarRef
    // @@protoc_insertion_point(GeneratedMessage[VarRef])
}

object VarRef extends scalapb.GeneratedMessageCompanion[coop.rchain.models.VarRef] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[coop.rchain.models.VarRef] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): coop.rchain.models.VarRef = {
    var __index: _root_.scala.Int = 0
    var __depth: _root_.scala.Int = 0
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 8 =>
          __index = _input__.readSInt32()
        case 16 =>
          __depth = _input__.readSInt32()
        case tag => _input__.skipField(tag)
      }
    }
    coop.rchain.models.VarRef(
        index = __index,
        depth = __depth
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[coop.rchain.models.VarRef] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      coop.rchain.models.VarRef(
        index = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Int]).getOrElse(0),
        depth = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Int]).getOrElse(0)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = RhoTypesProto.javaDescriptor.getMessageTypes().get(45)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = RhoTypesProto.scalaDescriptor.messages(45)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = coop.rchain.models.VarRef(
    index = 0,
    depth = 0
  )
  implicit class VarRefLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, coop.rchain.models.VarRef]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, coop.rchain.models.VarRef](_l) {
    def index: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.index)((c_, f_) => c_.copy(index = f_))
    def depth: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.depth)((c_, f_) => c_.copy(depth = f_))
  }
  final val INDEX_FIELD_NUMBER = 1
  final val DEPTH_FIELD_NUMBER = 2
  def of(
    index: _root_.scala.Int,
    depth: _root_.scala.Int
  ): _root_.coop.rchain.models.VarRef = _root_.coop.rchain.models.VarRef(
    index,
    depth
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[VarRef])
}
