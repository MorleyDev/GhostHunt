package uk.co.morleydev.ghosthunt.data.net

import scala.collection.{JavaConversions, GenSeq}
import java.util.concurrent.{ConcurrentHashMap, ConcurrentLinkedQueue}
import java.net.{SocketException, Socket, InetSocketAddress, ServerSocket}
import java.io._
import uk.co.morleydev.ghosthunt.util.using
import scala.concurrent._
import scala.concurrent.duration.Duration
import uk.co.morleydev.ghosthunt.model.net.{game, NetworkMessage, ClientId}
import uk.co.morleydev.ghosthunt.model.GameTime

/**
 * The server maintains it's own thread for dealing with connected clients, sending messages to and from those clients
 * as well as accepting new connections. It is thread-safe, using a concurrent queue to talk between the internal threads
 * and the consuming thread.
 *
 * @param executionContent
 */
class Server(implicit val executionContent : ExecutionContext = ExecutionContext.Implicits.global) extends AutoCloseable {

  private val connectedUsers = new ConcurrentHashMap[ClientId, (ConcurrentLinkedQueue[NetworkMessage], Thread)]()

  private class SocketThread(clientId: ClientId,
                             socket: Socket,
                             outboundMessageQueue: ConcurrentLinkedQueue[NetworkMessage],
                             receivedMessageQueue: ConcurrentLinkedQueue[(ClientId, NetworkMessage)]) extends Thread {
    private val inputStream = socket.getInputStream
    private val outputStream = socket.getOutputStream

    private def processReceivedMessages() {
      Stream.continually(inputStream)
        .takeWhile(_.available() > 0)
        .map(s => new ObjectInputStream(inputStream).readObject().asInstanceOf[NetworkMessage])
        .foreach(msg => receivedMessageQueue.add(clientId, msg))
    }

    private def processOutboundMessages() {
      Iterator.continually(outboundMessageQueue.poll())
        .takeWhile(_ != null)
        .foreach(message => {
        new ObjectOutputStream(outputStream).writeObject(message)
      })
    }

    private def tick(): Unit = {
      val readFuture = future {
        processReceivedMessages()
      }
      val writeFuture = future {
        processOutboundMessages()
      }
      Await.result(Future.sequence(Seq(readFuture, writeFuture)), Duration.Inf)
    }

    override def run(): Unit = {
      try {
        using(socket) {
          socket =>
            while (isConnected) {
              tick()
              Thread.sleep(10)
            }
        }
      } catch {
        case e: Exception =>
          e.printStackTrace()
          receivedMessageQueue.add(clientId, game.Disconnected.apply(new GameTime(Duration(0, duration.SECONDS), Duration(0, duration.SECONDS))))
      }
    }
  }

  private var serverAcceptThread : ServerAcceptThread = null

  private class ServerAcceptThread extends Thread {

    private var port : Int = 80

    def start(port : Int) {
      this.port = port
      start()
    }

    override def run() : Unit = {
      try {
        socketServer.bind(new InetSocketAddress(port))
        mIsConnected = true

        while (mIsConnected) {
          accept()
        }
      } catch {
        case e: IOException => mIsFailed = true
      }

    }

    def accept() {
      try {
        val clientId = new ClientId()
        val clientOutboundQueue = new ConcurrentLinkedQueue[NetworkMessage]()
        val socketThread = new SocketThread(clientId, socketServer.accept(), clientOutboundQueue, receivedMessageQueue)
        connectedUsers.put(clientId, (clientOutboundQueue, socketThread))
        socketThread.start()
      } catch {
        case e: IOException => ()
      }
    }
  }

  private var mIsConnected = false
  private var mIsFailed = false
  private val receivedMessageQueue = new ConcurrentLinkedQueue[(ClientId, NetworkMessage)]()
  private val socketServer = new ServerSocket()

  def listen(port: Int): Unit = {
    close()
    serverAcceptThread = new ServerAcceptThread()
    serverAcceptThread.start(port)
  }

  def close(): Unit = {
    if (mIsConnected) {
      mIsConnected = false
      socketServer.close()
      serverAcceptThread.join()
      JavaConversions.iterableAsScalaIterable(connectedUsers.entrySet()).foreach(e => e.getValue._2.join())
    }
    mIsFailed = false
  }

  def receive(): GenSeq[(ClientId, NetworkMessage)] = {
    Iterator.continually(receivedMessageQueue.poll())
      .takeWhile(_ != null)
      .toList
  }

  def send(clientId: ClientId, message: NetworkMessage): Unit =
    JavaConversions.iterableAsScalaIterable(connectedUsers.entrySet())
      .filter(f => f.getKey == clientId)
      .foreach(e => e.getValue._1.add(message))

  def isConnected: Boolean =
    mIsConnected

  def isFailed: Boolean =
    mIsFailed
}
