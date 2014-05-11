package uk.co.morleydev.ghosthunt.data.net

import scala.collection.GenSeq
import scala.concurrent.{Future, Await, future}
import java.net.{Socket, InetSocketAddress}
import java.util.concurrent.ConcurrentLinkedQueue
import java.io.{IOException, ObjectOutputStream, ObjectInputStream}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext
import uk.co.morleydev.ghosthunt.model.net.NetworkMessage

class Client(implicit val executionContent : ExecutionContext = ExecutionContext.Implicits.global) extends AutoCloseable {

  private class SocketThread(socket : Socket) extends Thread {
    override def run(): Unit = {
      while (mIsConnected) {
        tick()
        Thread.sleep(10)
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

  private var mIsConnected = false
  private val receivedMessageQueue = new ConcurrentLinkedQueue[NetworkMessage]()
  private val outboundMessageQueue = new ConcurrentLinkedQueue[NetworkMessage]()
  private val jsocket = new java.net.Socket()

  private lazy val networkThread = new SocketThread(jsocket)

  def connect(host : String, port : Int) : Unit =
    try {
      jsocket.connect(new InetSocketAddress(host, port))
      mIsConnected = true
      networkThread.start()
    } catch {
      case e : IOException => ()
    }

  def close() : Unit =
    if (isConnected) {
      mIsConnected = false
      networkThread.join()
      jsocket.close()
    }

  def receive() : GenSeq[NetworkMessage] =
    Iterator.continually(receivedMessageQueue.poll())
      .takeWhile(_ != null)
      .toList


  def send(message : NetworkMessage) : Unit =
    outboundMessageQueue.add(message)

  def isConnected : Boolean =
    mIsConnected
}
