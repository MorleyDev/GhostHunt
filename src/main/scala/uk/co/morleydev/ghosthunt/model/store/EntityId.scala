package uk.co.morleydev.ghosthunt.model.store

import java.util.UUID

case class EntityId(value : UUID = UUID.randomUUID()) {
  override def toString : String = "[Entity:%s]".format(value.toString)

  override def equals(other : Any) : Boolean = {
    other match {
      case otherEntity : EntityId => this.value == otherEntity.value
      case _ => false
    }
  }
}