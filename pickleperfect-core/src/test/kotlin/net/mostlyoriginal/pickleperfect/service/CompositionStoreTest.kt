package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.common.Bits
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


/**
 * @author Daan van Yperen
 */
class CompositionStoreTest {
    @Test
    fun When_looking_up_empty_entity_Should_be_composition_id_0() {
        val entityId = 1
        assertEquals(0, CompositionStore().getCompositionId(entityId))
    }

    @Test
    fun When_registering_empty_entity_Should_be_composition_id_0() {
        val entityId = 1
        val store = CompositionStore()
        store.registerCompositionChange(entityId, Bits())
        assertEquals(0, store.getCompositionId(entityId))
    }

    @Test
    fun When_registering_compositionally_identical_entities_Should_have_matching_composition_id() {
        val store = CompositionStore()
        val entityId = 1
        store.registerCompositionChange(entityId, Bits.of(true, false, true))
        store.registerCompositionChange(entityId + 1, Bits.of(true, false, true))

        assertEquals(store.getCompositionId(entityId), store.getCompositionId(entityId + 1))
    }

    @Test
    fun When_registering_compositionally_different_entities_Should_not_match_composition_id() {
        val store = CompositionStore()
        val entityId = 1
        store.registerCompositionChange(entityId, Bits.of(true, false, true))
        store.registerCompositionChange(entityId + 1, Bits.of(true))

        assertNotEquals(store.getCompositionId(entityId), store.getCompositionId(entityId + 1))
    }
}