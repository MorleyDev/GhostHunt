package uk.co.morleydev.ghosthunt.model.event

/**
 * An event is a local in-process message that gets added to a queue and processed every frame. The event has a name
 * that identifies it, and optionally some data that is retrieved from the message in order to allow for information and
 * data to be passed around the system
 *
 * @param name
 * @param data
 */
class Event(val name : String, val data : Any = null) {
  override def equals(obj: Any): Boolean =
    obj match {
      case e: Event => name == e.name && data == e.data
      case _ => false
    }

  override def toString: String =
    if (data == null) "[Event:%s]".format(name)
    else "[Event:%s|%s]".format(name, data)
}
