name := "poc-mifid"
version := "0.1.0-SNAPSHOT"
organization := "org.example"

scalaVersion in ThisBuild := "2.11.11"

val versions = new {
  val kafka = "0.11.0.1"
  val utest = "0.5.4"
}

doctestDecodeHtmlEntities := true
doctestMarkdownEnabled    := true
doctestTestFramework      := DoctestTestFramework.MicroTest

libraryDependencies ++= Seq(
  //TODO: "com.github.aseigneurin" %% "kafka-streams-scala" % "0.0.1-SNAPSHOT",
  "org.apache.kafka" %  "kafka-streams" % versions.kafka,
  "org.apache.kafka" %  "kafka-clients" % versions.kafka,
  "com.lihaoyi"      %% "utest"         % versions.utest  % "test",
)

// For the time being...
dependsOn(RootProject(uri("git://github.com/frgomes/kafka-streams-scala.git#support-kafka-0.11")))
