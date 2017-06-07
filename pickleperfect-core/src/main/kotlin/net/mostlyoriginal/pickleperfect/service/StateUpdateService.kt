package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.common.*
import net.mostlyoriginal.pickleperfect.predicate.DeletedPredicate
import net.mostlyoriginal.pickleperfect.predicate.DeletedPredicate.Companion.DELETED_COMPONENT_BIT
import net.mostlyoriginal.pickleperfect.service.common.ComponentMutationListener

/**
 * Handles overarching state related concerns.
 *
 * @see update for an overview.
 * @todo perhaps this belongs in CompositionStore?
 * @author Daan van Yperen
 */
class StateUpdateService : ComponentMutationListener {

    private val dirtyComponentsOnEntity = Bits()
    private val entityComposition = Bag<Bits>()
    private var processedCompositionId = -1
    private var scheduledForComponentCleanup = Bag<Int>(64)

    /** Observe components being added to entity. */
    override fun componentAdded(entityId: Int, componentBit: Int) {
        dirtyComponentsOnEntity[entityId] = true
        compositionOf(entityId)[componentBit] = true
    }

    /** Observe components being removed. */
    override fun componentRemoved(entityId: Int, componentBit: Int) {
        dirtyComponentsOnEntity[entityId] = true
        compositionOf(entityId)[componentBit] = false
    }

    /**
     * When called (Typically between system calls).
     *
     * 1. Set aside all entities that are flagged Deleted
     * 2. Updates composition lookups
     * 3. Informs subscriptions of new compositions.
     * 4. Informs subscriptions of changed entities.
     * 5. Cleanup deleted entities.
     */
    fun update(compositionStore: CompositionStore, subscriptionStore: SubscriptionStore, componentService: ComponentService) {

        var workRemaining = !dirtyComponentsOnEntity.pristine

        while (workRemaining) {
            prepareDeletedEntitiesForCleanup()
            updateEntityCompositionLookup(compositionStore)
            informSubscriptionsOfNewCompositions(subscriptionStore, compositionStore)
            informSubscriptionsOfChangedEntities(subscriptionStore, compositionStore)

            markDirtyEntitiesAsResolved()

            // This might introduce new dirty entities, causing a re-run.
            informListenersOfSubscriptionChanges(subscriptionStore)

            workRemaining = !dirtyComponentsOnEntity.pristine

            cleanupDeletedEntityComponents(componentService)
        }

        dirtyComponentsOnEntity.pristine = true // cleanupDeletedEntityComponents may un-pristine the bitvector.
    }

    private fun informListenersOfSubscriptionChanges(subscriptionStore:SubscriptionStore) {

        // not implemented yet.
        //subscriptionStore.callListenersWithChanges()
    }

    /**
     * Deletes all components from entities flagged with {@see Deleted} component, except for the Deleted flag itself.
     */
    private fun cleanupDeletedEntityComponents(componentService: ComponentService) {

        while (scheduledForComponentCleanup.notEmpty()) {
            val entityId = scheduledForComponentCleanup.pop()
            val composition = compositionOf(entityId)

            // skip deleted bit to avoid having to if-check everything.
            composition[DELETED_COMPONENT_BIT] = false
            composition.forTrue { bit ->
                // @todo danger, this will actually touch the bitset we are iterating! Might be fine.
                componentService.getStore(bit).remove(entityId)
            }
            composition[DELETED_COMPONENT_BIT] = true

            // Removing components marks them dirty. manually clear so system won't act on it.
            dirtyComponentsOnEntity[entityId] = false
        }
    }


    val deletedPredicate = DeletedPredicate()
    private fun prepareDeletedEntitiesForCleanup() {
        if (!dirtyComponentsOnEntity.pristine) {
            dirtyComponentsOnEntity.forTrue {
                if (deletedPredicate.matches(compositionOf(it))) {
                    scheduledForComponentCleanup.push(it)
                }
            }
        }
    }

    private fun markDirtyEntitiesAsResolved() {
        dirtyComponentsOnEntity.clear()
    }

    /**
     * @return Composition of entity at this exact moment. Might be ahead of what CompositionStore knows.
     */
    fun compositionOf(entityId: Int): Bits {
        val composition = entityComposition.getOrPut(entityId, { Bits() })
        return composition
    }

    /**
     * @return Composition of entity at this exact moment. For Deleted, returns a default Deleted-only set bit.
     */
    private fun compositionOfOrStrippedIfDeleted(it: Int): Bits {
        val composition = compositionOf(it)
        return if (deletedPredicate.matches(composition)) DELETED_COMPOSITION_BITS else composition
    }

    val DELETED_COMPOSITION_BITS = Bits.of(DeletedPredicate.DELETED_COMPONENT_BIT)

    private fun updateEntityCompositionLookup(compositionStore: CompositionStore) {
        if (!dirtyComponentsOnEntity.pristine) {
            dirtyComponentsOnEntity.forTrue {
                compositionStore.registerCompositionChange(it, compositionOfOrStrippedIfDeleted(it))
            }
        }
    }

    private fun informSubscriptionsOfNewCompositions(subscriptionStore: SubscriptionStore, compositionStore: CompositionStore) {
        val highestId = compositionStore.highestId()
        // whenever new compositions are discovered, highestId will reflect that. we need to process everything new only.
        if (highestId > processedCompositionId) {
            compositionStore.forEach(processedCompositionId + 1, highestId) { compositionId, composition ->
                subscriptionStore.registerComposition(compositionId, composition)
            }
            processedCompositionId = highestId
        }
    }

    private fun informSubscriptionsOfChangedEntities(subscriptionStore: SubscriptionStore, compositionStore: CompositionStore) {
        if (!dirtyComponentsOnEntity.pristine) {
            dirtyComponentsOnEntity.forTrue {
                subscriptionStore.reconsiderMembershipFor(it, compositionStore.getCompositionId(it))
            }
        }
    }
}