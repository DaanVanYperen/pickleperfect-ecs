package net.mostlyoriginal.game

import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.System
import net.mostlyoriginal.pickleperfect.World
import net.mostlyoriginal.pickleperfect.WorldConfiguration
import net.mostlyoriginal.pickleperfect.common.Bits
import net.mostlyoriginal.pickleperfect.common.EntityPattern
import net.mostlyoriginal.pickleperfect.emu.Math
import net.mostlyoriginal.pickleperfect.internal.SubscriptionListener
import net.mostlyoriginal.pickleperfect.internal.WorldFacade
import net.mostlyoriginal.pickleperfect.jam.api.Application
import net.mostlyoriginal.pickleperfect.jam.api.Game
import net.mostlyoriginal.pickleperfect.service.ComponentStore

/**
 * @author Daan van Yperen
 */
class TestGame(val configure: (P: WorldConfiguration) -> Unit) : Game {
    lateinit var world: World

    override fun initialize() {
        val config = WorldConfiguration()
                .with(Pos::class, ::Pos)
                .with(Sprite::class, ::Sprite)
                .with(ClearScreenSystem())
                .with(TestSystem())
        configure.invoke(config)
        world = World(config)
    }

    override fun dispose() {
    }

    override fun process(delta: Float): Boolean {
        world.delta = delta
        world.process()
        return true
    }

    class ClearScreenSystem : System {
        override fun process(w: WorldFacade) {
            Application.gfx.clearScreen(0f, 1f, 0f, 1f)
        }
    }

    class TestSystem : System {
        val pattern = EntityPattern().all(Pos::class, Sprite::class)
        private lateinit var mPos: ComponentStore<Pos>

        override fun initialize(w: WorldFacade) {
            super.initialize(w)

            mPos = w.getMapper(Pos::class)
            val mSprite = w.getMapper(Sprite::class)

            val entity = w.create()
            mPos.get(entity.id).x = 15f
            mPos.get(entity.id).y = 15f
            mSprite.get(entity.id).name = "dancingman.png"
        }

        override fun process(w: WorldFacade) {
            w.getSubscription(pattern).forEach {
                mPos.get(it).x = (mPos.get(it).x + 1) % 500
            }
        }
    }

    class Pos : Component {
        var x: Float = 0F
        var y: Float = 0F
    }

    class Sprite : Component {
        var name: String = ""
    }
}