package net.mostlyoriginal.pickleperfect.common

import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.emu.Arrays
import kotlin.reflect.KClass

/**
 * Describes entity configuration.
 *
 * @author Daan van Yperen
 */
data class EntityPattern(
        val any: Array<out KClass<out Component>> = emptyArray<KClass<Component>>(),
        val all: Array<out KClass<out Component>> = emptyArray<KClass<Component>>(),
        val none: Array<out KClass<out Component>> = emptyArray<KClass<Component>>()) {

    /** Used for identity comparison. Workaround for lack of IdentityHashMap on JS. */
    val identity = Identity()

    fun all(vararg values: KClass<out Component>): EntityPattern {
        return copy(all = values)
    }

    fun none(vararg values: KClass<out Component>): EntityPattern {
        return copy(none = values)
    }

    fun any(vararg values: KClass<out Component>): EntityPattern {
        return copy(any = values)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EntityPattern) return false
        return all contentEquals other.all &&
                any contentEquals other.any &&
                none contentEquals other.none
    }

    override fun hashCode(): Int {
        var l = 1
        l = l * 31 + Arrays.hashCode(all)
        l = l * 31 + Arrays.hashCode(any)
        l * 31 + Arrays.hashCode(none)
        return l
    }

    class Identity
}

