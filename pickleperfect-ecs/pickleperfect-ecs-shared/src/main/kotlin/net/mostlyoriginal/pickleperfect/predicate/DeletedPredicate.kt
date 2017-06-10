package net.mostlyoriginal.pickleperfect.predicate

import net.mostlyoriginal.pickleperfect.common.Bits

/**
 * Match on Framework internal deleted bit.
 *
 * @author Daan van Yperen
 */
class DeletedPredicate(val desired: Boolean = true) : BitPredicate {
    companion object {
        const val DELETED_COMPONENT_BIT = 0
    }

    override fun matches(bits: Bits): Boolean {
        return bits[DELETED_COMPONENT_BIT] == desired
    }
}