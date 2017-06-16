package net.mostlyoriginal.pickleperfect.jam.api

import com.badlogic.gdx.Screen
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

/**
 * @author Daan van Yperen
 */
class LibGdxApplication(game: Game) : Application(game) {
    override fun initialize() {
        game.initialize()
        LwjglApplication(GameAdapter(), createConfig())
    }

    private fun createConfig(): LwjglApplicationConfiguration {
        val config = LwjglApplicationConfiguration()
        config.width = 800
        config.height = 600
        config.title = "Jam game"
        return config
    }

    inner class ScreenAdapter : Screen {
        override fun hide() {
        }

        override fun show() {
        }

        override fun render(delta: Float) {
            game.process(delta)
        }

        override fun pause() {
        }

        override fun resume() {
        }

        override fun resize(p0: Int, p1: Int) {
        }

        override fun dispose() {
            game.dispose()
        }
    }

    inner class GameAdapter : com.badlogic.gdx.Game() {
        override fun create() {
            Application.gfx = LibGdxGraphics()
            setScreen(ScreenAdapter())
        }
    }
}