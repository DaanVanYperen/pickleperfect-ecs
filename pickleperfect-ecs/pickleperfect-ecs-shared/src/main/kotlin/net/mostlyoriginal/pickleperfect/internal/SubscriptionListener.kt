package net.mostlyoriginal.pickleperfect.internal

import net.mostlyoriginal.pickleperfect.common.Bits

interface SubscriptionListener {
    // all added entity bits.
    fun added(entities: Bits)

    // all removed entity bits.
    fun removed(entities: Bits)
}