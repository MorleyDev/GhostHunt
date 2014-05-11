package uk.co.morleydev.ghosthunt.test

import org.scalatest.FunSpec
import org.mockito.{Matchers, Mockito}
import org.scalatest.mock.MockitoSugar
import uk.co.morleydev.ghosthunt.view.View
import org.jsfml.graphics.RenderTarget
import uk.co.morleydev.ghosthunt.ViewStore
import uk.co.morleydev.ghosthunt.model.event.Event

class ViewStoreTests extends FunSpec with MockitoSugar {
  describe("Given a store of views with added views") {

    val store = new ViewStore()

    val mockAliveViews = Seq[View](mock[View], mock[View], mock[View])
    val mockDeadViews = Seq[View](mock[View], mock[View], mock[View])

    (mockAliveViews ++ mockDeadViews).foreach(c => store.add(c))
    mockAliveViews.foreach(c => Mockito.when(c.isAlive).thenReturn(true))
    mockDeadViews.foreach(c => Mockito.when(c.isAlive).thenReturn(false))

    describe("When drawing the views") {
      val mockRenderTarget = mock[RenderTarget]
      store.draw(mockRenderTarget)

      it("Then the expected views were updated") {
        mockAliveViews.foreach(c => Mockito.verify(c).draw(mockRenderTarget))
      }
      it("Then the dead views were not updated") {
        mockDeadViews.foreach(c => Mockito.verify(c, Mockito.never()).draw(Matchers.any[RenderTarget]))
      }
    }

    describe("When an event occurs") {
      class someEvent(name : String, value : Any) extends Event(name, value)
      val event = new someEvent("some", 123)
      store.onEvent(event)

      it("Then the expected views has events passed to them") {
        mockAliveViews.foreach(c => Mockito.verify(c).handleEvent(event))
      }
      it("Then the dead views were not given events") {
        mockDeadViews.foreach(c => Mockito.verify(c, Mockito.never()).handleEvent(event))
      }
    }
  }
}
