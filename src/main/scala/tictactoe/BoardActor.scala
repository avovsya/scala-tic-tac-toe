package tictactoe

import akka.actor.{Actor, Terminated}
import akka.actor.Actor.Receive

object BoardActor {
  trait IncomingMessage
//  case class Join(player: Player) extends IncomingMessage
//  case class Leave(player: Player) extends IncomingMessage
}

class BoardActor extends Actor {
  import BoardActor._

  val board = new Board(this)

  override def receive: Receive = {
    ???
//    case Join(player) => {
//      board.join(player)
//      context.watch(player.actor)
//    }
//
//    case Leave(player) => board.leave(player)
//
//    case Terminated(playerActor: PlayerActor) => board.leave(playerActor.player)
  }
}
