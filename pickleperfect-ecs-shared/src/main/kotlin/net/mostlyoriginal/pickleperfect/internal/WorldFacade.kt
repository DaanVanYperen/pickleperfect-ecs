package net.mostlyoriginal.pickleperfect.internal

import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.Entity
import net.mostlyoriginal.pickleperfect.World
import net.mostlyoriginal.pickleperfect.common.AccessingRecycledEntityException
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
     * Flush pending changes, updating subscriptions, calling listeners and finalising entity deletions.
     *
     * Safe to call from within system, but make sure you do not call this from while iterating over any subscriptions.
     */
    fun flush() {
        world.updateService.update(world.compositionStore, world.subscriptionStore, world.componentService)
    }

    /** @return bag of system harnesses. */
    fun systems(): Bag<SystemHarness> {
        return world.systems
    }

    /**
     * Obtain instance with given id.
     *
     * Deleted entities can be safely accessed until the next {@see flush}, including all of its components.
     *
     * @throws AccessingRecycledEntityException if called on entities deleted before a flush
     * @return entity of given entityId. Will be created if never requested before.
     */
    fun get(entityId: Int): Entity {
        return world.entityService.get(entityId)
    }

    /**
     * @return spawn new entity.
     *
     * Can recycle all entities deleted before the last flush.
     *
     * @see flush
     */
    fun create(): Entity {
        return world.entityService.create()
    }

    /**
     * Schedule deletion of entity from world.
     *
     * Deleted entities can be safely accessed until the next {@see flush}, including all of its components.
     *
     * @see flush
     */
    fun delete(entityId: Int) {
        world.entityService.delete(entityId)
    }

    /** @return subscription for pattern. Cached so relatively cheap. */
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