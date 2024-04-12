import Dependencies._

ThisBuild / scalaVersion     := "2.13.12" // use your specific Scala version
ThisBuild / javacOptions     ++= Seq("--release", "17") // For Java 17
ThisBuild / scalacOptions    ++= Seq("--release", "17")


lazy val root = (project in file("."))
  .settings(
    organization := "com.iscs",
    name := "collectioncleaner",
    version := "1.0",
    libraryDependencies ++= Seq(
      mongo4cats.core,
      mongo4cats.circe,
      mongodb.driver,
      scalaTest,
      logback.classic,
      logback.logging
    ),
    reStartArgs := Seq("7"),
    Revolver.enableDebugging(5061, true),
    dependencyOverrides ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.5.1"
    )
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature"
  //"-Xfatal-warnings",
)

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", "services", xs @ _*) => MergeStrategy.concat
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case "io.netty.versions.properties" => MergeStrategy.discard
  case "reference.conf" => MergeStrategy.concat
  case x => MergeStrategy.first
}
