import sbt._

object Dependencies {
  object Versions {
    val Specs2Version = "4.9.3"
    val LogbackVersion = "1.2.3"
    val catsRetryVersion = "1.1.0"
    val log4catsVersion = "2.3.1"
    val loggingVersion = "3.9.2"
    val mongo4catsVersion = "0.6.15"
    val catsVersion = "3.9.0"
    val catsEffectVersion = "3.5.0"
    val scalatestVersion = "3.2.2"
    val mongoScalaVersion = "4.10.0"
  }

  lazy val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalatestVersion % Test

  object logback {
    val classic = "ch.qos.logback"            % "logback-classic" % Versions.LogbackVersion
    val logging = "com.typesafe.scala-logging" %% "scala-logging" % Versions.loggingVersion
  }

  object cats {
    val retry = "com.github.cb372" %% "cats-retry"      % Versions.catsRetryVersion
    val log4cats = "org.typelevel" %% s"log4cats-slf4j" % Versions.log4catsVersion
    val cats = "org.typelevel" %% "cats-core" % Versions.catsVersion
    val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffectVersion
  }

  object mongodb {
    val driver = "org.mongodb.scala" %% "mongo-scala-driver" % Versions.mongoScalaVersion
  }

  object mongo4cats {
    val core = "io.github.kirill5k" %% "mongo4cats-core" % Versions.mongo4catsVersion
    val circe = "io.github.kirill5k" %% "mongo4cats-circe" % Versions.mongo4catsVersion
  }
}
