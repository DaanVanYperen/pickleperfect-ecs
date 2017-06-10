package net.mostlyoriginal.pickleperfect.internal

import net.mostlyoriginal.pickleperfect.common.Bits
import net.mostlyoriginal.pickleperfect.predicate.ContainsAnyBitPredicate
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author Daan van Yperen
 */
class SubscriptionTest {

    @Test
    fun Should_accept_reject_based_on_matching_compositions() {

        val testCompositionA = Bits.of(true, false, false)
        val testCompositionB = Bits.of(false, true, false)

        val subscription = Subscription(ContainsAnyBitPredicate(Bits.of(false, true, true)))

        val MISMATCHED_COMPOSITION = 1
        val VALID_COMPOSITION = 2

        // register new compositions.
        subscription.registerComposition(MISMATCHED_COMPOSITION, testCompositionA)
        subscription.registerComposition(VALID_COMPOSITION, testCompositionB)

        assertFalse(subscription.accepts(MISMATCHED_COMPOSITION))
        assertTrue(subscription.accepts(VALID_COMPOSITION))
    }


    @Test
    fun Should_not_explode_upon_unknown_composition() {
        val subscription = Subscription(ContainsAnyBitPredicate(Bits.of(false, true, true)))
        assertFalse(subscription.accepts(999))
    }

    @Test
    fun Should_accept_matching_entities() {
        val subscription = createSubscriptionThatAccepts(9)
        val entityId = 1
        subscription.reconsiderMembershipFor(entityId, 9)
        assertTrue(subscription.has(entityId))
    }

    @Test
    fun Should_ignore_mismatching_entities() {
        val subscription = createSubscriptionThatAccepts(9)
        val entityId = 1
        subscription.reconsiderMembershipFor(entityId, 8)
        assertFalse(subscription.has(entityId))
    }

    @Test
    fun When_entity_becomes_mismatching_Should_remove() {
        val subscription = createSubscriptionThatAccepts(9)
        val entityId = 1
        subscription.reconsiderMembershipFor(entityId, 9)
        subscription.reconsiderMembershipFor(entityId, 8)
        assertFalse(subscription.has(entityId))
    }

    private fun createSubscriptionThatAccepts(compositionId: Int): Subscription {
        val matchingComposition = Bits.of(false, true, true)
        val subscription = Subscription(ContainsAnyBitPredicate(matchingComposition))
        subscription.registerComposition(compositionId, matchingComposition)
        return subscription
    }

}