package tictactoe

import akka.actor.{Actor, ActorRef, Props}
import akka.actor.Actor.Receive

object BoardManager {
  case class FindAndJoinBoard(player: ActorRef)
  case class BoardReleased(board: ActorRef)
}

class BoardManager extends Actor {
  import BoardManager._

  var boards: Map[ActorRef, Int] = Map.empty[ActorRef, Int]

  def createEmptyBoard: ActorRef = {
    val boardActor = context.actorOf(Props[BoardActor])
    boards += boardActor -> 0
    boardActor
  }

  def getOrCreateBoard: ActorRef = {
    val boardMapping: (ActorRef, Int) = boards.find(_._2 < 2).getOrElse(createEmptyBoard -> 0)

    boards += boardMapping._1 -> (boardMapping._2 + 1)

    boardMapping._1
  }

  def releaseBoard(board: ActorRef): Unit = {
    // TODO: should remove empty boards if there are too many of them
    boards += board -> 0
  }

  override def receive: Receive = {
    case FindAndJoinBoard(player: ActorRef) => {
      val readyBoard = getOrCreateBoard
      readyBoard ! BoardActor.PlayerJoined(player)
    }

    case BoardReleased(board: ActorRef) => releaseBoard(board)
  }
}
