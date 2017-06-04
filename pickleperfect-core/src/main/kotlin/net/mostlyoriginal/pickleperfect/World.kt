package net.mostlyoriginal.pickleperfect

import net.mostlyoriginal.pickleperfect.service.ComponentService

/**
 * @author Daan van Yperen
 */
class World(config: WorldConfiguration) {
    val systems = config.systems
    val componentService = ComponentService()

    fun process() {
        for (system in systems) {
            system.begin()
            system.end()
        }
    }
}