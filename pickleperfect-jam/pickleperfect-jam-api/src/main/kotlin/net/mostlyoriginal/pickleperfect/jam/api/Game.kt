package net.mostlyoriginal.pickleperfect.jam.api

/**
 * @author Daan van Yperen
 */
interface Game {
    fun initialize()
    fun dispose()
    fun process(delta: Float): Boolean
}