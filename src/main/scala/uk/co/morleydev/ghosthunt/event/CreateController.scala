package uk.co.morleydev.ghosthunt.event

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.data.event.Event

class CreateController(controller : Controller) extends Event("CreateController", controller)
