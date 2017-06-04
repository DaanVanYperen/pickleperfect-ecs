package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.E
import net.mostlyoriginal.pickleperfect.World
import net.mostlyoriginal.pickleperfect.common.Bag
import net.mostlyoriginal.pickleperfect.common.Pool

/**
 * @author Daan van Yperen
 */
class ComponentStore<out C : Component>(producer: () -> C) {
    private val components = Bag<C>()
    private val pool = Pool(producer)

    fun create(entityId: Int): C {
        val result: C? = components[entityId]
        if (result != null) {
            return result
        } else {
            val newInstance: C = pool.obtain()
            components[entityId] = newInstance
            return newInstance
        }
    }

    fun has(entityId: Int) = components[entityId] != null

    fun get(entityId: Int): C = create(entityId)

    fun remove(entityId: Int) {
        val result = components[entityId]
        if (result != null) {
            components[entityId] = null
            pool.release(result)
        }
    }
}
