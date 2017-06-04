package net.mostlyoriginal.pickleperfect

/**
 * @author Daan van Yperen
 */
class TestComponent1(var text: String = "") : Component {
    override fun reset() {
        text = ""
    }
}

class TestComponent2 : Component
class TestComponent3 : Component

class TestCountSystem : System {
    var countBegin = 0
    var countProcess = 0
    var countEnd = 0
    override fun begin() {
        countBegin++
    }

    override fun process(e: E) {
        countProcess++
    }

    override fun end() {
        countEnd++
    }
}