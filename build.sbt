scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-M5"
)

lazy val replesent = RootProject(uri("git://github.com/marconilanna/REPLesent.git#dba078837f21a4768a617ed5da2c9b771a4190a9"))
lazy val root = (project in file("."))
  .dependsOn(replesent)
