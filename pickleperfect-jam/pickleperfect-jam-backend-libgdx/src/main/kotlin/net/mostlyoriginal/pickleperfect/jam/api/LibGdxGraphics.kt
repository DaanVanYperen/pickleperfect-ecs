package net.mostlyoriginal.pickleperfect.jam.api

import com.badlogic.gdx.Gdx

/**
 * @author Daan van Yperen
 */
class LibGdxGraphics : Graphics {
    override fun clearScreen(r: Float, g: Float, b: Float, a: Float) {
        Gdx.gl.glClearColor(r, g, b, a)
        Gdx.gl.glClear(16384)
    }
}