package uk.co.morleydev.ghosthunt.test.data.event

import org.scalatest.FunSpec
import uk.co.morleydev.ghosthunt.data.event.EventQueue
import org.scalatest.mock.MockitoSugar
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import uk.co.morleydev.ghosthunt.model.event.Event

class EventQueueTests extends FunSpec with MockitoSugar {

  val eventQueue = new EventQueue()
  class someEvent(name : String, value : Any) extends Event(name, value)
  val expectedQueue = Seq[Event](new someEvent("data1", 10),
    new someEvent("data2", 10),
    new someEvent("data1", 10),
    new someEvent("daeta1", "ead"),
    new someEvent("da2a1", 12.0f))

  describe("Given an event queue with queued data") {
    expectedQueue.foreach(event => eventQueue.enqueue(event))

    describe("When dequeue the data") {
      val dequeue = eventQueue.dequeue()

      it("Then the expected data was returned by the dequeue") {
        assert(expectedQueue == dequeue)
      }
      it("Then a second dequeue gives empty data") {
        assert(eventQueue.dequeue().isEmpty)
      }
    }
  }
}
