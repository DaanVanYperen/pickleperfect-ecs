package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.common.Bits
import net.mostlyoriginal.pickleperfect.internal.Subscription
import net.mostlyoriginal.pickleperfect.predicate.ContainsAllBitPredicate
import org.junit.Test
import kotlin.test.assertTrue


/**
 * @author Daan van Yperen
 */
class SubscriptionStoreTest {

    val entityId = 1

    @Test
    fun Should_populate_new_subscriptions_with_known_entities() {
        val compositions = CompositionStore()
        val store = SubscriptionStore()

        val testComposition = Bits.of(true, false, true)

        compositions.registerCompositionChange(entityId, testComposition)

        store.registerComposition(compositions.getCompositionId(entityId), testComposition)
        store.reconsiderMembershipFor(entityId, compositions.getCompositionId(entityId))

        val subscription = Subscription(ContainsAllBitPredicate(testComposition))
        store.add(subscription, compositions)

        assertTrue(subscription.has(entityId))
    }
}