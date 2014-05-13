package uk.co.morleydev.ghosthunt.model.net

import scala.concurrent.duration.Duration

case class AcceptJoinGameRequest(id : Int, clientTime : Duration)
