package uk.co.morleydev.ghosthunt.model.event

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
