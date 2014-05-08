package uk.co.morleydev.ghosthunt.data.net

import scala.collection.GenSeq
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentLinkedQueue
import java.io.{IOException, ObjectOutputStream, ObjectInputStream}

class Client extends AutoCloseable {

  private lazy val networkThread = new Thread(new Runnable {
    override def run(): Unit = {
      val input = jsocket.getInputStream
      val output = jsocket.getOutputStream
      while (mIsConnected) {

        if (input.available() > 0) {
          receivedMessageQueue.add(new ObjectInputStream(input).readObject().asInstanceOf[NetworkMessage])
        }

        Iterator.continually(outboundMessageQueue.poll())
          .takeWhile(_ != null)
          .foreach(message => {
          new ObjectOutputStream(output).writeObject(message)
        })

        Thread.sleep(10)
      }
    }
  })

  private var mIsConnected = false
  private val receivedMessageQueue = new ConcurrentLinkedQueue[NetworkMessage]()
  private val outboundMessageQueue = new ConcurrentLinkedQueue[NetworkMessage]()
  private val jsocket = new java.net.Socket()

  def connect(host : String, port : Int) : Unit = {
    try {
      jsocket.connect(new InetSocketAddress(host, port))
      mIsConnected = true
      networkThread.start()
    } catch {
      case e : IOException => ()
    }
  }

  def close() : Unit = {
    if (isConnected) {
      mIsConnected = false
      networkThread.join()
      jsocket.close()
    }
  }

  def receive() : GenSeq[NetworkMessage] = {
    Iterator.continually(receivedMessageQueue.poll())
      .takeWhile(_ != null)
      .toList
  }

  def send(message : NetworkMessage) : Unit = {
    outboundMessageQueue.add(message)
  }

  def isConnected : Boolean =
    mIsConnected
}
