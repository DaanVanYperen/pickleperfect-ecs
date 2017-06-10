package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.IgnoreComponentMutationListener
import net.mostlyoriginal.pickleperfect.TestComponent1
import net.mostlyoriginal.pickleperfect.World
import net.mostlyoriginal.pickleperfect.WorldConfiguration
import net.mostlyoriginal.pickleperfect.common.Bits
import net.mostlyoriginal.pickleperfect.common.EntityPattern
import net.mostlyoriginal.pickleperfect.internal.Subscription
import net.mostlyoriginal.pickleperfect.internal.SubscriptionListener
import net.mostlyoriginal.pickleperfect.internal.WorldFacade
import net.mostlyoriginal.pickleperfect.predicate.ContainsAnyBitPredicate
import net.mostlyoriginal.pickleperfect.service.common.ComponentMutationListener
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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

    val pattern = EntityPattern().all(TestComponent1::class)

    @Test
    fun When_entity_is_purged_Should_retain_component_state_for_listeners() {
        val facade = WorldFacade(World(WorldConfiguration().with(TestComponent1::class, ::TestComponent1)))
        val mComponent1 = facade.createMapper(TestComponent1::class)
        var called = 0

        val entity = facade.create()
        val entityId = entity.id

        class Listener : SubscriptionListener {
            override fun removed(entities: Bits) {
                assertTrue(mComponent1.has(entityId)) // state remains! yippy.
                called++
            }

            override fun added(entities: Bits) {
            }

        }

        val subscription = facade.getSubscription(pattern)
        subscription.register(Listener())

        mComponent1.create(entityId)

        facade.flush() // otherwise will never be signed up.
        facade.delete(entityId)
        facade.flush()

        assertEquals(1,called)
    }

    @Test
    fun When_entity_is_purged_Should_unsub_from_all_subscriptions() {
        val facade = WorldFacade(World(WorldConfiguration().with(TestComponent1::class, ::TestComponent1)))
        val mComponent1 = facade.createMapper(TestComponent1::class)
        var called = 0

        val entity = facade.create()
        val entityId = entity.id

        val subscription = facade.getSubscription(pattern)

        mComponent1.create(entityId)

        facade.flush() // otherwise will never be signed up.
        facade.delete(entityId)
        facade.flush()

        assertEquals(0,subscription.entityCount)
        assertFalse(subscription.has(entityId))
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