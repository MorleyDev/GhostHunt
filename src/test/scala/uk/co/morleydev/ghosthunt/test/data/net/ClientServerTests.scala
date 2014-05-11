package uk.co.morleydev.ghosthunt.test.data.net

import org.scalatest.FunSpec
import uk.co.morleydev.ghosthunt.data.net.{Client, Server}
import scala.collection.GenSeq
import scala.concurrent.duration.Duration
import scala.concurrent.duration.SECONDS
import uk.co.morleydev.ghosthunt.model.GameTime
import com.fasterxml.jackson.annotation.JsonProperty
import uk.co.morleydev.ghosthunt.model.net.{NetworkMessage, ClientId}

case class MessageToSend(@JsonProperty("time") value : Int)

class ClientServerTests extends FunSpec {
  describe("Given a server with connected clients") {
    val server = new Server()
    server.listen(9090)

    val clients = Seq(new Client(), new Client())
    clients.foreach(client => client.connect("127.0.0.1", 9090))
    Thread.sleep(100)

    it("Then the server is connected") {
      assert(server.isConnected)
    }
    it("Then the clients are connected") {
      assert(clients.forall(_.isConnected))
    }

    var receivedMessages : GenSeq[(ClientId, NetworkMessage)] = null
    describe("When a client sends messages") {

      val gameTime = new GameTime(Duration(1, SECONDS), Duration(10, SECONDS))

      val messages = Seq(NetworkMessage("hello", new MessageToSend(10), gameTime),
                         new NetworkMessage("world","asd", Duration(1, SECONDS)))
      messages.foreach(message => clients(0).send(message))
      Thread.sleep(100)

      receivedMessages = server.receive()
      it("Then the server received the messages") {
        assert(receivedMessages.map(_._2) == messages)
      }
    }

    describe("When server broadcasts messages") {
      val messages = Seq(new NetworkMessage("hello", "10", Duration(1, SECONDS)), new NetworkMessage("world", "asd", Duration(1, SECONDS)))
      messages.foreach(message => receivedMessages.map(s => s._1).distinct.foreach(s => server.send(s, message)))
      Thread.sleep(100)

      it("Then the client received the messages") {
        clients.foreach(s => assert(s.receive() == messages))
      }
    }

  }
}
