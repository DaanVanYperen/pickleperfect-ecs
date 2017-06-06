package net.mostlyoriginal.pickleperfect.common

import kotlin.js.Math

/**
 * 'Infinite' bit array.
 *
 * Small footprint initially, backing array grows to fit required bits.
 *
 * Note that equality checks ignore capacity; only enabled bits are compared.
 *
 * @param startingBitCapacity Number of bits this array is expected to fit (initially).
 */
class Bits(startingBitCapacity: Int = BIT_SIZE) {

    constructor(elements: Bits) : this(elements.capacity()) {
        mirror(elements)
    }

    private var bits = IntArray(capacityNeededForBits(startingBitCapacity))
    var pristine: Boolean = true


    companion object {
        const val BIT_SIZE = 32
        private fun capacityNeededForBits(bits: Int) = ((bits - 1) / BIT_SIZE) + 1

        /**
         * Costly, don't use in performant code.
         * @return Bits with assigned values.
         */
        fun of(vararg values: Boolean): Bits {
            val result = Bits(values.size)
            var index = 0
            for (bit in values) {
                result[index++] = bit
            }
            return result
        }
    }

    /**
     * @return Bit capacity of this bit array.
     */
    fun capacity(): Int = bits.size * BIT_SIZE

    private fun grow(newSize: Int) {
        bits = bits.copyOf(newSize)
    }

    /***
     * @return structurally identical copy.
     */
    fun copy(): Bits {
        return Bits(this)
    }

    /** Mirror contents of other entity. */
    private fun mirror(other: Bits) {
        mirrorCapacity(other)
        var index = 0
        for (value in other.bits) {
            bits[index++] = value
        }
    }

    private fun mirrorCapacity(other: Bits) {
        if (other.capacity() != capacity()) {
            grow(capacityNeededForBits(other.capacity()))
        }
    }

    /** @return {@code true} if bit is set, {@code false} if it isn't or out of range. */
    operator fun get(index: Int): Boolean {
        if (index >= capacity()) return false
        return unsafeGet(index)
    }

    /**
     * Set bit state.
     * @param index bit to set
     * @param value bit state
     */
    operator fun set(index: Int, value: Boolean): Unit {
        growIfNeeded(index)
        unsafeSet(index, value)
    }

    /**
     * Grow capacity to fit bits. Actual capacity might end up higher.
     */
    fun growIfNeeded(bits: Int) {
        if (bits > capacity()) {
            grow(capacityNeededForBits(bits))
        }
    }


    /**
     * Get bit without checking capacity. Unsafe! Call {@see get} instead!
     * @return {@code true} if bit is set, undetermined if out of range.
     */
    private fun unsafeGet(index: Int) = bits[index / BIT_SIZE].getBit(index % BIT_SIZE)

    /**
     * Set bit without checking capacity. Unsafe! Call {@see set} instead!
     * @param index bit to set
     * @param value bit state
     */
    fun unsafeSet(index: Int, value: Boolean) {
        if (pristine) pristine = false
        val aIndex = index / BIT_SIZE
        val localBit = index % BIT_SIZE
        bits[aIndex] =
                if (value)
                    bits[aIndex].setBit(localBit)
                else
                    bits[aIndex].clearBit(localBit)
    }

    /**
     * @return {@code true} if all passed bits are set.
     * @Todo optimization candidate
     */
    fun containsAll(requiredBits: Bits): Boolean {
        val max = Math.max(requiredBits.capacity(), capacity()) - 1
        return (0..max).none { requiredBits[it] && !this[it] }
    }

    /**
     * @return {@code true} if one or more of the passed bits are set.
     * @Todo optimization candidate
     */
    fun containsAny(requiredBits: Bits): Boolean {
        val max = Math.max(requiredBits.capacity(), capacity()) - 1
        return (0..max).any { requiredBits[it] && this[it] }
    }

    /**
     * @return {@code true} if structurally identical. Ignores capacity.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Bits) return false
        return bits contentEquals other.bits
    }

    override fun hashCode(): Int {
        var result = 1
        for (value in bits) {
            result = 31 * result + value
        }
        return result
    }

    /**
     * Reset all bits to false.
     * Leaves capacity intact.
     */
    fun clear() {
        if (!pristine) {
            for (i in 0..bits.size - 1) {
                bits[i] = 0
            }
            pristine = true
        }
    }

    /**
     * Access array backing this bit array. Read only use only!
     */
    fun rawBits(): IntArray = bits

    /**
     * @param function to call for each set bit.
     */
    inline fun forTrue(function: (Int) -> Unit) {
        var bitOffset = 0
        for (value in rawBits()) {
            if (value != 0) {
                for (bit in 0..31) {
                    if (value.getBit(bit)) {
                        function(bit + bitOffset)
                    }
                }
            }
            bitOffset += BIT_SIZE
        }
    }
}