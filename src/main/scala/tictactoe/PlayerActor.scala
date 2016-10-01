package tictactoe

import akka.actor.{Actor, ActorRef, FSM}
import akka.actor.Actor.Receive

object PlayerActor {
  sealed trait PlayerState
  case object Disconnected extends PlayerState
  case object WaitingForBoard extends PlayerState
  case object PlayingTurn extends PlayerState
  case object PlayingWaiting extends PlayerState
  
  sealed trait PlayerData
  case object NoData extends PlayerData
  case class Data(board: BoardActor, outgoingActor: ActorRef) extends PlayerData

  sealed trait PlayerEvent
  case class Connect(outgoingActor: ActorRef) extends PlayerEvent

  sealed trait OutgoingMessage extends PlayerEvent
  case class BoardState() extends OutgoingMessage
  case class GameOver() extends OutgoingMessage
  case class GameStart() extends OutgoingMessage

  sealed trait IncomingMessage extends PlayerEvent
  case class Mark(cell: (Int, Int)) extends IncomingMessage
  
//  trait IncomingMessage
//  trait OutgoingMessage
//
//  case class Join(outgoingActor: ActorRef) extends IncomingMessage
//
//  case class PlayerJoined(player: Player) extends OutgoingMessage
//  case class PlayerLeft(player: Player) extends OutgoingMessage
}

class PlayerActor() extends FSM[PlayerActor.PlayerState, PlayerActor.PlayerData] {
  import PlayerActor._

  startWith(Disconnected, NoData)

  when(Disconnected) {
    case Event(Connect(outgoingActor), _) => {
      goto(WaitingForBoard) using Data(null, outgoingActor)
    }
  }

  when(WaitingForBoard) { ??? }

  when(PlayingTurn) { ??? }

  when(PlayingWaiting) { ??? }

  whenUnhandled {
    case Event(outgoingMessage, playerData: Data) => {
      playerData.outgoingActor ! outgoingMessage
      stay() using playerData
      // Send message to user
    }
    case Event(_, playerData) => {
      stay() using playerData
    }
  }

}
