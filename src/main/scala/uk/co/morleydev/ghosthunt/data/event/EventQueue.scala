package uk.co.morleydev.ghosthunt.data.event

import java.util.concurrent.ConcurrentLinkedQueue
import scala.collection.GenSeq

class EventQueue {
  private val queue = new ConcurrentLinkedQueue[Event]()

  def enqueue(event : Event) : Unit =
    queue.add(event)

  def dequeue() : GenSeq[Event] =
    Iterator.continually(queue.poll())
      .takeWhile(_ != null)
      .toSeq
}
