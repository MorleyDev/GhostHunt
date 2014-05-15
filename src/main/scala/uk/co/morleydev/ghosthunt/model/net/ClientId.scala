package uk.co.morleydev.ghosthunt.model.net

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonProperty

class ClientId(@JsonProperty("remote") val value : UUID = UUID.randomUUID()) {
  override def toString : String = "[Client:%s]".format(value.toString)

  override def equals(other : Any) : Boolean = {
    other match {
      case otherClient : ClientId => this.value == otherClient.value
      case _ => false
    }
  }
}
