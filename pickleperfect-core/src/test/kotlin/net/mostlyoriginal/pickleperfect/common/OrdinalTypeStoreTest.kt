package net.mostlyoriginal.pickleperfect.common

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * @author Daan van Yperen
 */
class OrdinalTypeStoreTest {

    @Test
    fun Test_highest_id() {
        val store = OrdinalTypeStore<Bits>()
        store.getOrCreate(Bits.of(true), ::Bits)
        assertEquals(0, store.highestId())
        store.getOrCreate(Bits.of(false,true), ::Bits)
        assertEquals(1, store.highestId())
    }

    @Test
    fun Should_return_incremental_index_for_strucurally_different_subjects() {
        val store = OrdinalTypeStore<Bits>()

        val a = Bits.of(true)
        val b = Bits.of(false, true)
        val c = Bits.of(false, true, true)

        assertEquals(0, store.getOrCreate(a, ::Bits))
        assertEquals(0, store.getOrCreate(a, ::Bits))
        assertEquals(1, store.getOrCreate(b, ::Bits))
        assertEquals(1, store.getOrCreate(b, ::Bits))
        assertEquals(2, store.getOrCreate(c, ::Bits))
        assertEquals(2, store.getOrCreate(c, ::Bits))
    }

    @Test
    fun Should_return_same_index_for_identical_object() {
        val store = OrdinalTypeStore<Bits>()

        val a = Bits.of(true)

        assertEquals(
                store.getOrCreate(a, ::Bits),
                store.getOrCreate(a, ::Bits))
    }

    @Test
    fun Should_identify_by_structural_equality() {
        val store = OrdinalTypeStore<Bits>()

        val a = Bits.of(true)
        val b = a.copy()

        assertEquals(
                store.getOrCreate(a, ::Bits),
                store.getOrCreate(b, ::Bits))
    }

    @Test
    fun Should_support_mutable_types() {
        val store = OrdinalTypeStore<Bits>()

        val a = Bits.of(true)
        val pristineCopyOfA = a.copy()

        val indexForPristineA = store.getOrCreate(a, ::Bits)

        a[0] = false

        assertEquals(indexForPristineA, store.getOrCreate(pristineCopyOfA, ::Bits))
        assertNotEquals(indexForPristineA, store.getOrCreate(a, ::Bits)) // mismatch, because different now!
    }

}
