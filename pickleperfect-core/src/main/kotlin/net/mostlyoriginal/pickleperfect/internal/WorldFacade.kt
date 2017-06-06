package net.mostlyoriginal.pickleperfect.internal

import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.Entity
import net.mostlyoriginal.pickleperfect.System
import net.mostlyoriginal.pickleperfect.World
import net.mostlyoriginal.pickleperfect.common.EntityPattern
import net.mostlyoriginal.pickleperfect.service.ComponentStore
import kotlin.reflect.KClass

/**
 * System facade to interact with ODB.
 *
 * Contains helper methods.
 *
 * @author Daan van Yperen
 */
class WorldFacade(
        val world: World,
        val system: System) {

    /** Iterate over all entities in pattern. */
    inline fun forEach(pattern: EntityPattern, function: (Entity) -> Unit) {
        val subscription: Subscription = world.patternStore.getSubscription(pattern)
        subscription.entities.forTrue {
            function(world.entityStore.get(it))
        }
    }

    /**
     * Flush pending changes.
     *
     * Safe to call from within system, but make sure you do not call this from while iterating over a subscription.
     */
    fun flush() {
        world.flush()
    }

    fun create(): Entity {
        return world.entityStore.create()
    }

    fun <T : Component> createMapper(type: KClass<T>): ComponentStore<T> {
        return world.componentService.getStore(type)
    }
}