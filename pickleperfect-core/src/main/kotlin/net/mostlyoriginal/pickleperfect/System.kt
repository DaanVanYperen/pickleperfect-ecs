package net.mostlyoriginal.pickleperfect

import net.mostlyoriginal.pickleperfect.internal.SystemHarness
import net.mostlyoriginal.pickleperfect.internal.WorldFacade

/**
 * @author Daan van Yperen
 */
interface System {
    fun initialize(w: WorldFacade) {}
    fun process(w: WorldFacade)
}