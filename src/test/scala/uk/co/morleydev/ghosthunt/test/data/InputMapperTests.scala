package uk.co.morleydev.ghosthunt.test.data

import org.mockito.{Matchers, Mockito}
import org.scalatest.mock.MockitoSugar
import org.scalatest.FunSpec
import org.jsfml.window.Keyboard.Key
import uk.co.morleydev.ghosthunt.model.event.Event
import uk.co.morleydev.ghosthunt.model.InputConfiguration
import uk.co.morleydev.ghosthunt.data.InputMapper
import uk.co.morleydev.ghosthunt.data.event.EventQueue

class InputMapperTests extends FunSpec with MockitoSugar {
  val inputConfig = new InputConfiguration()
  val expectedMap = Map(
    Key.valueOf(inputConfig.up) -> new Event("MoveLocalUp"),
    Key.valueOf(inputConfig.down) -> new Event("MoveLocalDown"),
    Key.valueOf(inputConfig.left) -> new Event("MoveLocalLeft"),
    Key.valueOf(inputConfig.right) -> new Event("MoveLocalRight"),
    Key.valueOf(inputConfig.activate) -> new Event("Activate"),
    Key.valueOf(inputConfig.cancel) -> new Event("Cancel"))

  expectedMap.foreach(inOut => {
    describe("Given an input mapper and input config") {
      val mockEventQueue = mock[EventQueue]
      val inputMapper = new InputMapper(inputConfig, mockEventQueue)

      describe("When key mapping to " + inOut._2.name + " is pressed") {
        inputMapper.apply(inOut._1, isPressed = true)
        it("Then the expected item is queued") {
          Mockito.verify(mockEventQueue).enqueue(inOut._2)
        }
      }
    }

    describe("Given an input mapper and input config") {
      val mockEventQueue = mock[EventQueue]
      val inputMapper = new InputMapper(inputConfig, mockEventQueue)
      describe("When key mapping to " + inOut._2.name + " is released") {
        inputMapper.apply(inOut._1, isPressed = false)
        it("Then the expected item is not queued") {
          Mockito.verify(mockEventQueue, Mockito.never()).enqueue(Matchers.any[Event]())
        }
      }
    }

  })
}
