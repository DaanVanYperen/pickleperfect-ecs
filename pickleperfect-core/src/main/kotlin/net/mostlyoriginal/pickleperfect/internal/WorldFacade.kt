package net.mostlyoriginal.pickleperfect.internal

import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.Entity
import net.mostlyoriginal.pickleperfect.World
import net.mostlyoriginal.pickleperfect.common.Bag
import net.mostlyoriginal.pickleperfect.common.EntityPattern
import net.mostlyoriginal.pickleperfect.service.ComponentStore
import kotlin.reflect.KClass

/**
 * Helper facade to interact with PicklePerfect.
 *
 * Contains universally useful sugar.
 *
 * @author Daan van Yperen
 */
class WorldFacade(
        private val world: World) {

    /** Iterate over all entities matching pattern. */
    inline fun forEach(matching: EntityPattern, function: (Entity) -> Unit) {
        val subscription: Subscription = getSubscription(matching)
        subscription.entities.forTrue {
            function(get(it))
        }
    }

    /**
     * Flush pending changes, updating subscriptions.
     *
     * Safe to call from within system, but make sure you do not call this from while iterating over a subscription.
     */
    fun flush() {
        world.updateService.update(world.compositionStore, world.subscriptionStore)
    }

    /** @return bag of system harnesses. */
    fun systems(): Bag<SystemHarness> {
        return world.systems
    }

    fun get(entityId: Int): Entity {
        return world.entityStore.get(entityId)
    }

    /** @return New entity. */
    fun create(): Entity {
        return world.entityStore.create()
    }

    fun getSubscription(pattern: EntityPattern): Subscription {
        return world.patternStore.getSubscription(pattern)
    }

    /**
     * @return Fetch component mapper.
     * @deprecated Use discouraged, see below.
     * @todo we want to use fluid entities, this mapper mechanic is undesirable.
     */
    fun <T : Component> createMapper(type: KClass<T>): ComponentStore<T> {
        return world.componentService.getStore(type)
    }
}