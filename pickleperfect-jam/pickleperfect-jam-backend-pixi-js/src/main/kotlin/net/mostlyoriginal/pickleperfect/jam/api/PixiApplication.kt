package net.mostlyoriginal.pickleperfect.jam.api

/**
 * @author Daan van Yperen
 */
class PixiApplication(game: Game) : Application(game) {
    override fun initialize() {
        game.initialize()
    }

    override fun process(): Boolean {
        return game.process(delta)
    }

    override fun release() {
        game.dispose()
    }
}