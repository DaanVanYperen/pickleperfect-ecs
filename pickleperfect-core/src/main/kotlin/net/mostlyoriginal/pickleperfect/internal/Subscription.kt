package net.mostlyoriginal.pickleperfect.internal

import net.mostlyoriginal.pickleperfect.common.Bag
import net.mostlyoriginal.pickleperfect.common.Bits
import net.mostlyoriginal.pickleperfect.predicate.BitPredicate

/**
 * Tracks entities that match predicate.
 *
 * @author Daan van Yperen
 */
open class Subscription(val predicate: BitPredicate) {

    private val desiredCompositions = Bits()
    private val listeners = Bag<SubscriptionListener>()

    val entities = Bits()
    var entityCount = 0

    val addedEntities = Bits()
    val removedEntities = Bits()

    /** @return {@code true} if no entities in subscription. */
    fun isEmpty(): Boolean = entityCount == 0

    /** @return {@code true} if at least one entity in subscription. */
    fun isNotEmpty(): Boolean = entityCount != 0


    fun register(listener: SubscriptionListener) {
        listeners.add(listener)
    }

    /** Add entity to subscription. Does nothing when already added. */
    open fun add(entityId: Int) {
        if (!entities[entityId]) {
            entities[entityId] = true
            entityCount++

            addedEntities[entityId] = true
            removedEntities[entityId] = false
        }
    }

    /** Inform listeners of subscription delta. Resets delta in the process. */
    fun invokeListeners() {
        if (!addedEntities.pristine) {
            listeners.forEach { it.added(addedEntities) }
            addedEntities.clear()
        }

        if (!removedEntities.pristine) {
            listeners.forEach { it.removed(removedEntities) }
            removedEntities.clear()
        }
    }

    /** Remove entity from subscription. Does nothing when already removed. */
    fun remove(entityId: Int) {
        if (entities[entityId]) {
            entities[entityId] = false
            entityCount--

            addedEntities[entityId] = false
            removedEntities[entityId] = true
        }
    }

    fun has(entityId: Int) = entities[entityId]

    /** Inform subscription of new composition.
     * @odo urn into ImmutableBits
     */
    fun registerComposition(compositionId: Int, composition: Bits) {
        if (predicate.matches(composition)) {
            desiredCompositions[compositionId] = true
        }
    }

    /** @return true if subscription would accept given composition ID as a member. */
    fun accepts(compositionId: Int): Boolean = desiredCompositions[compositionId]

    /** Iterate over all entities in this subscription. */
    inline fun forEach(function: (Int) -> Unit) {
        // @todo probably want to create a bag for this?
        entities.forTrue {
            function(it)
        }
    }

    /**
     * Reconsider membership for entity, based on current composition ID.
     *
     * Register all unique compositions with {@see registerComposition}
     * before calling this method. Internally the subscription predetermines
     * which compositions are desirable.
     *
     * @param compositionId current unique composition ID for entity.
     */
    fun reconsiderMembershipFor(entityId: Int, compositionId: Int) {
        val accepts = accepts(compositionId)
        val contains = entities[entityId]

        if (accepts && !contains) {
            add(entityId)
        } else if (!accepts && contains) {
            remove(entityId)
        }
    }
}

