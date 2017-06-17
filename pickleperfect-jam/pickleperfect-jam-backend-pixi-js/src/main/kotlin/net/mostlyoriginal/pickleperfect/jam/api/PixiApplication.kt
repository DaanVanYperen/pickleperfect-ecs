package net.mostlyoriginal.pickleperfect.jam.api

import kotlin.browser.document

/**
 * @author Daan van Yperen
 */
class PixiApplication(game: Game) : Application(game) {

    companion object {
        lateinit var app: PIXI.Application
    }

    fun process() {
        game.process(0F)
    }

    override fun initialize() {

        val options = Options()

        options.backgroundColor = "0x00FF00"
        options.width = 800
        options.height = 600

        app = PIXI.Application(
                options = options
        )

        gfx = PixiGraphics()

        document.body!!.appendChild(app.view)

        game.initialize()
        app.ticker.add {
            f -> game.process(60 / f) // f = 1.0 if 100%, 0.5 = 50% target FPS.
            }
        game.dispose()
    }
}