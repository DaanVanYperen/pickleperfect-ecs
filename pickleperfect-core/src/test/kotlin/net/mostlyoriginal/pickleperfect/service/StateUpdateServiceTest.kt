package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.common.Bits
import net.mostlyoriginal.pickleperfect.internal.Subscription
import net.mostlyoriginal.pickleperfect.predicate.BitPredicate
import net.mostlyoriginal.pickleperfect.predicate.ContainsAnyBitPredicate
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


/**
 * Integration tests.
 *
 * @author Daan van Yperen
 */
class StateUpdateServiceTest {
    private val entityId: Int = 1

    @Test
    fun Should_track_live_composition_of_entities() {

        val service = StateUpdateService()
        val compositionStore = CompositionStore()
        val subscriptionStore = SubscriptionStore()

        service.componentAdded(entityId, 0)
        service.componentAdded(entityId, 1)
        service.componentAdded(entityId, 2)
        service.componentRemoved(entityId, 0)
        service.update(compositionStore, subscriptionStore)

        assertEquals(Bits.of(false, true, true), service.liveComposition(entityId))
    }

    @Test
    fun When_entity_composition_changes_Should_register_changes_with_composition_store() {
        val service = StateUpdateService()
        val compositionStore = CompositionStore()
        val subscriptionStore = SubscriptionStore()

        assertEquals(0, compositionStore.highestId())
        service.componentAdded(entityId, 2)
        service.update(compositionStore, subscriptionStore)
        assertEquals(1, compositionStore.highestId())
        assertEquals(1, compositionStore.getCompositionId(entityId))
    }

    @Test
    fun When_new_composition_types_Should_inform_subscription_store_of_entities_and_compositions() {
        val service = StateUpdateService()
        val compositionStore = CompositionStore()
        val subscriptionStore = SubscriptionStore()

        val subscription = Subscription(ContainsAnyBitPredicate(Bits.of(false, true)))
        subscriptionStore.add(subscription, compositionStore)

        service.componentAdded(entityId, 1)
        service.update(compositionStore, subscriptionStore)

        assertTrue(subscription.has(entityId))
    }
}