package io.mathminds.util


trait KafkaAdmin {
  import org.apache.kafka.clients.CommonClientConfigs

  def createTopic(server: String, topic: String): Unit = {
    //FIXME: validate name of topic. Should not contain "(" and possibly other things
    import scala.collection.JavaConverters._
    import org.apache.kafka.clients.admin._

    val props = new java.util.HashMap[String, AnyRef]
    props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, server)
    val admin = AdminClient.create(props)
    val cmd: CreateTopicsResult = admin.createTopics(Seq(new NewTopic(topic, 1, 1)).asJava)
    val f = cmd.values().get(topic) //FIXME
    println(f.toString)
  }
}
