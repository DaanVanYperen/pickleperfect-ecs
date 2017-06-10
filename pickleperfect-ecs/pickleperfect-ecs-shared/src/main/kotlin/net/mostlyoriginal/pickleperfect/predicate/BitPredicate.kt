package net.mostlyoriginal.pickleperfect.predicate

import net.mostlyoriginal.pickleperfect.common.Bits

/**
 * @author Daan van Yperen
 */
interface BitPredicate {
    fun matches(bits: Bits): Boolean
}