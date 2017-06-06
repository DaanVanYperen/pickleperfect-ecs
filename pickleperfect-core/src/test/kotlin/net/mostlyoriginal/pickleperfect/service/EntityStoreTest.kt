package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.Entity
import net.mostlyoriginal.pickleperfect.World
import net.mostlyoriginal.pickleperfect.WorldConfiguration
import org.junit.Test
import kotlin.test.*


/**
 * @author Daan van Yperen
 */
class EntityStoreTest {
    val entityId = 1

    @Test
    fun When_create_Should_obtain_instance() {
        val store = defaultStore()
        assertNotNull(store.create(entityId))
    }

    @Test
    fun When_create_twice_Should_obtain_same_instance() {
        val store = defaultStore()
        assertEquals(store.create(entityId), store.create(entityId))
    }

    private fun defaultStore(): EntityStore<Entity> {
        val world = World(WorldConfiguration())
        val store = EntityStore({ id -> Entity(world, id) })
        return store
    }

    @Test
    fun When_active_Then_Should_read_as_active() {
        val store = defaultStore()
        store.create(entityId)
        assertTrue(store.isActive(entityId))
    }

    @Test
    fun When_removed_then_Should_read_inactive() {
        val store = defaultStore()
        store.create(entityId)
        store.remove(entityId)
        assertFalse(store.isActive(entityId))
    }

    @Test
    fun When_creating_after_deleting_entities_Should_recycle_ids() {
        val store = defaultStore()
        store.create(entityId)
        store.remove(entityId)
        assertEquals(entityId, store.create().id)
    }

    @Test
    fun When_recreating_by_id_Should_reclaim_old_instance() {
        val store = defaultStore()
        val instanceA = store.create(entityId)
        store.remove(entityId)
        assertEquals(instanceA, store.create(entityId))
    }

    @Test
    fun When_recycling_ids_Should_properly_cleanup_limbo() {
        val store = defaultStore()
        val instanceA = store.create(entityId)
        store.remove(entityId)
        val reclaimed = store.create()
        assertEquals(instanceA, reclaimed)
        assertTrue(store.isActive(reclaimed.id))
        assertNotEquals(instanceA, store.create())
    }
}