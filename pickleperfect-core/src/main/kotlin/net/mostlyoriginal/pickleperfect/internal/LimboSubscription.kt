package net.mostlyoriginal.pickleperfect.internal

import net.mostlyoriginal.pickleperfect.common.Bag
import net.mostlyoriginal.pickleperfect.predicate.BitPredicate

/**
 * Specialized subscription for tracking deleted entities.
 *
 * Allows popping recently added entities.
 *
 * @author Daan van Yperen
 */
internal class LimboSubscription(predicate: BitPredicate) : Subscription(predicate) {

    /** Recently added entries. Not guaranteed to still be available if user circumvents the normal method calls. */
    val limbo = Bag<Int>()

    /** Add entity to subscription. Does nothing when already added. */
    override fun add(entityId: Int) {
        super.add(entityId)
        limbo.add(entityId)
    }

    /** */
    fun pop(): Int {
        while (limbo.notEmpty()) {
            val id = limbo.pop()
            if (entities[id]) {
                remove(id)
                return id
            }
        }
        return -1
    }
}