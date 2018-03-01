package io.mathminds.plutus.fake

import utest._

object FakeSpec extends TestSuite {
  val tests = this {
    "Ability to produce raw fake records and process them"-{
      {
        val props = new java.util.HashMap[String, Any]
        props.put("precision", 6)
        props.put("version",   "mvp1")
        props.put("system",    "demo")
        props.put("provider",  "DAT_ASCII")
        props.put("tenor",     "T_2017XX")
        props.put("market",    "EURUSD")
        val worker = new Producer(props)
        worker.run
      }
      {
        val props = new java.util.HashMap[String, Any]
        props.put("precision",   6)
        props.put("version",     "mvp1")
        props.put("system",      "demo")
        props.put("application", "ingestor-fake")
        props.put("provider",    "DAT_ASCII")
        props.put("tenor",       "T_2017XX")
        props.put("market",      "EURUSD")
        val worker = new Ingestor(props)
        worker.run
      }
      {
        val props = new java.util.HashMap[String, Any]
        props.put("precision", 6)
        props.put("version",   "mvp1")
        props.put("system",    "demo")
        props.put("group",     "consumer-fake")
        props.put("provider",  "DAT_ASCII")
        props.put("tenor",     "T_2017XX")
        props.put("market",    "EURUSD")
        val worker = new Consumer(props)
        worker.run
      }
    }
  }
}
