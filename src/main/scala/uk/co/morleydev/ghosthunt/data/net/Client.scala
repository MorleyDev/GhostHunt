package uk.co.morleydev.ghosthunt.data.net

import scala.collection.GenSeq
import scala.concurrent.{Future, Await, future}
import java.net.{Socket, InetSocketAddress}
import java.util.concurrent.ConcurrentLinkedQueue
import java.io.{IOException, ObjectOutputStream, ObjectInputStream}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext
import uk.co.morleydev.ghosthunt.model.net.NetworkMessage

/**
 * The server maintains it's own thread for dealing with connecting to the server,
 * sending messages to and from the server. It is thread-safe, using a concurrent queue to talk between the
 * internal thread and the consuming thread.
 *
 * @param executionContent
 */
class Client(implicit val executionContent : ExecutionContext = ExecutionContext.Implicits.global) extends AutoCloseable {

  private class SocketThread(socket : Socket) extends Thread {
    private var host : String = "localhost"
    private var port : Int = 80

    def start(host : String, port : Int) : Unit = {
      this.host = host
      this.port = port
      start()
    }

    override def run(): Unit = {
      try {
        socket.connect(new InetSocketAddress(host, port))
        mIsConnected = true

        while (mIsConnected) {
          tick()
          Thread.sleep(10)
        }
      } catch {
        case e : IOException => mIsFailedConnect = true
      }
    }

    private def processReceivedMessages() {
      Stream.continually(socket.getInputStream)
        .takeWhile(_.available() > 0)
        .map(s => new ObjectInputStream(socket.getInputStream).readObject().asInstanceOf[NetworkMessage])
        .foreach(receivedMessageQueue.add)
    }

    private def processOutboundMessages() {
      Iterator.continually(outboundMessageQueue.poll())
        .takeWhile(_ != null)
        .foreach(message => new ObjectOutputStream(socket.getOutputStream).writeObject(message))
    }

    def tick() {
      val readFuture = future {
        processReceivedMessages()
      }
      val writeFuture = future {
        processOutboundMessages()
      }
      Await.result(Future.sequence(Seq(readFuture, writeFuture)), Duration.Inf)
    }
  }

  private var mIsFailedConnect = false
  private var mIsConnected = false
  private val receivedMessageQueue = new ConcurrentLinkedQueue[NetworkMessage]()
  private val outboundMessageQueue = new ConcurrentLinkedQueue[NetworkMessage]()
  private val jsocket = new java.net.Socket()

  private var networkThread : SocketThread = null

  def connect(host : String, port : Int) : Unit = {
    close()
    networkThread = new SocketThread(jsocket)
    networkThread.start(host, port)
  }

  def close() : Unit = {
    if (isConnected) {
      mIsConnected = false
      networkThread.join()
      jsocket.close()
    }
    mIsFailedConnect = false
  }

  def receive() : GenSeq[NetworkMessage] =
    Iterator.continually(receivedMessageQueue.poll())
      .takeWhile(_ != null)
      .toList


  def send(message : NetworkMessage) : Unit =
    outboundMessageQueue.add(message)

  def isConnected : Boolean =
    mIsConnected

  def isFailed : Boolean =
    mIsFailedConnect
}
