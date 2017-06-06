package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.*
import net.mostlyoriginal.pickleperfect.common.EntityPattern
import net.mostlyoriginal.pickleperfect.internal.Subscription
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


/**
 * @author Daan van Yperen
 */
class PatternStoreTest {

    val pattern1 = EntityPattern()
            .all(TestComponent1::class)
    val pattern2 = EntityPattern()
            .all(TestComponent2::class)
    val pattern_structurally_like_2 = EntityPattern()
            .all(TestComponent2::class)

    @Test
    fun When_resolving_pattern_twice_Should_return_identical_subscription() {
        val store = PatternStore(::testComponentBitLookup, ::Subscription)
        assertTrue(
                store.getSubscription(pattern1) === store.getSubscription(pattern1))
    }

    @Test
    fun When_resolving_structurally_identical_patterns_Should_return_identical_subscription() {
        val store = PatternStore(::testComponentBitLookup, ::Subscription)
        assertTrue(
                store.getSubscription(pattern2) === store.getSubscription(pattern_structurally_like_2))
    }

    @Test
    fun When_resolving_structurally_different_patterns_Should_return_different_subscriptions() {
        val store = PatternStore(::testComponentBitLookup, ::Subscription)
        assertTrue(
                store.getSubscription(pattern1) !== store.getSubscription(pattern2))
    }
}