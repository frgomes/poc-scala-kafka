package io.mathminds.plutus


case class  RecordFX(tstamp: String, ask: Long, bid: Long, vol: Int)

case object RecordFX {
  def apply(text: String, precision: Int): RecordFX = {
    val parts = text.split(",")
    val tstamp: String = parts(0)
    val ask   : Long = (parts(1).toFloat * precision).toLong
    val bid   : Long = (parts(2).toFloat * precision).toLong
    val vol   : Int  = parts(3).toInt
    RecordFX(tstamp, ask, bid, vol)
  }

  object Serde extends org.apache.kafka.common.serialization.Serde[RecordFX] {
    import org.apache.kafka.common.serialization.{Deserializer, Serdes, Serializer}

    override def configure(configs: java.util.Map[String, _], isKey: Boolean) = {}
    override def close() = {}

    override def deserializer(): Deserializer[RecordFX] =
      new Deserializer[RecordFX]() {
        override def configure(configs: java.util.Map[String, _], isKey: Boolean) = { }
        override def close() = { }
        override def deserialize(topic: String, data: Array[Byte]): RecordFX = {
          val len = data.length - java.lang.Long.BYTES - java.lang.Long.BYTES - java.lang.Integer.BYTES
          val tstamp = Serdes.String.deserializer.deserialize(topic,  data.slice(0, len))
          val ask    = Serdes.Long.deserializer.deserialize(topic,    data.slice(len, java.lang.Long.BYTES))
          val bid    = Serdes.Long.deserializer.deserialize(topic,    data.slice(len+1*java.lang.Long.BYTES, java.lang.Long.BYTES))
          val vol    = Serdes.Integer.deserializer.deserialize(topic, data.slice(len+2*java.lang.Long.BYTES, java.lang.Integer.BYTES))
          RecordFX(tstamp, ask, bid, vol)
        }
      }

    override def serializer(): Serializer[RecordFX] =
      new Serializer[RecordFX]() {
        override def configure(configs: java.util.Map[String, _], isKey: Boolean) = { }
        override def close() = { }
        override def serialize(topic: String, data: RecordFX): Array[Byte] = {
          val tstamp = Serdes.String.serializer.serialize(topic, data.tstamp)
          val ask = Serdes.Long.serializer.serialize(topic, data.ask)
          val bid = Serdes.Long.serializer.serialize(topic, data.bid)
          val vol = Serdes.Integer.serializer.serialize(topic, data.vol)
          tstamp ++ ask ++ bid ++ vol
        }
      }
  }
}
