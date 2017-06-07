package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.common.Bag
import net.mostlyoriginal.pickleperfect.common.EntityPattern
import net.mostlyoriginal.pickleperfect.common.OrdinalTypeStore
import net.mostlyoriginal.pickleperfect.internal.EntityPatternCompiler
import net.mostlyoriginal.pickleperfect.internal.Subscription
import net.mostlyoriginal.pickleperfect.predicate.BitPredicate
import net.mostlyoriginal.pickleperfect.predicate.DeletedPredicate
import kotlin.reflect.KClass

/**
 * Maps patterns to subscriptions
 *
 * @author Daan van Yperen
 */
class PatternStore(
        componentBitLookup: (KClass<out Component>) -> Int,
        val subscriptionProducer: (BitPredicate) -> Subscription) {

    private val patternCompositions = OrdinalTypeStore<EntityPattern>()
    private val patternIdentities = OrdinalTypeStore<EntityPattern.Identity>()
    private val compiledEntityPatternByIdentity = Bag<Subscription>()
    private val compiledEntityPatternByComposition = Bag<Subscription>()
    private val compiler = EntityPatternCompiler(componentBitLookup, { DeletedPredicate(false) })

    /** @return subscription for pattern. Cached by pattern identity. */
    fun getSubscription(pattern: EntityPattern): Subscription {
        val index = patternIdentities.getOrCreate(pattern.identity)
        return compiledEntityPatternByIdentity.getOrPut(index) { findOrCreateSubscriptionFor(pattern) }
    }

    /**
     * @return subscription that matches pattern composition. Cached by composition (slower).
     */
    private fun findOrCreateSubscriptionFor(pattern: EntityPattern): Subscription {
        val compositionIndex = patternCompositions.getOrCreate(pattern)
        return compiledEntityPatternByComposition.getOrPut(compositionIndex) { subscriptionProducer(compiler.compile(pattern)) }
    }

}