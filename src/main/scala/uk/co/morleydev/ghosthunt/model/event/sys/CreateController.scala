package uk.co.morleydev.ghosthunt.model.event.sys

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.event.Event

class CreateController(controller : Controller) extends Event("CreateController", controller)
