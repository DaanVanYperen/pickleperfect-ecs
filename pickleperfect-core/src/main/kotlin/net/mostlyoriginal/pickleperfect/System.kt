package net.mostlyoriginal.pickleperfect

/**
 * @author Daan van Yperen
 */
interface System {
    fun process(e: E)
    fun begin() {}
    fun end() {}
}