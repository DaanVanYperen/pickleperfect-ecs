package net.mostlyoriginal.pickleperfect.predicate

import net.mostlyoriginal.pickleperfect.common.Bits

/**
 * @author Daan van Yperen
 */
class CompositeBitPredicate(val predicates: Array<BitPredicate>) : BitPredicate {
    override fun matches(bits: Bits): Boolean {
        for (predicate in predicates) {
            if (!predicate.matches(bits)) return false
        }
        return true
    }
}
