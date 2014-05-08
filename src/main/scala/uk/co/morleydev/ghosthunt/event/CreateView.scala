package uk.co.morleydev.ghosthunt.event

import uk.co.morleydev.ghosthunt.data.event.Event
import uk.co.morleydev.ghosthunt.view.View

class CreateView(view : View) extends Event("CreateView", view)
