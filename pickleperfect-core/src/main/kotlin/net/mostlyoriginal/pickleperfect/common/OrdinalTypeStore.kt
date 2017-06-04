package net.mostlyoriginal.pickleperfect.common

import kotlin.reflect.KClass

/**
 * Track ordinal metadata for set of classes.
 *
 * @author Daan van Yperen
 */
class OrdinalTypeStore {
    var index = 0
    var cache = HashMap<KClass<*>, OrdinalType>()

    class OrdinalType(val type: KClass<*>, val index: Int)

    /**
     * @return
     */
    fun get(type: KClass<*>): OrdinalType {
        val result = cache[type]
        if (result != null) {
            return result
        } else {
            val newInstance = OrdinalType(type, index++)
            cache[type] = newInstance
            return newInstance
        }
    }
}