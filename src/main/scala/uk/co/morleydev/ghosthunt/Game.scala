package uk.co.morleydev.ghosthunt

import org.jsfml.graphics.Color
import uk.co.morleydev.ghosthunt.model.{GameTime, Configuration}
import scala.concurrent.duration.Duration
import scala.collection.JavaConversions
import org.jsfml.window.event.Event
import uk.co.morleydev.ghosthunt.data.sfml.{SfmlWindowEvents, SfmlFactory}
import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.data.event.EventQueue
import uk.co.morleydev.ghosthunt.data.net.{Server, Client}
import uk.co.morleydev.ghosthunt.view.View
import uk.co.morleydev.ghosthunt.data.file.ContentFactoryFromFileSystem
import scala.concurrent._
import scala.concurrent.duration
import scala.concurrent.ExecutionContext.Implicits.global
import uk.co.morleydev.ghosthunt.util.Killable
import uk.co.morleydev.ghosthunt.data.InputMapper
import uk.co.morleydev.ghosthunt.model.event
import uk.co.morleydev.ghosthunt.controller.impl.{TitleScreenController, TextBoxController, MenuOptionController}
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.view.impl.{TextView, TextBoxView, MenuOptionView}

class Game(config : Configuration) extends Killable {

  private val renderWindow = SfmlFactory.create(config.fullscreen, config.width, config.height)
  private val windowEvents = new SfmlWindowEvents(renderWindow)

  private val entities = new EntityComponentStore()
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
        case event.sys.CreateController.name =>
          controllers.add(e.data.asInstanceOf[Controller])

        case event.sys.CreateView.name =>
          views.add(e.data.asInstanceOf[View])

        case event.sys.UpdateGameRunningTime.name =>
          gameRunningTime = Duration(e.data.asInstanceOf[Duration].toNanos, duration.NANOSECONDS)

        case event.sys.ConnectToServer.name =>
          val hostPort = e.data.asInstanceOf[(String, Int)]
          client.connect(hostPort._1, hostPort._2)
          events.enqueue(event.sys.CheckConnectedToServer)

        case event.sys.CheckConnectedToServer =>
          if (client.isConnected)
            events.enqueue(event.sys.ConnectedToServer)
          else if (client.isFailed)
            events.enqueue(event.sys.FailedConnectToServer)
          else
            events.enqueue(event.sys.CheckConnectedToServer)

        case event.sys.HostServer.name =>
          val port = e.data.asInstanceOf[Int]
          server.listen(port)
          events.enqueue(event.sys.CheckServerHosting)

        case event.sys.CheckServerHosting.name =>
          if (server.isConnected)
            events.enqueue(event.sys.ServerHosting)
          else if (server.isFailed)
            events.enqueue(event.sys.FailedHostServer)
          else
            events.enqueue(event.sys.CheckServerHosting)

        case event.sys.CloseGame.name =>
          kill()

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

    val inputMapper = new InputMapper(config.input, events)
    windowEvents.onKey(inputMapper.apply)
    windowEvents.onMouse(inputMapper.apply)
    windowEvents.onText(inputMapper.apply)

    controllers.add(new MenuOptionController(entities))
    controllers.add(new TextBoxController(entities))
    views.add(new MenuOptionView(entities, contentFactory))
    views.add(new TextBoxView(entities, contentFactory))
    views.add(new TextView(entities, contentFactory))

    controllers.add(new TitleScreenController(events, entities))
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

        var counter = 0
        while (counter < 10 && isAlive && Duration(currentTime - lastUpdate, duration.NANOSECONDS) >= UPDATE_LENGTH) {
          counter = counter + 1
          lastUpdate += UPDATE_LENGTH.toNanos
          update(UPDATE_LENGTH)
        }

        if (Duration(currentTime - lastDraw, duration.NANOSECONDS) >= RENDER_LENGTH) {
          draw()
          lastDraw = currentTime
        }
      }
    } finally {
      onEnd()
    }
  }
}
