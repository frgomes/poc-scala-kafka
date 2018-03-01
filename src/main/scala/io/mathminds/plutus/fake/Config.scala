package io.mathminds.plutus.fake

trait Config {
  val props: java.util.Map[String, Any]

  import scala.reflect.ClassTag
  
  def getProperty[T : ClassTag](property: String): T =
    props.get(property).asInstanceOf[T]

  def getProperty[T : ClassTag](property: String, default: T): T =
    if(props.containsKey(property)) props.get(property).asInstanceOf[T] else default


/*
  protected val impl     : String = "fake"
  protected var precision: Int = 6
  protected var version  : String = "???"
  protected var system   : String = "???"
  protected var server   : String = "???"
  protected var group    : String = "???"
  protected var provider : String = "???"
  protected var tenor    : String = "???"
  protected var market   : String = "???"

  def config(props: java.util.Map[String, Any]): Unit = {
    precision = if(props.containsKey("precision")) props.get("precision").asInstanceOf[Int]    else 6
    version   = if(props.containsKey("version"))   props.get("version")  .asInstanceOf[String] else "noversion"
    system    = if(props.containsKey("system"))    props.get("system")   .asInstanceOf[String] else "nosystem"
    server    = if(props.containsKey("server"))    props.get("server")   .asInstanceOf[String] else "localhost:9092"
    group     = if(props.containsKey("group"))     props.get("group")    .asInstanceOf[String] else "nogroup"
    provider  = props.get("provider").toString
    tenor     = props.get("tenor").toString
    market    = props.get("market").toString
  }
 */
}
