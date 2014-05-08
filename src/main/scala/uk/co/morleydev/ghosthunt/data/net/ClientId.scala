package uk.co.morleydev.ghosthunt.data.net

import java.util.UUID

class ClientId(val value : UUID = UUID.randomUUID()) {
  override def toString : String = "[Client:%s]".format(value.toString)

  override def equals(other : Any) : Boolean = {
    other match {
      case otherClient : ClientId => this.value == otherClient.value
      case _ => false
    }
  }
}
