package uk.co.morleydev.ghosthunt

import org.jsfml.graphics.Color
import uk.co.morleydev.ghosthunt.model.{GameTime, Configuration}
import scala.concurrent.duration.Duration
import scala.collection.JavaConversions
import org.jsfml.window.event.Event
import uk.co.morleydev.ghosthunt.sfml.{SfmlWindowEvents, SfmlFactory}
import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.data.event.EventQueue
import uk.co.morleydev.ghosthunt.data.net.{Server, Client}
import uk.co.morleydev.ghosthunt.view.View
import uk.co.morleydev.ghosthunt.data.file.ContentFactoryFromFileSystem
import scala.concurrent._
import scala.concurrent.duration
import scala.concurrent.ExecutionContext.Implicits.global
import uk.co.morleydev.ghosthunt.util.Killable

class Game(config : Configuration) extends Killable {

  private val renderWindow = SfmlFactory.create(config.fullscreen, config.width, config.height)
  private val windowEvents = new SfmlWindowEvents()

  private val contentFactory = new ContentFactoryFromFileSystem()
  private val events = new EventQueue()
  private val client = new Client()
  private val server = new Server()

  private val controllers = new ControllerStore()
  private val views = new ViewStore()

  private var gameRunningTime = Duration(0, duration.NANOSECONDS)

  def pollSystem() : Unit = {
    JavaConversions.iterableAsScalaIterable[Event](renderWindow.pollEvents()).foreach(e => windowEvents.invoke(e))
  }

  def update(dt: Duration): Unit = {
    gameRunningTime = Duration((gameRunningTime + dt).toNanos, duration.NANOSECONDS)
    val gameTime = new GameTime(dt, gameRunningTime)
    controllers.update(gameTime)

    events.dequeue().par.foreach(e => {
      e.name match {
        case "CreateController" =>
          controllers.add(e.data.asInstanceOf[Controller])

        case "CreateView" =>
          views.add(e.data.asInstanceOf[View])

        case _ =>
          controllers.onEvent(e, gameTime)
          views.onEvent(e)
      }
    })
    client.receive().par.foreach(m => controllers.onClientMessage(m, gameTime))
    server.receive().par.foreach(m => controllers.onServerMessage(m._1, m._2, gameTime))
  }

  def draw(): Unit = {
    renderWindow.clear(Color.BLACK)
    views.draw(renderWindow)
    renderWindow.display()
  }

  def onStart(): Unit = {
    windowEvents.onClosed(kill)
  }

  def onEnd(): Unit = {
    client.close()
    server.close()
  }

  def run() {
    def sleepToNextOperation(nextUpdateTime: Long, nextRenderTime: Long, currentTime: Long) {
      val timeToNextOperation = Duration(Math.min(nextUpdateTime, nextRenderTime) - currentTime, duration.NANOSECONDS)
      if (timeToNextOperation > Duration(0, duration.NANOSECONDS))
        Thread.sleep(timeToNextOperation.toMillis)
    }

    onStart()
    try {
      val UPDATE_LENGTH = Duration(10, duration.MILLISECONDS)
      val RENDER_LENGTH = Duration(3333333L, duration.NANOSECONDS)

      var lastUpdate = System.nanoTime()
      var lastDraw = System.nanoTime() - RENDER_LENGTH.toNanos

      while (isAlive) {
        pollSystem()

        val currentTime = System.nanoTime()
        sleepToNextOperation(lastUpdate + UPDATE_LENGTH.toNanos, lastDraw + RENDER_LENGTH.toNanos, currentTime)

        val updateFuture = future {
          while (isAlive && Duration(currentTime - lastUpdate, duration.NANOSECONDS) >= UPDATE_LENGTH) {
            lastUpdate += UPDATE_LENGTH.toNanos
            update(UPDATE_LENGTH)
          }
        }

        if (Duration(currentTime - lastDraw, duration.NANOSECONDS) >= RENDER_LENGTH) {
          draw()
          lastDraw = currentTime
        }

        Await.result(updateFuture, Duration.Inf)
      }
    } finally {
      onEnd()
    }
  }
}
