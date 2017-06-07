package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.Entity
import net.mostlyoriginal.pickleperfect.common.Bag

/**
 * Simple store for entities.
 *
 * Entity instances are kept around forever for recycling.
 *
 * @see EntityService
 * @author Daan van Yperen
 */
internal class EntityStore<out T : Entity>(val producer: (Int) -> T) {
    private val items = Bag<T>()

    private var highestId = 0

    /** @return existing or new entity with entityId. */
    fun create(entityId: Int): T {
        val result: T? = items[entityId]
        if (result != null) {
            return result
        } else {
            val newInstance: T = producer(entityId)
            items[entityId] = newInstance
            if (highestId < entityId) {
                highestId = entityId
            }
            return newInstance
        }
    }

    /** Create new entity with sequential ID. Do not call if you have stuff to recycle! */
    fun create(): T {
        return create(++highestId)
    }

    /** Remove entity. Not the same as deletion! */
    fun remove(entityId: Int) {
        items[entityId] = null
    }

    /** @return {@code true} if entity has been created. */
    fun has(entityId: Int) = items[entityId] != null

    /** @return existing or new instance of entityId. */
    fun get(entityId: Int): T = create(entityId)
}