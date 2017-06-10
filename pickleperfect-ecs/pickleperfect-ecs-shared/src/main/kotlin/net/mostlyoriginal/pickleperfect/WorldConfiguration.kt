package net.mostlyoriginal.pickleperfect

import net.mostlyoriginal.pickleperfect.common.ProcessingStrategy
import net.mostlyoriginal.pickleperfect.internal.DefaultProcessingStrategy
import kotlin.reflect.KClass

/**
 * @author Daan van Yperen
 */
class WorldConfiguration {
    val systems = mutableListOf<System>()
    val componentTypes = mutableListOf<ComponentType<*>>()
    var entityProducer = ::Entity
    var processingStrategy: ProcessingStrategy = DefaultProcessingStrategy()

    fun with(strategy: ProcessingStrategy) {
        this.processingStrategy = strategy
    }

    fun with(system: System): WorldConfiguration {
        systems.add(system)
        return this
    }

    fun <T : Component> with(componentClass: KClass<T>, newInstance: () -> T): WorldConfiguration {
        componentTypes.add(ComponentType(componentClass, newInstance))
        return this
    }

    data class ComponentType<T : Component>(val componentClass: KClass<T>, val newInstance: () -> T)


}