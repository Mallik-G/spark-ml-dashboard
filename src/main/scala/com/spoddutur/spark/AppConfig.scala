package com.spoddutur.spark

import com.typesafe.config.ConfigFactory

/**
  * Created by sruthi on 02/08/17.
  * Loads default config params from application.conf file.
  * It also supports cmd-line args to override the default values.
  */
object AppConfig {

  val conf = ConfigFactory.load
  val sparkMasterDef = conf.getString("spark.master")
  val sparkAppNameDef = conf.getString("spark.appname")
  val akkaHttpPortDef = conf.getInt("akka.http.port")

  var akkaHttpPort = akkaHttpPortDef
  var sparkMaster = sparkMasterDef
  var sparkAppName = sparkAppNameDef

  def main(args: Array[String]): Unit = {
    parse("-m localhost1 --akkaHttpPort 8080".split(" ").toList)
    print(sparkMaster, sparkAppName, akkaHttpPort)
  }

  val usage =
    s"""
    Apart from Spark, this application uses akka-http from browser integration.
    So, it needs config params like AkkaWebPort to bind to, SparkMaster
    and SparkAppName.

    Usage: spark-submit graph-knowledge-browser.jar [options]
      Options:
      -h, --help
      -m, --master <master_url>                    spark://host:port, mesos://host:port, yarn, or local. Default: $sparkMasterDef
      -n, --name <name>                            A name of your application. Default: $sparkAppNameDef
      -p, --akkaHttpPort <portnumber>              Port where akka-http is binded. Default: $akkaHttpPortDef

    Configured one route:
    1. http://host:port/index.html - takes user to knowledge browser page
  """

  def parse(list: List[String]): this.type = {

    list match {
      case Nil => this
      case ("--master" | "-m") :: value :: tail => {
        sparkMaster = value
        parse(tail)
      }
      case ("--name" | "-n") :: value :: tail => {
        sparkAppName = value
        parse(tail)
      }
      case ("--akkaHttpPort" | "-p") :: value :: tail => {
        akkaHttpPort = value.toInt
        parse(tail)
      }
      case ("--help" | "-h") :: tail => {
        printUsage(0)
      }
      case _ => {
        printUsage(1)
      }
    }
  }

  def printUsage(exitNumber: Int) = {
    println(usage)
    sys.exit(status = exitNumber)
  }
}
