package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.common.Bag
import net.mostlyoriginal.pickleperfect.common.Pool
import net.mostlyoriginal.pickleperfect.service.common.ComponentMutationListener

/**
 * Container for instances of a component.
 *
 * @author Daan van Yperen
 */
class ComponentStore<out C : Component>(val listener: ComponentMutationListener, producer: () -> C, val componentBit: Int) {
    private val items = Bag<C>()
    private val pool = Pool(producer)

    /**
     * @return Create existing or new component for entityId.
     */
    fun create(entityId: Int): C {
        val result: C? = items[entityId]
        if (result != null) {
            return result
        } else {
            val newInstance: C = pool.obtain()
            items[entityId] = newInstance
            listener.componentAdded(entityId, componentBit)
            return newInstance
        }
    }

    /**
     * @return {@code true} when entity exists. Does nothing when it doesn't.
     */
    fun has(entityId: Int) = items[entityId] != null

    /**
     * @return Create existing or new component for entityId.
     */
    fun get(entityId: Int): C = create(entityId)

    fun remove(entityId: Int) {
        val result = items[entityId]
        if (result != null) {
            listener.componentRemoved(entityId, componentBit)
            items[entityId] = null
            pool.release(result)
        }
    }
}