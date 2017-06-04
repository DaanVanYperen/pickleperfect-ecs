package net.mostlyoriginal.pickleperfect.common

import kotlin.js.Math

/**
 * @todo license or not?
 * @author Daan van Yperen
 */
class Bag<T> (startingCapacity: Int = 1) {
    var items: Array<Any?> = kotlin.arrayOfNulls(startingCapacity)
    var size = 0

    fun add(value: T) {
        if (items.size > size) {
            grow(items.size * 2)
        }

        items[size++] = value
    }

    operator fun set(index: Int, value: T?) {
        if (index > items.size) {
            // @todo javascript specific dependencies.
            grow(Math.max(items.size * 2, index + 1))
        }
        size = Math.max(items.size, index)
        items[index] = value
    }

    @Suppress("UNCHECKED_CAST")
    // @todo null safe all the way, or better to force Has, T? or getOrSet passthrough?
    operator fun get(index: Int): T? = items[index] as T

    @Suppress("UNCHECKED_CAST")
    fun getOrPut(index: Int, value: T): T = items[index] as T ?: value

    fun grow(newSize: Int) {
        items = items.copyOf(newSize)
    }

    fun notEmpty(): Boolean = size != 0
    fun empty(): Boolean = size == 0

    @Suppress("UNCHECKED_CAST")
    fun pop(): T {
        return items[--size] as T
    }

    fun push(value: T): T {
        add(value)
        return value
    }

    fun capacity() : Int = items.size
}
