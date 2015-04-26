scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-testkit-experimental" % "1.0-RC1",
  "com.typesafe.akka" %% "akka-http-scala-experimental" % "1.0-RC1",
  "io.reactivex" % "rxjava-reactive-streams" % "0.5.0"
)

initialCommands := """
def go(intp: scala.tools.nsc.interpreter.IMain, s: Int) = {
  val pres = REPLesent(intp=intp)
  import pres._
  s.g
  pres
}

def run(intp: scala.tools.nsc.interpreter.IMain, s: Int) = {
  val pres = REPLesent(intp=intp)
  import pres._
  s.g
  r
}

def cal(intp: scala.tools.nsc.interpreter.IMain) = {
  val pres = REPLesent(intp=intp)
  import pres._
  1.g
  ()
}

import akka.actor._
import akka.stream._
import akka.stream.scaladsl._
import akka.stream.testkit.scaladsl._
import akka.stream.OperationAttributes._
import akka.stream.OverflowStrategy._
import akka.stream.stage._
import akka.stream.io._
import akka.http.scaladsl._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server.Directives._
import akka.util.ByteString
import akka.pattern.after
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

def wireAllTheThings[T](thunk: ActorSystem => FlowMaterializer => ExecutionContext => T) = {
  implicit val sys = ActorSystem("streams")
  val mat = ActorFlowMaterializer()
  thunk(sys)(mat)(sys.dispatcher)
}
"""

lazy val replesent = RootProject(uri("git://github.com/marconilanna/REPLesent.git#dba078837f21a4768a617ed5da2c9b771a4190a9"))
lazy val root = (project in file("."))
  .dependsOn(replesent)
