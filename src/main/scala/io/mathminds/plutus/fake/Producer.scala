package io.mathminds.plutus.fake

import io.mathminds.util.KafkaAdmin
import org.apache.kafka.common.serialization.Serdes


class Producer(val props: java.util.Map[String, Any]) extends Config with KafkaAdmin with Runnable {
  private val impl     : String = "fake"
  private val precision: Int    = getProperty("precision", 6)
  private val version  : String = getProperty("version",   "noversion")
  private val system   : String = getProperty("system",    "nosystem")
  private val server   : String = getProperty("server",    "localhost:9092")
  private val provider : String = getProperty("provider")
  private val tenor    : String = getProperty("tenor")
  private val market   : String = getProperty("market")

  private val topic = s"${version}.${system}.${impl}.${provider}.${precision}.${tenor}.${market}.raw"

  def run: Unit = {
    import org.apache.kafka.clients.CommonClientConfigs
    import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

    val props = new java.util.Properties
    props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, server)

    createTopic(server, topic)

    val dir = System.getProperty("user.dir")
    val file: String = s"${dir}/src/test/resources/${provider}_${market}_${tenor}.csv"

    val it = scala.io.Source.fromFile(file).getLines
    val producer = new KafkaProducer[Integer, String](props, Serdes.Integer.serializer, Serdes.String.serializer)

    var i = 0
    while(it.hasNext) {
      i += 1
      producer.send(new ProducerRecord[Integer, String](topic, i, it.next))
    }
  }
}
