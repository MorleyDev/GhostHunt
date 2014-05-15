package uk.co.morleydev.ghosthunt

import uk.co.morleydev.ghosthunt.model.Configuration

object Main {

  def main(args: Array[String]) {
    val config = Configuration.loadOrWrite("config.json")

    val game = new Game(config)
    game.run()

  }
}
