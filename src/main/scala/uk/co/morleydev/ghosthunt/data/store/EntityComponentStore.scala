package uk.co.morleydev.ghosthunt.data.store

import scala.collection.{GenSeq, JavaConversions, GenMap}
import java.util.concurrent.ConcurrentHashMap
import java.util.Map.Entry

class EntityComponentStore {
  private val entities = new ConcurrentHashMap[EntityId, ConcurrentHashMap[String, Any]]()

  def createEntity() : EntityId = {
    val id = new EntityId()
    entities.put(id, new ConcurrentHashMap[String, Any]())
    id
  }

  def removeEntity(id : EntityId) =
    entities.remove(id)

  def link(id : EntityId, name : String, component : Any) = {
    val ec = entities.get(id)
    if (ec != null)
      ec.put(name, component)
  }

  def unlink(id : EntityId, name: String) = {
    val ec = entities.get(id)
    if (ec != null)
      ec.remove(name)
  }

  private def entrySetToScalaMap[K,V](set : java.util.Set[Entry[K,V]]) : GenMap[K,V] = {
    JavaConversions.asScalaSet[Entry[K,V]](set)
                   .map(s => (s.getKey, s.getValue))
                   .toMap
  }

  def get(components : String*) : GenMap[EntityId, GenMap[String, Any]] =
    entrySetToScalaMap(entities.entrySet())
      .map(s => (s._1, entrySetToScalaMap(s._2.entrySet())))
      .filter(ec => components.forall(component => ec._2.contains(component)))
      .toMap
}
