package net.mostlyoriginal.pickleperfect

/**
 * @author Daan van Yperen
 */
class WorldConfiguration {
    val systems = ArrayList<System>(32)

    fun with(system: System): WorldConfiguration {
        systems.add(system)
        return this
    }
}