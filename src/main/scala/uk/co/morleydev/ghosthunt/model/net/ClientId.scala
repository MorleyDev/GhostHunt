package uk.co.morleydev.ghosthunt.model.net

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The client id is a UUID unique id for the client to use with tracking what clients are connected and sending messages,
 * as well as specifying what client to send a message to and what remote user a message refers to.
 *
 * @param value
 */
class ClientId(@JsonProperty("remote") val value : UUID = UUID.randomUUID()) {
  override def toString : String = "[Client:%s]".format(value.toString)

  override def equals(other : Any) : Boolean = {
    other match {
      case otherClient : ClientId => this.value == otherClient.value
      case _ => false
    }
  }
}
