package tictactoe

object PlayerMessageSerializer {
  import PlayerActor._

  def eventToOutgoingMessage(event: PlayerEvent): OutgoingMessage = event match {
    case Joined() => OutgoingMessage("Joined")
    case Mark(_) => OutgoingMessage("Dummy")
    case _ => throw new Exception
  }
}
