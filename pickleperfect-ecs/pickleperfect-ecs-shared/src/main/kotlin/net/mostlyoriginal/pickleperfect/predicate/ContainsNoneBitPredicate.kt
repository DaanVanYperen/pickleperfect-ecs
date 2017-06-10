package net.mostlyoriginal.pickleperfect.predicate

import net.mostlyoriginal.pickleperfect.common.Bits

/**
 * @author Daan van Yperen
 */
class ContainsNoneBitPredicate(val toMatch: Bits) : BitPredicate {
    override fun matches(bits: Bits): Boolean {
        return !bits.containsAny(toMatch)
    }
}