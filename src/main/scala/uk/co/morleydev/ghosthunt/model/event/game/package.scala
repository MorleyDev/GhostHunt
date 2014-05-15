package uk.co.morleydev.ghosthunt.model.event

package object game {
  class BaseCreateEventWithParam[T](final val name : String) {
    def apply(data : T) : Event =
      new Event(name, data)
  }

  val EnableLocalActors = new Event("EnableLocalActors")
  val DisableLocalActors = new Event("DisableLocalActors")

  val ShowScore = new Event("ShowScore")
  val HideScore = new Event("HideScore")
}
