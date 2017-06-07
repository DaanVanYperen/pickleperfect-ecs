package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.Entity
import net.mostlyoriginal.pickleperfect.common.AccessingRecycledEntityException
import net.mostlyoriginal.pickleperfect.internal.Deleted

/**
 * Manages entities.
 *
 * @author Daan van Yperen
 */
class EntityService<T : Entity>(subscriptionStore: SubscriptionStore, componentService: ComponentService, producer: (Int) -> T) {

    private val entityStore: EntityStore<T> = EntityStore(producer)
    // @todo convert to listener?
    private val limbo = subscriptionStore.getLimboSubscription()
    private val mLimbo = componentService.getStore(Deleted::class)

    /**
     * Obtain instance with given id.
     *
     * Warning: Deleted entities remain accessible until the next {@see flush}, including
     * all components.
     *
     * @throws AccessingRecycledEntityException if called on entities deleted before a flush
     * @return entity of given entityId. Will be created if never requested before.
     */
    fun get(entityId: Int): T {
        if (limbo.has(entityId)) {
            throw AccessingRecycledEntityException("Attempt to access deleted entity $entityId, which is being held for recycling.")
        }
        return entityStore.get(entityId)
    }

    /**
     * Obtain new instance of entity. Prioritizes recycling entities, if possible.
     */
    fun create(): T {
        if (limbo.isNotEmpty()) {
            val entityId = limbo.pop()
            if (entityId != -1) {
                mLimbo.create(entityId)
                return entityStore.get(entityId)
            }
        }
        return entityStore.create()
    }

    /**
     * Schedule deletion of entity from world.
     *
     * Deleted entities can be safely accessed until the next {@see flush}, including all of its components.
     *
     * @see net.mostlyoriginal.pickleperfect.internal.WorldFacade.flush
     */
    fun delete(entityId: Int): Unit {
        mLimbo.create(entityId)
    }


}