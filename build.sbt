name := "GhostHunt"

version := "alpha-0.0.1"

scalaVersion := "2.10.3"

mainClass in (Compile, run) := Some("uk.co.morleydev.ghosthunt.Main")

libraryDependencies += "com.lambdaworks" % "jacks_2.10" % "2.2.3"
