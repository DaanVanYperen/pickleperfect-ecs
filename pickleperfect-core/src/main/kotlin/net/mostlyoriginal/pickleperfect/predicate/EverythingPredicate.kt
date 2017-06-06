package net.mostlyoriginal.pickleperfect.predicate

import net.mostlyoriginal.pickleperfect.common.Bits

/**
 * @author Daan van Yperen
 */
class EverythingPredicate : BitPredicate {
    override fun matches(bits: Bits): Boolean = true
}