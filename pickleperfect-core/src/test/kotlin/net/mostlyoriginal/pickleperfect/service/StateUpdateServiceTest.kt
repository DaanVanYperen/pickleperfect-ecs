package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.IgnoreComponentMutationListener
import net.mostlyoriginal.pickleperfect.common.Bits
import net.mostlyoriginal.pickleperfect.internal.Subscription
import net.mostlyoriginal.pickleperfect.predicate.ContainsAnyBitPredicate
import net.mostlyoriginal.pickleperfect.service.common.ComponentMutationListener
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail


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
        val componentService = ComponentService(IgnoreComponentMutationListener())

        service.componentAdded(entityId, 0)
        service.componentAdded(entityId, 1)
        service.componentAdded(entityId, 2)
        service.componentRemoved(entityId, 0)
        service.update(compositionStore, subscriptionStore, componentService)

        assertEquals(Bits.of(false, true, true), service.compositionOf(entityId))
    }

    /** Entities that are to be purged should retain o */

    @Test
    fun When_entity_is_purged_Should_retain_component_state_for_listeners() {
        fail("NOT IMPLEMENTED")
    }

    @Test
    fun When_entity_is_purged_Should_unsub_from_all_subscriptions() {
        fail("NOT IMPLEMENTED")
    }

    @Test
    fun When_entity_composition_changes_Should_register_changes_with_composition_store() {
        val service = StateUpdateService()
        val compositionStore = CompositionStore()
        val subscriptionStore = SubscriptionStore()
        val componentService = ComponentService(IgnoreComponentMutationListener())

        assertEquals(0, compositionStore.highestId())
        service.componentAdded(entityId, 2)
        service.update(compositionStore, subscriptionStore, componentService)
        assertEquals(1, compositionStore.highestId())
        assertEquals(1, compositionStore.getCompositionId(entityId))
    }

    @Test
    fun When_new_composition_types_Should_inform_subscription_store_of_entities_and_compositions() {
        val service = StateUpdateService()
        val compositionStore = CompositionStore()
        val subscriptionStore = SubscriptionStore()
        val componentService = ComponentService(IgnoreComponentMutationListener())

        val subscription = Subscription(ContainsAnyBitPredicate(Bits.of(false, true)))
        subscriptionStore.add(subscription, compositionStore)

        service.componentAdded(entityId, 1)
        service.update(compositionStore, subscriptionStore, componentService)

        assertTrue(subscription.has(entityId))
    }
}