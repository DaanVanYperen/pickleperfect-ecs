package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.IgnoreComponentMutationListener
import net.mostlyoriginal.pickleperfect.TestComponent1
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * @author Daan van Yperen
 */
class ComponentStoreTest {

    @Test
    fun When_getting_missing_component_Should_return_new_component() {
        val store = ComponentStore(IgnoreComponentMutationListener(), ::TestComponent1, 0)
        val entityId = 0
        assertNotNull(store.get(entityId))
    }

    @Test
    fun When_getting_same_component_twice_Should_return_same_component_twice() {
        val store = ComponentStore(IgnoreComponentMutationListener(), ::TestComponent1, 0)
        val entityId = 0
        assertEquals(store.get(entityId), store.get(entityId))
    }

    @Test
    fun When_check_missing_component_exists_Should_return_false() {
        val store = ComponentStore(IgnoreComponentMutationListener(), ::TestComponent1, 0)
        val entityId = 0
        assertFalse(store.has(entityId))
    }

    @Test
    fun When_check_existing_component_exists_Should_return_true() {
        val store = ComponentStore(IgnoreComponentMutationListener(), ::TestComponent1, 0)
        val entityId = 0
        store.create(entityId)
        assertTrue(store.has(entityId))
    }

    @Test
    fun When_deleting_component_Should_forget_about_it() {
        val store = ComponentStore(IgnoreComponentMutationListener(), ::TestComponent1, 0)
        val entityId = 0
        store.create(entityId)
        store.remove(entityId)
        assertFalse(store.has(entityId))
    }

    @Test
    fun Should_draw_components_from_pool() {
        val store = ComponentStore(IgnoreComponentMutationListener(), ::TestComponent1, 0)
        val entityId = 0
        val compA = store.create(entityId)
        store.remove(entityId) // should end up in pool, ready for recycle.
        val compB = store.create(entityId) // recycled!
        assertTrue(compA === compB)
    }

}