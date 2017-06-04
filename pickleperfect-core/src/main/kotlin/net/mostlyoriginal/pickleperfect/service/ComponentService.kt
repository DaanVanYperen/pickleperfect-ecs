package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.World
import net.mostlyoriginal.pickleperfect.common.Bag
import net.mostlyoriginal.pickleperfect.common.InvalidConfigurationException
import net.mostlyoriginal.pickleperfect.common.OrdinalTypeStore
import net.mostlyoriginal.pickleperfect.common.orConfigurationError
import kotlin.reflect.KClass

/**
 * @author Daan van Yperen
 */
class ComponentService {
    private val types = OrdinalTypeStore()
    private val stores = Bag<ComponentStore<*>>()

    fun <T : Component> register(componentClass: KClass<T>, newInstance: () -> T) {
        val indexOf = indexOf(componentClass)
        if (stores[indexOf] != null) {
            throw InvalidConfigurationException("Cannot register component twice on the same component service.")
        }
        stores[indexOf] = ComponentStore(newInstance)
    }

    fun <T : Component> getStore(componentClass: KClass<T>): ComponentStore<T> {
        @Suppress("UNCHECKED_CAST")
        return stores[indexOf(componentClass)].orConfigurationError("Unknown component class, did you register it with ComponentService?") as ComponentStore<T>
    }

    private fun <T : Component> indexOf(componentClass: KClass<T>): Int = types.get(componentClass).index
}