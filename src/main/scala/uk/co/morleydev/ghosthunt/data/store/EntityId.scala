package uk.co.morleydev.ghosthunt.data.store

import java.util.UUID

class EntityId(val value : UUID = UUID.randomUUID()) {
  override def toString : String = "[Entity:%s]".format(value.toString)

  override def equals(other : Any) : Boolean = {
    other match {
      case otherEntity : EntityId => this.value == otherEntity.value
      case _ => false
    }
  }
}
