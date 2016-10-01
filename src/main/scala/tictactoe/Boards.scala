package tictactoe

import akka.actor.{ActorRef, ActorSystem, Props}

class Boards(implicit val actorSystem: ActorSystem) {
  var boards: Set[(ActorRef, Int)] = Set.empty

  private def createNewBoard(): ActorRef = {
    val board = actorSystem.actorOf(Props[Board])
    ???
  }
}
