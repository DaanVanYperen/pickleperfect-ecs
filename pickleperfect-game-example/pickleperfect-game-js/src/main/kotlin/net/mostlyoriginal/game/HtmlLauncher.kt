package net.mostlyoriginal.game

import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.System
import net.mostlyoriginal.pickleperfect.common.Bits
import net.mostlyoriginal.pickleperfect.common.EntityPattern
import net.mostlyoriginal.pickleperfect.internal.SubscriptionListener
import net.mostlyoriginal.pickleperfect.internal.WorldFacade
import net.mostlyoriginal.pickleperfect.jam.api.Application
import net.mostlyoriginal.pickleperfect.jam.api.PIXI
import net.mostlyoriginal.pickleperfect.jam.api.PixiApplication
import net.mostlyoriginal.pickleperfect.service.ComponentStore

/**
 * @author Daan van Yperen
 */

fun main(args: Array<String>) {
    PixiApplication(TestGame(
            { c ->
                c.with(HtmlSprite::class, ::HtmlSprite)
                c.with(HtmlRenderSystem())
            }
    )).run()
}


class HtmlSprite : Component {
    lateinit var actor: PIXI.Sprite
}

class HtmlRenderSystem : System, SubscriptionListener {

    private lateinit var mHtmlSprite: ComponentStore<HtmlSprite>
    private lateinit var mPos: ComponentStore<TestGame.Pos>
    private lateinit var mSprite: ComponentStore<TestGame.Sprite>

    val pattern = EntityPattern()
            .all(TestGame.Pos::class, TestGame.Sprite::class)

    private lateinit var stage: PIXI.Container

    override fun added(entities: Bits) {
        entities.forTrue {
            val HtmlSprite = mHtmlSprite.create(it)
            HtmlSprite.actor = PIXI.Sprite.fromImage(mSprite.get(it).name)
            stage.addChild(HtmlSprite.actor)
        }
    }

    override fun removed(entities: Bits) {
        entities.forTrue {
            mHtmlSprite.remove(it)
        }
    }

    override fun initialize(w: WorldFacade) {

        // setup mappers.
        mHtmlSprite = w.getMapper(HtmlSprite::class)
        mPos = w.getMapper(TestGame.Pos::class)
        mSprite = w.getMapper(TestGame.Sprite::class)

        // make sure we get informed of updates.
        w.getSubscription(pattern).register(this)

        stage = PixiApplication.app.stage
    }

    override fun process(w: WorldFacade) {
        Application.gfx.clearScreen(0f, 1f, 0f, 1f)

        // move all sprites around.
        w.getSubscription(pattern).forEach {
            val pos = mPos.get(it)
            val sprite = mHtmlSprite.create(it)
            sprite.actor.x = pos.x
            sprite.actor.y = pos.y
        }

        PixiApplication.app.render()
    }
}


