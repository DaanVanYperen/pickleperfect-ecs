package net.mostlyoriginal.pickleperfect.jam.api

/**
 * @author Daan van Yperen
 */
abstract class Application(val game: Game) {

    companion object {
        lateinit var gfx : Graphics
    }

    fun run() {
        initialize()
    }

    protected abstract fun initialize()
}