package net.mostlyoriginal.pickleperfect

import net.mostlyoriginal.pickleperfect.internal.WorldFacade
import net.mostlyoriginal.pickleperfect.service.common.ComponentMutationListener
import kotlin.reflect.KClass

/**
 * @author Daan van Yperen
 */
class TestComponent1(var text: String = "") : Component {
    constructor() : this("")

    override fun reset() {
        text = ""
    }
}

class TestComponent2 : Component
class TestComponent3 : Component

class TestCountSystem : System {
    var countCalls = 0
    override fun process(w: WorldFacade) {
        countCalls++
    }
}

class ScopedSystem : System {
    override fun process(w: WorldFacade) {
    }
}

class IgnoreComponentMutationListener : ComponentMutationListener {
    override fun componentRemoved(entityId: Int, componentBit: Int) {
    }

    override fun componentAdded(entityId: Int, componentBit: Int) {
    }
}

class TestUniverse {
    companion object {
        fun testComponentBitLookup(type: KClass<out Component>): Int {
            if (type == TestComponent1::class) return 0
            if (type == TestComponent2::class) return 1
            if (type == TestComponent3::class) return 2
            throw RuntimeException("Unknown type")
        }
    }

}