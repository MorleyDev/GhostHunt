package uk.co.morleydev.ghosthunt.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.jsfml.window.Keyboard.Key

case class InputConfiguration(@JsonProperty("up") up : String = Key.W.name(),
                              @JsonProperty("down") down : String = Key.S.name(),
                              @JsonProperty("left") left : String = Key.A.name(),
                              @JsonProperty("right") right : String = Key.A.name(),
                              @JsonProperty("activate") activate : String = Key.RETURN.name(),
                              @JsonProperty("cancel") cancel : String = Key.ESCAPE.name())
