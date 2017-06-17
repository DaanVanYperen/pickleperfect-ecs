package net.mostlyoriginal.pickleperfect.jam.api

import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.Node

external class Object


@JsName("Object")
external class Options {
    var width: Int = definedExternally
    var height: Int = definedExternally
    var backgroundColor: String = definedExternally
}

@JsName("PIXI")
external object PIXI {

    @JsName("Application")
    class Application(
            options: Any? = definedExternally,
            arg2: Any? = definedExternally,
            arg3: Any? = definedExternally,
            arg4: Boolean? = definedExternally,
            arg5: Boolean? = definedExternally) {
        var view: Node = definedExternally
        var stage: Container = definedExternally
        var ticker : Ticker = definedExternally

        class Ticker {
            fun add(fn: (speed : Float) -> Unit): Unit = definedExternally
        }

        fun render(): Unit = definedExternally
    }

    @JsName("Text")
    class Text(text: Any? = definedExternally) : Sprite {
    }

    @JsName("Sprite")
    open class Sprite : Container {
        var x: Float = definedExternally
        var y: Float = definedExternally
        var width: Float = definedExternally
        var height: Float = definedExternally

        companion object {
            fun fromImage(imageId: String): Sprite = definedExternally
        }
    }

    @JsName("Container")
    open class Container : DisplayObject {
        fun addChild(child: DisplayObject): DisplayObject = definedExternally
    }

    @JsName("DisplayObject")
    open class DisplayObject
}
