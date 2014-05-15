package uk.co.morleydev.ghosthunt.model.component.game

import uk.co.morleydev.ghosthunt.model.net.ClientId

/**
 * A remote entity is an entity that is somehow controlled by a remote player,
 * so is any actor that is not the local actor (and every actor on the server)
 *
 * @param id
 */
case class Remote(id : ClientId)
