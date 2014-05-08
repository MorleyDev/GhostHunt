name := "GhostHunt"

version := "alpha-0.0.1"

scalaVersion := "2.10.3"

mainClass in (Compile, run) := Some("uk.co.morleydev.ghosthunt.Main")

libraryDependencies += "com.lambdaworks" % "jacks_2.10" % "2.2.3"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.1.3" % "test"

libraryDependencies += "org.scalacheck" % "scalacheck_2.10" % "1.11.3" % "test"

libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5" % "test"
