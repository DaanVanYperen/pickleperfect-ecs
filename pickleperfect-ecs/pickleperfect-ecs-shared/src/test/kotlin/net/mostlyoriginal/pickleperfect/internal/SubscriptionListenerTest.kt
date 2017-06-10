package net.mostlyoriginal.pickleperfect.internal

import net.mostlyoriginal.pickleperfect.common.Bits
import net.mostlyoriginal.pickleperfect.predicate.ContainsAnyBitPredicate
import net.mostlyoriginal.pickleperfect.predicate.EverythingPredicate
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * @author Daan van Yperen
 */
class SubscriptionListenerTest {

    class CountingListener : SubscriptionListener {
        var addedCalls: Int = 0
        var removedCalls: Int = 0
        var removed: Bits? = null
        var added: Bits? = null

        override fun added(entities: Bits) {
            addedCalls++
            added = entities.copy()
        }

        override fun removed(entities: Bits) {
            removedCalls++
            removed = entities.copy()
        }

    }

    @Test
    fun Should_inform_listener_added_entities() {
        val subscription = Subscription(EverythingPredicate())
        val listener = CountingListener()
        subscription.register(listener)
        subscription.add(1)
        subscription.add(2)

        subscription.invokeListeners()

        assertEquals(1, listener.addedCalls)
        assertEquals(0, listener.removedCalls)
        assertEquals(Bits.of(false, true, true), listener.added)
    }

    @Test
    fun Should_inform_listener_removed_entities() {
        val subscription = Subscription(EverythingPredicate())
        val listener = CountingListener()
        subscription.register(listener)
        subscription.add(1)
        subscription.add(2)
        subscription.invokeListeners()
        subscription.remove(1)
        subscription.remove(2)
        subscription.invokeListeners()

        assertEquals(1, listener.removedCalls)
        assertEquals(Bits.of(false, true, true), listener.removed)
    }

    @Test
    fun When_nothing_changed_Should_not_call_listeners() {
        val subscription = Subscription(EverythingPredicate())
        val listener = CountingListener()
        subscription.register(listener)
        subscription.invokeListeners()
        assertEquals(0, listener.removedCalls)
        assertEquals(0, listener.addedCalls)
    }

    @Test
    fun When_no_delta_on_second_call_Should_not_call_listeners() {
        val subscription = Subscription(EverythingPredicate())
        val listener = CountingListener()
        subscription.register(listener)
        subscription.add(1)
        subscription.add(2)
        subscription.invokeListeners()
        subscription.invokeListeners()
        assertEquals(1, listener.addedCalls)
    }


}