package uk.co.morleydev.ghosthunt.model.net

import scala.concurrent.duration.Duration
import uk.co.morleydev.ghosthunt.model.GameTime
import com.lambdaworks.jacks.JacksMapper


/**
 * The network message is used to send data over the network and communicate between clients and servers.
 * It records the name of the message, the data inside the message (JSON-serialized) and the time that message
 * was sent in GameTime
 */
case class NetworkMessage(name : String, data : String, time : Duration) extends Serializable

class NullNetworkMessage

object NetworkMessage {
  def apply[E: Manifest] (name : String, data : E, time : GameTime) : NetworkMessage =
    new NetworkMessage(name, JacksMapper.writeValueAsString(data), time.totalTime)

  def apply(name : String, time : GameTime) : NetworkMessage =
    new NetworkMessage(name, JacksMapper.writeValueAsString(new NullNetworkMessage()), time.totalTime)

  def extract[E: Manifest](msg : NetworkMessage) : E =
    JacksMapper.readValue[E](msg.data)

}
