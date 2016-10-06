package tictactoe

class PlayerMessageSerializerSpec extends TicTacToeTestBase {

  import PlayerActor._

  "PlayerMessageSerializer" should "correctly convert Mark event to OutgoingMessage with JSON" in {
    val outgoingMsg = PlayerMessageSerializer.eventToOutgoingMessage(Mark((1, 1)))
    assertResult(outgoingMsg) {
      OutgoingMessage("Dummy")
    }
  }

  it should "correctly convert Joined event to OutgoingMessage" in {
    val outgoingMsg = PlayerMessageSerializer.eventToOutgoingMessage(Joined())
    assertResult(outgoingMsg) {
      OutgoingMessage("Joined")
    }
  }

  it should "throw on unknown event type" in {
    assertThrows[Exception] {
      PlayerMessageSerializer.eventToOutgoingMessage(new PlayerEvent {})
    }
  }
}
