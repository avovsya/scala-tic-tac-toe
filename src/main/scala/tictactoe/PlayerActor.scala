package tictactoe

import akka.actor.{Actor, ActorRef, FSM}
import akka.actor.Actor.Receive

object PlayerActor {
  sealed trait PlayerState
  case object Disconnected extends PlayerState
  case object WaitingForBoard extends PlayerState
  case object Playing extends PlayerState
//  case object PlayingTurn extends PlayerState
//  case object PlayingWaiting extends PlayerState
  
  sealed trait PlayerData
  case object NoData extends PlayerData
  case class Data(board: ActorRef, outgoingActor: ActorRef) extends PlayerData

  // Events
  case class Connect(outgoingActor: ActorRef)

  case class JoinBoard(boardActor: ActorRef)
  case object GameStarts
  case object GameClosed // When one of the players leaves game

  trait PlayerEvent
  case class Mark(cell: (Int, Int)) extends PlayerEvent
  case class Joined() extends PlayerEvent

  // TODO
  // PlayerActor is responsible for handling WebSocket connection messages(in & out)
  // What are the possible options to separate this from Player?
  // Is it really neccessary?
  // 1. Interim actor that parses incoming messages and forwards parsed message to PlayerActor
  // 2. ?
  case class IncomingMessage(msg: String)
  case class OutgoingMessage(msg: String)

//  sealed trait OutgoingMessage extends PlayerEvent
//  case class BoardState() extends OutgoingMessage
//  case class GameOver() extends OutgoingMessage
//  case class GameStart() extends OutgoingMessage
//
//  sealed trait IncomingMessage extends PlayerEvent
//  case class Mark(cell: (Int, Int)) extends IncomingMessage

//  trait IncomingMessage
//  trait OutgoingMessage
//
//  case class Join(outgoingActor: ActorRef) extends IncomingMessage
//
//  case class PlayerJoined(player: Player) extends OutgoingMessage
//  case class PlayerLeft(player: Player) extends OutgoingMessage
}

class PlayerActor(boardManager: ActorRef) extends FSM[PlayerActor.PlayerState, PlayerActor.PlayerData] {
  import PlayerActor._

  startWith(Disconnected, NoData)

  when(Disconnected) {
    case Event(Connect(outgoingActor), _) => {
      boardManager ! BoardManager.FindAndJoinBoard(self)
      goto(WaitingForBoard) using Data(null, outgoingActor)
    }
  }

  when(WaitingForBoard) {
    case Event(JoinBoard(boardActor: ActorRef), data: Data)  => {
      stay using(data.copy(board = boardActor))
    }
    case Event(GameStarts, data) => {
      goto(Playing) using data
    }
  }

  when(Playing) {
    case Event(GameClosed, _) => ???
  }

  whenUnhandled {
    case Event(msg: IncomingMessage, playerData: Data) => {
      // Parse incoming message and send to itself
      ???
    }

    case Event(msg: OutgoingMessage, playerData: Data) => {
      // Convert msg to JSON and send message to user
      playerData.outgoingActor ! msg
      stay() using playerData
    }

    case Event(_, playerData) => {
      println("WHAT HAPPENED??? I SHOULDN'T RECEIVE THIS")
      stay() using playerData
    }
  }

}
