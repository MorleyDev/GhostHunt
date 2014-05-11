package uk.co.morleydev.ghosthunt.test

import org.scalatest.FunSpec
import uk.co.morleydev.ghosthunt.controller.Controller
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import scala.concurrent.duration.Duration
import scala.concurrent.duration.SECONDS
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.ControllerStore
import uk.co.morleydev.ghosthunt.model.event.Event
import uk.co.morleydev.ghosthunt.model.net.NetworkMessage

class ControllerStoreTests extends FunSpec with MockitoSugar {
  val duration = new GameTime(Duration(1, SECONDS), Duration(10, SECONDS))

  describe("Given a store of controllers with added controllers") {

    val store = new ControllerStore()

    val mockAliveControllers = Seq[Controller](mock[Controller], mock[Controller], mock[Controller])
    val mockDeadControllers = Seq[Controller](mock[Controller], mock[Controller], mock[Controller])

    (mockAliveControllers ++ mockDeadControllers).foreach(c => store.add(c))
    mockAliveControllers.foreach(c => Mockito.when(c.isAlive).thenReturn(true))
    mockDeadControllers.foreach(c => Mockito.when(c.isAlive).thenReturn(false))

    describe("When updating the controllers") {
      store.update(duration)

      it("Then the expected controllers were updated") {
        mockAliveControllers.foreach(c => Mockito.verify(c).update(duration))
      }
      it("Then the dead controllers were not updated") {
        mockDeadControllers.foreach(c => Mockito.verify(c, Mockito.never()).update(duration))
      }
    }

    describe("When an event occurs") {
      class someEvent(name : String, value : Any) extends Event(name, value)
      val event = new someEvent("some", "123")
      store.onEvent(event, duration)

      it("Then the expected controllers has events passed to them") {
        mockAliveControllers.foreach(c => Mockito.verify(c).handleEvent(event, duration))
      }
      it("Then the dead controllers were not given events") {
        mockDeadControllers.foreach(c => Mockito.verify(c, Mockito.never()).handleEvent(event, duration))
      }
    }

    describe("When a network message occurs") {
      val message = new NetworkMessage("some", "123", Duration(1, SECONDS))
      store.onClientMessage(message, duration)

      it("Then the expected controllers has events passed to them") {
        mockAliveControllers.foreach(c => Mockito.verify(c).handleClientMessage(message, duration))
      }
      it("Then the dead controllers were not given events") {
        mockDeadControllers.foreach(c => Mockito.verify(c, Mockito.never()).handleClientMessage(message, duration))
      }
    }
  }
}
