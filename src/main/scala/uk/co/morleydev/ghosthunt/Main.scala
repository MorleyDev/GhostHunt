package uk.co.morleydev.ghosthunt

import uk.co.morleydev.ghosthunt.model.Configuration

object Main {

  def main(args : Array[String]) {
    val config = Configuration.loadOrWrite("config.json")

    val threads = Iterator.continually(new Thread(new Runnable {
      override def run(): Unit = {
        val game = new Game(config)
        game.run()
      }
    })).take(3)

    threads.foreach(_.start())
    threads.foreach(_.join())
  }
}
