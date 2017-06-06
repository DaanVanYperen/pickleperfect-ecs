package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.Entity
import net.mostlyoriginal.pickleperfect.common.Bag
import net.mostlyoriginal.pickleperfect.common.Bits

/**
 * Entity store.
 *
 * Entity references are not pooled since they are kept around forever.
 *
 * @todo removeFromWorld.
 * @author Daan van Yperen
 */
class EntityStore<out T : Entity>(val producer: (Int) -> T) {
    private val items = Bag<T>()
    private val limbo = Bag<Int>()
    private val active = Bits()

    private var highestId = 0

    /** @return existing or new entity with entityId. */
    fun create(entityId: Int): T {
        val result: T? = items[entityId]
        if (result != null) {
            if (!active[entityId]) reactivate(entityId)
            return result
        } else {
            val newInstance: T = producer(entityId)
            items[entityId] = newInstance
            reactivate(entityId)
            return newInstance
        }
    }

    /**
     * @return Create existing or new entity.
     */
    fun create(): T {
        return create(if (limbo.empty()) ++highestId else limbo.pop())
    }

    fun remove(entityId: Int) {
        if (active[entityId]) {
            active[entityId] = false
            limbo.push(entityId)
        }
    }

    private fun reactivate(entityId: Int) {
        active[entityId] = true
        if (entityId > highestId) {
            highestId = entityId
        }
    }

    /** @return {@code true} if entity has been created and is alive. */
    fun isActive(entityId: Int) = active[entityId]

    /** @return existing or new instance of entityId. */
    fun get(entityId: Int): T = create(entityId)

    fun forEach(function: (T) -> Unit) {
        active.forTrue {
            function(items[it]!!)
        }
    }
}