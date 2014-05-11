package uk.co.morleydev.ghosthunt.model.event.sys

import uk.co.morleydev.ghosthunt.view.View
import uk.co.morleydev.ghosthunt.model.event.Event

class CreateView(view : View) extends Event("CreateView", view)
