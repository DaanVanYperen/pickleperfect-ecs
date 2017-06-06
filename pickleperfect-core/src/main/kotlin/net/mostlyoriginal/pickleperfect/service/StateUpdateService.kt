package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.common.*
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
    private val liveEntityComposition = Bag<Bits>()
    private var processedCompositionId = -1

    /** Observe components being added to entity. */
    override fun componentAdded(entityId: Int, componentBit: Int) {
        dirtyComponentsOnEntity[entityId] = true
        liveComposition(entityId)[componentBit] = true
    }

    /** Observe components being removed. */
    override fun componentRemoved(entityId: Int, componentBit: Int) {
        dirtyComponentsOnEntity[entityId] = true
        liveComposition(entityId)[componentBit] = false
    }

    /**
     * When called (Typically between system calls).
     * - Updates composition lookups
     * - Informs subscriptions of new compositions.
     * - Informs subscriptions of changed entities.
     */
    fun update(compositionStore: CompositionStore, subscriptionStore: SubscriptionStore) {

        updateEntityCompositionLookup(compositionStore)
        informSubscriptionsOfNewCompositions(subscriptionStore, compositionStore)
        informSubscriptionsOfChangedEntities(subscriptionStore, compositionStore)

        reset()
    }

    private fun reset() {
        dirtyComponentsOnEntity.clear()
    }

    fun liveComposition(entityId: Int) = liveEntityComposition.getOrPut(entityId, { Bits() })

    private fun updateEntityCompositionLookup(compositionStore: CompositionStore) {
        if (!dirtyComponentsOnEntity.pristine) {
            dirtyComponentsOnEntity.forTrue {
                compositionStore.registerCompositionChange(it, liveComposition(it))
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