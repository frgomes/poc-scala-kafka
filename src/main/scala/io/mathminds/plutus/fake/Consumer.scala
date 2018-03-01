package io.mathminds.plutus.fake

import org.apache.kafka.common.serialization.Serdes


class Consumer(val props: java.util.Map[String, Any]) extends Config with Runnable {
  private val impl     : String = "fake"
  private val precision: Int    = getProperty("precision", 6)
  private val version  : String = getProperty("version",   "noversion")
  private val system   : String = getProperty("system",    "nosystem")
  private val server   : String = getProperty("server",    "localhost:9092")
  private val group    : String = getProperty("group",     "nogroup")
  private val provider : String = getProperty("provider")
  private val tenor    : String = getProperty("tenor")
  private val market   : String = getProperty("market")

  private val topic = s"${version}.${system}.${impl}.${provider}.${precision}.${tenor}.${market}"

  def run: Unit = {
    import org.apache.kafka.clients.CommonClientConfigs
    import org.apache.kafka.clients.consumer.ConsumerConfig
    import org.apache.kafka.clients.consumer.ConsumerRecords
    import org.apache.kafka.clients.consumer.KafkaConsumer
    import io.mathminds.plutus.RecordFX

    val props = new java.util.Properties
    props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, server)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, group)
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val consumer = new KafkaConsumer[Integer, RecordFX](props, Serdes.Integer.deserializer, RecordFX.Serde.deserializer)
    consumer.subscribe(java.util.Arrays.asList(topic))

    var size = 0
    do {
      val records: ConsumerRecords[Integer, RecordFX] = consumer.poll(100)
      import scala.collection.JavaConversions._
      for (record <- records) println(record)
      size = records.size
    } while (size > 0)
  }
}
