package net.mostlyoriginal.pickleperfect.common

import net.mostlyoriginal.pickleperfect.internal.WorldFacade

/**
 * @author Daan van Yperen
 */
interface ProcessingStrategy {

    fun initialize(w: WorldFacade) = {}

    /**
     * When called run systems in desired fashion.
     *
     * While it is up to the discretion of the strategy, systems typically expect to
     * encounter a sane world state when called. Make sure {@code w.flush()} is called
     * before the first system is invoked, and after each system finishes processing.
     */
    fun process(w: WorldFacade)
}
