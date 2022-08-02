import Dependencies._

lazy val root = (project in file("."))
  .settings(
    organization := "com.iscs",
    name := "collectioncleaner",
    version := "1.0",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      mongo4cats.core,
      mongo4cats.circe,
      mongodb.driver,
      scalaTest,
      logback.classic,
      logback.logging,
      cats.retry
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    reStartArgs := Seq("7"),
    Revolver.enableDebugging(5061, true)
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature"
  //"-Xfatal-warnings",
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
