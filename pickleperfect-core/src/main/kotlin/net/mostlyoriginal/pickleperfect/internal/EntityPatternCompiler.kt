package net.mostlyoriginal.pickleperfect.internal

import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.common.Bits
import net.mostlyoriginal.pickleperfect.common.EntityPattern
import net.mostlyoriginal.pickleperfect.predicate.*
import kotlin.reflect.KClass

/**
 * Compiles EntityPattern to predicate.
 * @author Daan van Yperen
 */
class EntityPatternCompiler(val componentBitLookup: (KClass<out Component>) -> Int) {

    /** Convert pattern into BitPredicate. */
    fun compile(pattern: EntityPattern): BitPredicate {
        val list = ArrayList<BitPredicate>(3)

        if (pattern.all.isNotEmpty()) {
            list.add(ContainsAllBitPredicate(toBits(pattern.all)))
        }

        if (pattern.any.isNotEmpty()) {
            list.add(ContainsAnyBitPredicate(toBits(pattern.any)))
        }

        if (pattern.none.isNotEmpty()) {
            list.add(ContainsNoneBitPredicate(toBits(pattern.none)))
        }

        return CompositeBitPredicate(list.toTypedArray())
    }

    private fun toBits(types: Array<out KClass<out Component>>): Bits {
        val result = Bits()
        for (entry in types) {
            result[componentBitLookup(entry)] = true
        }
        return result
    }
}
