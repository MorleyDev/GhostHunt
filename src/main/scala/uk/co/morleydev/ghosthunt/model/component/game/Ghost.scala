package uk.co.morleydev.ghosthunt.model.component.game

/**
 * A ghost is the villain of the game of ghost hunt, and this entity is used to tag an entity as one of thos villains.
 * Mutually exclusive with the player component.
 *
 * @param id A value between 0 to 2 that marks the ghost as one of three possible ghost sprites (red, green, blue)
 */
case class Ghost(id : Int)
