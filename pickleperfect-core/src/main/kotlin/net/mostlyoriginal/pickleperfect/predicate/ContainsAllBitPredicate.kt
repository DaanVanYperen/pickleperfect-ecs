package net.mostlyoriginal.pickleperfect.predicate

import net.mostlyoriginal.pickleperfect.common.Bits

/**
 * @author Daan van Yperen
 * @todo make Bits ImmutableBits
 */
class ContainsAllBitPredicate(val toMatch: Bits) : BitPredicate {
    override fun matches(bits: Bits): Boolean {
        return bits.containsAll(toMatch)
    }
}