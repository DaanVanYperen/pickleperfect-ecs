package net.mostlyoriginal.pickleperfect.jam.api

import kotlin.browser.document

/**
 * @author Daan van Yperen
 */
class PixiApplication(game: Game) : Application(game) {
    override fun initialize() {

        val options = Options()

        options.backgroundColor = "0x00FF00"
        options.width = 800
        options.height = 600

        val application = PIXI.Application(
                options = options
        )

        document.body!!.appendChild(application.view)

//        game.initialize()
//        game.process(0F)
//        game.dispose()
    }
}