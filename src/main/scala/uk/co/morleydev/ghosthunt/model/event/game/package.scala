package uk.co.morleydev.ghosthunt.model.event

package object game {
  class BaseCreateEventWithParam[T](final val name : String) {
    def apply(data : T) : Event =
      new Event(name, data)
  }
}
