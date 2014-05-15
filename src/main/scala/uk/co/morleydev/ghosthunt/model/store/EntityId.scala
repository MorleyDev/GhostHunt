package uk.co.morleydev.ghosthunt.model.store

import java.util.UUID

/**
 * An entity id represents an entity stored in the Entity-Component store, via a UUID that is guaranteed to be unique
 * to that entity.
 * 
 * @param value
 */
case class EntityId(value : UUID = UUID.randomUUID()) {
  override def toString : String = "[Entity:%s]".format(value.toString)

  override def equals(other : Any) : Boolean = {
    other match {
      case otherEntity : EntityId => this.value == otherEntity.value
      case _ => false
    }
  }
}
