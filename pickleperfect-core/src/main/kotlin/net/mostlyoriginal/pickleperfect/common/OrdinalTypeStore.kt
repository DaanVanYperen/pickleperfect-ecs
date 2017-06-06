package net.mostlyoriginal.pickleperfect.common

/**
 * Track ordinal metadata for set of classes.
 *
 * @author Daan van Yperen
 */
class OrdinalTypeStore<T> {
    private var highestId = -1
    private val cache = HashMap<T, Int>()
    private val lookup = Bag<T>()

    class OrdinalType<out T>(val type: T, val index: Int)

    fun highestId() = highestId

    fun getValue(id: Int): T {
        return lookup[id] ?: throw RuntimeException("Ordinal type store does not contain " + id)
    }

    /**
     * Get ID for mutable subjects.
     * @return unique ordinal ID for subject.
     * @param subject subject to obtain ID for.
     * @param cloneFunction Function to clone subject, whenever a new ID is created. Useful when dealing with mutable classes and you want to match on structural equality.
     * @todo Ripple in the fabric. might want to do something with immutable indices?
     */
    inline fun getOrCreate(subject: T, cloneFunction: (T) -> T): Int {
        return getOrCreate(if (has(subject)) subject else cloneFunction(subject))
    }

    fun has(subject: T) = cache[subject] != null

    /**
     * Get ID for immutable subject or subject with referential equality.
     * @return unique ordinal ID for subject.
     * @param subject subject to obtain ID for. Consider if this needs to be immutable!
     */
    fun getOrCreate(subject: T): Int {
        val result = cache[subject]
        if (result != null) {
            return result
        } else {
            cache[subject] = ++highestId
            lookup[highestId] = subject
            return highestId
        }
    }
}