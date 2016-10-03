package tictactoe

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn

class Server extends App{
  implicit val actorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val route = path("/") {
    get {
      complete("Such response")
    }
  }

  println("Server started")
  StdIn.readLine()
  actorSystem.terminate()
}
