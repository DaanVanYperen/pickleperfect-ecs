package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.WorldConfiguration
import net.mostlyoriginal.pickleperfect.common.Bag
import net.mostlyoriginal.pickleperfect.common.InvalidConfigurationException
import net.mostlyoriginal.pickleperfect.common.OrdinalTypeStore
import net.mostlyoriginal.pickleperfect.common.orConfigurationError
import net.mostlyoriginal.pickleperfect.service.common.ComponentMutationListener
import kotlin.reflect.KClass

/**
 * Factory for component stores.
 *
 * @See Component
 * @See ComponentStore
 * @author Daan van Yperen
 * @param types Component types to register.
 * @param componentMutationListener listener to invoke upon each composition change. Called immediately.
 */
class ComponentService
(
        val componentMutationListener: ComponentMutationListener
) {

    private val types = OrdinalTypeStore<KClass<*>>()
    private val stores = Bag<ComponentStore<*>>()

    fun register(types: List<WorldConfiguration.ComponentType<*>>) {
        types.forEach {
            register(it.componentClass as KClass<Component>, it.newInstance)
        }
    }

    /**
     * Register component type.
     */
    fun <T : Component> register(componentClass: KClass<T>, newInstance: () -> T) {
        val indexOf = bitIndexOf(componentClass)
        if (stores[indexOf] != null) {
            throw InvalidConfigurationException("Cannot register component twice on the same component service.")
        }
        stores[indexOf] = ComponentStore(componentMutationListener, newInstance, indexOf)
    }

    /**
     * @return store for passed component type.
     */
    fun <T : Component> getStore(componentClass: KClass<T>): ComponentStore<T> {
        @Suppress("UNCHECKED_CAST")
        return stores[bitIndexOf(componentClass)].orConfigurationError("Unknown component class, did you register it with ComponentService?") as ComponentStore<T>
    }

    /**
     * @return Bit index / ID for component type store.
     */
    fun <T : Component> bitIndexOf(componentClass: KClass<T>): Int = types.getOrCreate(componentClass)
}