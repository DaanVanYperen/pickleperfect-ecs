package net.mostlyoriginal.pickleperfect.common

/**
 * @author Daan van Yperen
 */
class Pool<T : Poolable>(val producer: () -> T) {
    private val cache = Bag<T>()

    fun obtain(): T {
        val result = if (cache.notEmpty()) cache.pop() else producer()
        result.reset()
        return result
    }

    fun release(value: T) = cache.push(value)
}