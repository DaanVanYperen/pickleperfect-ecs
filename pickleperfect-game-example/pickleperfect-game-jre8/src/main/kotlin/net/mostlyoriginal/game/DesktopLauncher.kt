package net.mostlyoriginal.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.System
import net.mostlyoriginal.pickleperfect.common.Bits
import net.mostlyoriginal.pickleperfect.common.EntityPattern
import net.mostlyoriginal.pickleperfect.internal.SubscriptionListener
import net.mostlyoriginal.pickleperfect.internal.WorldFacade
import net.mostlyoriginal.pickleperfect.jam.api.Application
import net.mostlyoriginal.pickleperfect.jam.api.LibGdxApplication
import net.mostlyoriginal.pickleperfect.service.ComponentStore
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera


/**
 * @author Daan van Yperen
 */

fun main(args: Array<String>) {
    LibGdxApplication(TestGame(
            { c ->
                c.with(LibGdxSprite::class, ::LibGdxSprite)
                c.with(LibGdxRenderSystem())
            }
    )).run()
}

class GdxActor(var region: TextureRegion) : Actor() {
    @Override
    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha)
        batch.draw(region, x, y, originX, originY,
                width, height, scaleX, scaleY, rotation)
    }
}

class LibGdxSprite : Component {
    lateinit var actor: GdxActor
}

class LibGdxRenderSystem : System, SubscriptionListener {

    private lateinit var mLibGdxSprite: ComponentStore<LibGdxSprite>
    private lateinit var mPos: ComponentStore<TestGame.Pos>
    private lateinit var mSprite: ComponentStore<TestGame.Sprite>

    val pattern = EntityPattern()
            .all(TestGame.Pos::class, TestGame.Sprite::class)

    private lateinit var stage: Stage

    override fun added(entities: Bits) {
        entities.forTrue {
            val libGdxSprite = mLibGdxSprite.create(it)
            val region = TextureRegion(Texture(mSprite.get(it).name))
            region.flip(false, true)
            libGdxSprite.actor = GdxActor(region)
            libGdxSprite.actor.width = region.regionWidth.toFloat()
            libGdxSprite.actor.height = region.regionHeight.toFloat()
            stage.addActor(libGdxSprite.actor)
        }
    }

    override fun removed(entities: Bits) {
        entities.forTrue {
            mLibGdxSprite.remove(it)
        }
    }

    override fun initialize(w: WorldFacade) {

        // setup mappers.
        mLibGdxSprite = w.getMapper(LibGdxSprite::class)
        mPos = w.getMapper(TestGame.Pos::class)
        mSprite = w.getMapper(TestGame.Sprite::class)

        // make sure we get informed of updates.
        w.getSubscription(pattern).register(this)

        stage = Stage()
        (stage.camera as OrthographicCamera).setToOrtho(true)
    }

    override fun process(w: WorldFacade) {
        Application.gfx.clearScreen(0f, 1f, 0f, 1f)

        // move all sprites around.
        w.getSubscription(pattern).forEach {
            val pos = mPos.get(it)
            val sprite = mLibGdxSprite.create(it)
            sprite.actor.x = pos.x
            sprite.actor.y = pos.y
        }

        stage.act(w.delta())
        stage.draw()
    }
}


