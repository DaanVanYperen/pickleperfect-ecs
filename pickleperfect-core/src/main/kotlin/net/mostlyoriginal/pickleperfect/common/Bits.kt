package net.mostlyoriginal.pickleperfect.common

import kotlin.js.Math

class Bits(startingBitCapacity: Int = BIT_SIZE) {
    private var bits = IntArray(capacityNeededForBits(startingBitCapacity))

    companion object {
        const val BIT_SIZE = 32
        private fun capacityNeededForBits(bits: Int) = ((bits-1) / BIT_SIZE) + 1
    }

    fun capacity() : Int = bits.size * BIT_SIZE

    private fun grow(newSize: Int) {
        bits = bits.copyOf(newSize)
    }

    /** @return {@code true} if bit is set, {@code false} if it isn't or out of range. */
    operator fun get(index: Int): Boolean {
        if (index >= capacity()) return false
        return unsafeGet(index)
    }

    /** Set bit to true/false. */
    operator fun set(index: Int, value: Boolean) : Unit {
        growIfNeeded(index)
        unsafeSet(index, value)
    }

    fun growIfNeeded(bits: Int) {
        if (bits > capacity()) {
            grow(capacityNeededForBits(bits))
        }
    }

    /** @return {@code true} if bit is set, undetermined if out of range. */
    private fun unsafeGet(index: Int) = bits[index / BIT_SIZE].getBit(index % BIT_SIZE)

    fun unsafeSet(index: Int, value: Boolean) {
        val aIndex = index / BIT_SIZE
        val localBit = index % BIT_SIZE
        bits[aIndex] =
                if (value)
                    bits[aIndex].setBit(localBit)
                else
                    bits[aIndex].clearBit(localBit)
    }
}