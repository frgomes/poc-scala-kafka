package io.mathminds.plutus.fake

import io.mathminds.util.KafkaAdmin


class Ingestor(val props: java.util.Map[String, Any]) extends Config with KafkaAdmin with Runnable {
  private val impl       : String = "fake"
  private val precision  : Int    = getProperty("precision",   6)
  private val version    : String = getProperty("version",     "noversion")
  private val system     : String = getProperty("system",      "nosystem")
  private val server     : String = getProperty("server",      "localhost:9092")
  private val application: String = getProperty("application", "noapplication")
  private val provider   : String = getProperty("provider")
  private val tenor      : String = getProperty("tenor")
  private val market     : String = getProperty("market")

  private val topicSink   = s"${version}.${system}.${impl}.${provider}.${precision}.${tenor}.${market}"
  private val topicSource = s"${version}.${system}.${impl}.${provider}.${precision}.${tenor}.${market}.raw"

  def run: Unit = {
    import org.apache.kafka.common.serialization.Serdes
    import org.apache.kafka.clients.CommonClientConfigs
    import org.apache.kafka.clients.consumer.ConsumerConfig
    import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
    import io.mathminds.plutus.RecordFX

    createTopic(server, topicSink)

    val props = new java.util.Properties
    props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, server)
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, application)
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    import com.github.aseigneurin.kafka.streams.scala._

    val source: KStreamS[Integer, String] = KStreamBuilderS.stream[Integer, String](topicSource)(Serdes.Integer, Serdes.String)
    val sink: KStreamS[Integer, RecordFX] = source.mapValues[RecordFX](v => RecordFX(v, precision))
    sink.to(topicSink)(Serdes.Integer, RecordFX.Serde)

    val streams = new KafkaStreams(KStreamBuilderS.inner, props)
    streams.start()

    //FIXME
    // usually the stream application would be running forever,
    // in this example we just let it run for some time and stop since the input data is finite.
    Thread.sleep(5000L)
    streams.close()
  }
}
