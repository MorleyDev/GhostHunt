package uk.co.morleydev.ghosthunt.test.data.net

import org.scalatest.FunSpec
import uk.co.morleydev.ghosthunt.data.net.{NetworkMessage, Client, Server}

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

    describe("When server broadcasts messages") {
      val messages = Seq(new NetworkMessage("hello", "10"), new NetworkMessage("world", "asd"))
      messages.foreach(message => server.broadcast(message))
      Thread.sleep(100)

      it("Then the client received the messages") {
        clients.foreach(s => assert(s.receive() == messages))
      }
    }

    describe("When a client sends messages") {
      val messages = Seq(new NetworkMessage("hello","10"), new NetworkMessage("world","asd"))
      messages.foreach(message => clients(0).send(message))
      Thread.sleep(100)

      it("Then the server received the messages") {
        assert(server.receive().map(_._2) == messages)
      }
    }
  }
}
