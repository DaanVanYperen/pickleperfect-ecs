package net.mostlyoriginal.pickleperfect

import net.mostlyoriginal.pickleperfect.common.EntityPattern
import net.mostlyoriginal.pickleperfect.internal.WorldFacade
import net.mostlyoriginal.pickleperfect.service.ComponentStore
import org.junit.Test
import kotlin.test.assertEquals

typealias M<C> = ComponentStore<C>

/**
 * @author Daan van Yperen
 */
class WorldIntegrationTest {

    @Test
    fun Should_support_entities_created_during_initialization() {

        class BasicSystem : System {

            var entitiesProcessed = 0
            val pattern =
                    EntityPattern()
                            .any(TestComponent1::class)

            private var mTestComponent1: ComponentStore<TestComponent1>? = null

            override fun initialize(w: WorldFacade) {
                mTestComponent1 = w.getMapper(TestComponent1::class)
                val entity = w.create()
                mTestComponent1?.create(entity.id)
            }

            override fun process(w: WorldFacade) {
                w.forEach(pattern) {
                    entitiesProcessed++
                }
            }
        }

        val basicSystem = BasicSystem()
        val config = WorldConfiguration()
                .with(TestComponent1::class, ::TestComponent1)
                .with(basicSystem)
        val world = World(config)
        world.process()
        assertEquals(1, basicSystem.entitiesProcessed)
    }


    @Test
    fun Should_support_entities_created_during_process() {

        class BasicSystem : System {

            var entitiesProcessed = 0
            val pattern =
                    EntityPattern()
                            .any(TestComponent1::class)

            private var mTestComponent1: ComponentStore<TestComponent1>? = null

            override fun initialize(w: WorldFacade) {
                mTestComponent1 = w.getMapper(TestComponent1::class)
                val entity = w.create()
                mTestComponent1?.create(entity.id)
            }

            override fun process(w: WorldFacade) {
                val entity = w.create()
                mTestComponent1?.create(entity.id)

                w.forEach(pattern) {
                    entitiesProcessed++
                }

            }
        }

        val basicSystem = BasicSystem()
        val config = WorldConfiguration()
                .with(TestComponent1::class, ::TestComponent1)
                .with(basicSystem)
        val world = World(config)
        world.process()
        assertEquals(1, basicSystem.entitiesProcessed)
        world.process()
        assertEquals(3, basicSystem.entitiesProcessed)
    }
}
