package uk.co.morleydev.ghosthunt.data.net

import scala.collection.{JavaConversions, GenSeq}
import java.util.concurrent.{ConcurrentHashMap, ConcurrentLinkedQueue}
import java.net.{Socket, InetSocketAddress, ServerSocket}
import java.io._

class Server extends AutoCloseable {

  private val connectedUsers = new ConcurrentHashMap[ClientId, (ConcurrentLinkedQueue[NetworkMessage], Thread)]()

  private def createSocketThread(clientId : ClientId, socket : Socket, outboundMessageQueue : ConcurrentLinkedQueue[NetworkMessage]) : Thread =
    new Thread(new Runnable{
      override def run(): Unit = {
        try {
          val input = socket.getInputStream
          val output = socket.getOutputStream
          try {
            while (mIsConnected) {
              if (input.available() > 0) {
                val message = new ObjectInputStream(input).readObject().asInstanceOf[NetworkMessage]
                receivedMessageQueue.add((clientId, message))
              }

              Iterator.continually(outboundMessageQueue.poll())
                .takeWhile(_ != null)
                .foreach(message => {
                new ObjectOutputStream(output).writeObject(message)
              })

              Thread.sleep(10)
            }
          } finally {
            socket.close()
          }
        } catch{
          case e : Exception => e.printStackTrace()
        }
      }
    })

  private lazy val serverAcceptThread = new Thread(new Runnable {
    override def run(): Unit = {
      while(mIsConnected) {
        try {
          val clientId = new ClientId()
          val clientOutboundQueue = new ConcurrentLinkedQueue[NetworkMessage]()
          val socketThread = createSocketThread(clientId, socketServer.accept(), clientOutboundQueue)
          socketThread.start()
          connectedUsers.put(clientId, (clientOutboundQueue, socketThread))
        } catch {
          case e : IOException => ()
        }
      }
    }
  })

  private var mIsConnected = false
  private val receivedMessageQueue = new ConcurrentLinkedQueue[(ClientId, NetworkMessage)]()
  private val socketServer = new ServerSocket()

  def listen(port : Int) : Unit = {
    try {
      socketServer.bind(new InetSocketAddress(port))
      mIsConnected = true
      serverAcceptThread.start()
    } catch {
      case e : IOException => ()
    }
  }

  def close() : Unit = {
    if (mIsConnected) {
      mIsConnected = false
      serverAcceptThread.join()
      JavaConversions.iterableAsScalaIterable(connectedUsers.entrySet()).foreach(e => e.getValue._2.join())
      socketServer.close()
    }
  }

  def receive() : GenSeq[(ClientId, NetworkMessage)] = {
    Iterator.continually(receivedMessageQueue.poll())
      .takeWhile(_ != null)
      .toList
  }

  def broadcast(message: NetworkMessage) : Unit = {
    JavaConversions.iterableAsScalaIterable(connectedUsers.entrySet()).foreach(e => e.getValue._1.add(message))
  }

  def isConnected : Boolean =
    mIsConnected
}
