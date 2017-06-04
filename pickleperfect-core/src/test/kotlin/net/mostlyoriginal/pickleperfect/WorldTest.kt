package net.mostlyoriginal.pickleperfect

import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Daan van Yperen
 */
class WorldTest {

    @Test
    fun When_process_basic_world_Then_systems_get_called() {
        val countSystem = TestCountSystem()
        val config = WorldConfiguration()
                .with(countSystem)
        val world = World(config)
        world.process()
        assertEquals(1, countSystem.countBegin)
    }
}