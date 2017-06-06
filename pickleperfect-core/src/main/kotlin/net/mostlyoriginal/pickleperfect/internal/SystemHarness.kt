package net.mostlyoriginal.pickleperfect.internal

import net.mostlyoriginal.pickleperfect.System
import net.mostlyoriginal.pickleperfect.World
import net.mostlyoriginal.pickleperfect.predicate.BitPredicate

/**
 * Companion for System instances.
 *
 * @author Daan van Yperen
 */
class SystemHarness(world: World, val system: System) {
    val facade = WorldFacade(world, system)

    /** Initialize contained system. */
    fun initialize() {
        system.initialize(facade)
    }

    /** Process contained system. */
    fun process() {
        system.process(facade)
    }
}