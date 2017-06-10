package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.*
import net.mostlyoriginal.pickleperfect.common.AccessingRecycledEntityException
import net.mostlyoriginal.pickleperfect.internal.Deleted
import net.mostlyoriginal.pickleperfect.internal.WorldFacade
import org.junit.Test
import kotlin.test.*

/**
 * Integration test.
 *
 * @author Daan van Yperen
 */
class EntityServiceTest {

    data class TestWorld(
            val facade: WorldFacade,
            val m1: ComponentStore<TestComponent1>,
            val m2: ComponentStore<TestComponent2>,
            val m3: ComponentStore<TestComponent3>,
            val mDeleted: ComponentStore<Deleted>)

    private fun createFacade(): TestWorld {
        val world = World(WorldConfiguration()
                .with(TestComponent1::class, ::TestComponent1)
                .with(TestComponent2::class, ::TestComponent2)
                .with(TestComponent3::class, ::TestComponent3))
        return TestWorld(WorldFacade(world),
                world.componentService.getStore(TestComponent1::class),
                world.componentService.getStore(TestComponent2::class),
                world.componentService.getStore(TestComponent3::class),
                world.componentService.getStore(Deleted::class)
        )
    }

    @Test
    fun Should_recycle_deleted_entities() {
        val (f) = createFacade()
        val entityId = f.create().id
        f.delete(entityId)
        f.flush()
        assertEquals(entityId, f.create().id)
    }

    @Test
    fun Should_cleanup_all_components_on_flush() {
        val (f, m1, m2, m3, mDeleted) = createFacade()

        val entityId = f.create().id

        m1.create(entityId)
        m2.create(entityId)
        m3.create(entityId)
        f.delete(entityId)
        f.flush()

        assertFalse(m1.has(entityId))
        assertFalse(m2.has(entityId))
        assertFalse(m3.has(entityId))
        assertTrue(mDeleted.has(entityId))
    }

    @Test
    fun When_entity_undeleted_before_flush_Should_not_finalize_deletion() {
        val (f, m1, m2, m3, mDeleted) = createFacade()

        val entityId = f.create().id

        m1.create(entityId)
        f.delete(entityId)
        mDeleted.remove(entityId)

        f.flush()

        assertNotNull(f.get(entityId))
        assertTrue(m1.has(entityId))
        assertFalse(mDeleted.has(entityId))
    }

    /*** Entities should stay fully available until all subscriptions have been updated and flush has been completed. */
    @Test
    fun When_deleting_entity_without_flush_Should_retain_all_components() {
        val (f, m1, m2, m3, mDeleted) = createFacade()

        val entityId = f.create().id

        m1.create(entityId)
        m2.create(entityId)
        m3.create(entityId)

        f.delete(entityId)

        assertNotNull(f.get(entityId))
        assertTrue(m1.has(entityId))
        assertTrue(m2.has(entityId))
        assertTrue(m3.has(entityId))
        assertTrue(mDeleted.has(entityId))
    }

    /*** we want devs to discover calls to deleted ids. */
    @Test
    fun When_getting_flushed_deleted_entity_Should_throw_exception() {
        val e = assertFails {
            val (f, m1, m2, _, mDeleted) = createFacade()

            val entityId = f.create().id

            m1.create(entityId)
            m2.create(entityId)
            f.delete(entityId)
            f.flush()
            f.get(entityId)
        }
        assertTrue(e is AccessingRecycledEntityException)
    }
}