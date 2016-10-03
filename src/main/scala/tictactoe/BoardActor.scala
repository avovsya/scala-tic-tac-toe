package tictactoe

import akka.actor.{Actor, ActorRef, FSM, Terminated}
import akka.actor.Actor.Receive

object BoardActor {

  sealed trait BoardState
  case object WaitingForPlayers extends BoardState
  case object Playing extends BoardState

  sealed trait BoardData
  case object NoData extends BoardData
  case class Data(players: Vector[ActorRef], board: Board) extends BoardData

  // Events
  case class PlayerJoined(player: ActorRef)
}

class BoardActor(boardManager: ActorRef) extends FSM[BoardActor.BoardState, BoardActor.BoardData] {
  import BoardActor._

  startWith(WaitingForPlayers, NoData)

  when(WaitingForPlayers) {
    case Event(PlayerJoined(player: ActorRef), data: Data) => {
      val players = data.players :+ player
      context.watch(player)
      player ! PlayerActor.JoinBoard(self)
      players foreach (_ ! PlayerActor.GameStarts)
      goto(Playing) using Data(players, createBoard)
    }

    case Event(PlayerJoined(player: ActorRef), _) => {
      context.watch(player)
      player ! PlayerActor.JoinBoard(self)
      stay using(Data(Vector(player), null))
    }
  }

  when(Playing) {
    // When player left
    case Event(Terminated(player), Data(players, _)) => {
      players foreach (_ ! PlayerActor.GameClosed)
      boardManager ! BoardManager.BoardReleased(self)
      goto(WaitingForPlayers) using NoData
    }
  }

  def createBoard: Board = new Board
}
