import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Embedded HTTP Server",
    libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.3",
    libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.12", 
    libraryDependencies += scalaTest % Test
  )
