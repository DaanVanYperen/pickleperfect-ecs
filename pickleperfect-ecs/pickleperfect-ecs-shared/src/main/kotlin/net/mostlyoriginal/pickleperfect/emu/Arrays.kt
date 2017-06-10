package net.mostlyoriginal.pickleperfect.emu

/**
 * No guarantees about internal function being identical.
 *
 * @author Daan van Yperen
 */
class Arrays {
    companion object {
        fun <T : Any?> hashCode(array: Array<T>?): Int {
            if (array == null)
                return 0
            var result = 1
            for (t in array) {
                result = result * 31 + (t?.hashCode() ?: 0)
            }
            return result
        }
    }
}