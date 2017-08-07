name := "Akka-Assignment-2"

version := "1.0"

scalaVersion := "2.12.3"




libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.3"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.3" % "test"


libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5" % "test"
coverageEnabled := true