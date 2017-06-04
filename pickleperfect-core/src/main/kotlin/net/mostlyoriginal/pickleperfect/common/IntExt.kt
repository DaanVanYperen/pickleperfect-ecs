package net.mostlyoriginal.pickleperfect.common

/** @return {@code true} if bit set. */
fun Int.getBit(bit: Int): Boolean = this and (1 shl (bit)) != 0
/** @return int with bit set */
fun Int.setBit(bit: Int): Int = this or (1 shl bit)
/** @return int with bit cleared */
fun Int.clearBit(bit: Int): Int = this and (1 shl bit).inv()