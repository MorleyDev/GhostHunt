package uk.co.morleydev.ghosthunt.test.data.store

import org.scalatest.FunSpec
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.model.store.EntityId

class EntityComponentStoreTests extends FunSpec {

  describe("Given an Entity Component Store with entities") {

    val entityComponentStore = new EntityComponentStore()

    val emptyEntity = entityComponentStore.createEntity()

    val contentEntity = entityComponentStore.createEntity()
    entityComponentStore.link(contentEntity, "floater", 124.0f)
    val expectedContentEntityComponents = Map[String, Any]("piano" -> 10, "floater" -> 15.0f, "fear" -> 15.0f)
    expectedContentEntityComponents.foreach(s => entityComponentStore.link(contentEntity, s._1, s._2))

    val otherContentEntity = entityComponentStore.createEntity()
    val expectedOtherContentEntityComponents = Map[String, Any]("piano" -> 10, "floater" -> 15.0f)
    expectedOtherContentEntityComponents.foreach(s => entityComponentStore.link(otherContentEntity, s._1, s._2))

    val ignoredContentEntity = entityComponentStore.createEntity()
    Map[String, Any]("piano" -> 10,
      "floater" -> 45.0f,
      "fear" -> 15.0f).foreach(s => entityComponentStore.link(ignoredContentEntity, s._1, s._2))
    entityComponentStore.unlink(ignoredContentEntity, "floater")

    val removedContentEntity = entityComponentStore.createEntity()
    val expectedRemovedContentEntityComponents = Map[String, Any]("piano" -> 10, "floater" -> 15.0f)
    expectedRemovedContentEntityComponents.foreach(s => entityComponentStore.link(removedContentEntity, s._1, s._2))
    entityComponentStore.removeEntity(removedContentEntity)

    describe("When getting entities with specific components") {
      val ec = entityComponentStore.get("piano", "floater")
      it("Then the expected set was returned") {
        assert(ec == Map[EntityId, Map[String, Any]](contentEntity -> expectedContentEntityComponents,
                                                 otherContentEntity -> expectedOtherContentEntityComponents))
      }
    }
  }
}
