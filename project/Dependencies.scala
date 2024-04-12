import sbt._

object Dependencies {
  object Versions {
    val LogbackVersion = "1.5.3"
    val loggingVersion = "3.9.5"
    val mongo4catsVersion = "0.7.3"
    val scalatestVersion = "3.2.2"
    val mongoScalaVersion = "5.0.1"
  }

  lazy val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalatestVersion % Test

  object logback {
    val classic = "ch.qos.logback"            % "logback-classic" % Versions.LogbackVersion
    val logging = "com.typesafe.scala-logging" %% "scala-logging" % Versions.loggingVersion
  }

  object mongodb {
    val driver = "org.mongodb.scala" %% "mongo-scala-driver" % Versions.mongoScalaVersion
  }

  object mongo4cats {
    val core = "io.github.kirill5k" %% "mongo4cats-core" % Versions.mongo4catsVersion
    val circe = "io.github.kirill5k" %% "mongo4cats-circe" % Versions.mongo4catsVersion
  }
}
