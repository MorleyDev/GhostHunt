package uk.co.morleydev.ghosthunt.model.net

import scala.concurrent.duration.Duration
import uk.co.morleydev.ghosthunt.model.GameTime
import com.lambdaworks.jacks.JacksMapper

case class NetworkMessage(name : String, data : String, time : Duration) extends Serializable

object NetworkMessage {
  def apply[E: Manifest] (name : String, data : E, time : GameTime) : NetworkMessage =
    new NetworkMessage(name, JacksMapper.writeValueAsString(data), time.totalTime)

  def extract[E: Manifest](msg : NetworkMessage) : E =
    JacksMapper.readValue[E](msg.data)
}
