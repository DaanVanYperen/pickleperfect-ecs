package net.mostlyoriginal.game

import net.mostlyoriginal.pickleperfect.System
import net.mostlyoriginal.pickleperfect.World
import net.mostlyoriginal.pickleperfect.WorldConfiguration
import net.mostlyoriginal.pickleperfect.internal.WorldFacade

/**
 * @author Daan van Yperen
 */
class Game {

    class TestSystem : System {
        override fun process(w: WorldFacade) {
            println("Hello, world!")
        }
    }

    fun process() {
        World(WorldConfiguration()
                .with(TestSystem()))
                .process()
    }

}