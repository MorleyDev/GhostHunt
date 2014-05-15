package uk.co.morleydev.ghosthunt.model

import scala.concurrent.duration.Duration

/**
 * The game time class stores the total time the game has been running, as well as the change in time since
 * the previous frame.
 *
 * @param deltaTime
 * @param totalTime
 */
case class GameTime(deltaTime : Duration, totalTime : Duration)