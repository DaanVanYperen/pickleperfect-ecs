package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.Entity
import net.mostlyoriginal.pickleperfect.common.Bag
import net.mostlyoriginal.pickleperfect.common.Bits
import net.mostlyoriginal.pickleperfect.internal.Subscription
import net.mostlyoriginal.pickleperfect.predicate.EverythingPredicate

/**
 * Stores all existing compositions.
 *
 * @author Daan van Yperen
 */
class SubscriptionStore {
    private val subscriptions = Bag<Subscription>(64)

    companion object {
        private const val CATCH_ALL_SUBSCRIPTION_INDEX: Int = 0
    }

    init {
        subscriptions[CATCH_ALL_SUBSCRIPTION_INDEX] = Subscription(EverythingPredicate()) // default subscription.
    }

    fun add(subscription: Subscription, compositions: CompositionStore): Subscription {
        subscriptions.add(subscription)

        // register compositions
        compositions.forEach { compositionId, composition ->
            subscription.registerComposition(compositionId, composition)
        }

        // feed
        subscriptions[CATCH_ALL_SUBSCRIPTION_INDEX]!!.entities.forTrue {
            subscription.reconsiderMembershipFor(it, compositions.getCompositionId(it))
        }

        return subscription
    }

    /**
     * Register a new composition with all subscriptions.
     * May only be called once per compositionId!
     **/
    fun registerComposition(compositionId: Int, composition: Bits) {
        subscriptions.forEach {
            it.registerComposition(compositionId, composition)
        }
    }

    /** Make all subscriptions reconsider if membership is desired. */
    fun reconsiderMembershipFor(entityId: Int, compositionId: Int) {
        subscriptions.forEach { it.reconsiderMembershipFor(entityId, compositionId) }
    }
}