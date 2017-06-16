package net.mostlyoriginal.game

import net.mostlyoriginal.pickleperfect.System
import net.mostlyoriginal.pickleperfect.World
import net.mostlyoriginal.pickleperfect.WorldConfiguration
import net.mostlyoriginal.pickleperfect.internal.WorldFacade
import net.mostlyoriginal.pickleperfect.jam.api.Application
import net.mostlyoriginal.pickleperfect.jam.api.Game

/**
 * @author Daan van Yperen
 */
class TestGame : Game {
    lateinit var world: World

    override fun initialize() {
        world = World(WorldConfiguration()
                .with(ClearScreenSysystem())
                .with(TestSystem()))
    }

    override fun dispose() {
    }

    override fun process(delta: Float): Boolean {
        world.delta = delta
        world.process()
        return true
    }

    class ClearScreenSysystem : System {
        override fun process(w: WorldFacade) {
            Application.gfx.clearScreen(0f, 1f, 0f, 1f)
        }
    }

    class TestSystem : System {
        override fun process(w: WorldFacade) {
            println("Hello, world!")
        }
    }
}