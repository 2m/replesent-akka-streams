scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-testkit-experimental" % "1.0-RC1",
  "com.typesafe.akka" %% "akka-http-xml-experimental" % "1.0-RC1",
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
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
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

def rc(intp: scala.tools.nsc.interpreter.IMain) = {
  wireAllTheThings { implicit s => implicit m => implicit e =>
    val route = path("command" / Segment) { c =>
        put { ctx =>
          println()
          intp.interpret(c)
          print("scala> ")
          ctx.complete(OK)
        }
      } ~
      path("") {
        get { ctx =>
          val js = "function send(cmd) { var req = new XMLHttpRequest(); req.open('PUT', 'command/' + cmd, true); req.send() }"
          ctx.complete(
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<style><![CDATA[
body { text-align: center; }
button { height: 100%; width: 32%; }
]]></style>
</head>
<body>
<script type="text/javascript">{js}</script>
<button onclick="send('p')">Prev</button>
<button onclick="send('r')">Run</button>
<button onclick="send('n')">Next</button>
</body>
</html>
)
        }
      }

    Http().bindAndHandle(route, "0.0.0.0", 8888)
  }
}
"""

lazy val replesent = RootProject(uri("git://github.com/marconilanna/REPLesent.git#dba078837f21a4768a617ed5da2c9b771a4190a9"))
lazy val root = (project in file("."))
  .dependsOn(replesent)
