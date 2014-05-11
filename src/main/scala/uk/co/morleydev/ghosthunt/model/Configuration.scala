package uk.co.morleydev.ghosthunt.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.{PrintWriter, File}
import uk.co.morleydev.ghosthunt.util.using
import scala.io.Source
import com.lambdaworks.jacks.JacksMapper
import org.jsfml.window.Keyboard.Key

case class Configuration(@JsonProperty("window-width") width : Int = 640,
                         @JsonProperty("window-height") height : Int = 480,
                         @JsonProperty("fullscreen") fullscreen : Boolean = false,
                         @JsonProperty("input") input : InputConfiguration = new InputConfiguration())

object Configuration {
  def load(filename : String) : Option[Configuration] = {
    val configFile = new File("config.json")
    if (configFile.exists())
      using(Source.fromFile(configFile)) {
        source =>
          try {
            Some(JacksMapper.readValue[Configuration](source.reader()))
          } catch {
            case e: Exception => None
          }
      }
    else
      None
  }

  def write(filename : String, config: Configuration) = {
      using(new PrintWriter("config.json")) {
        writer => JacksMapper.writeValue(writer, config)
      }
  }

  def loadOrWrite(filename : String) : Configuration = {
    load(filename) match {
      case config : Some[Configuration] =>
        config.get

      case None => val config = new Configuration()
        write(filename, config)
        config
    }
  }
}
