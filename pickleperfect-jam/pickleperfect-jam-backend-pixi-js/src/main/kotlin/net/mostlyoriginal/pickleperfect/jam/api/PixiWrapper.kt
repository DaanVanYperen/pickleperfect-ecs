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
    }
}
