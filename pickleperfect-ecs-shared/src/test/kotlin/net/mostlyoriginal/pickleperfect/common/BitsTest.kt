package net.mostlyoriginal.pickleperfect.common

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * @author Daan van Yperen
 */
class BitsTest {

    @Test
    fun Set_and_get() {
        val bits = Bits()
        bits[0] = true
        assertTrue(bits[0])
    }

    @Test
    fun When_copy_Should_produce_structurally_identical() {
        val bits = Bits()
        bits[30] = true

        val copy = bits.copy()
        assertTrue(copy[30])

        assertEquals(copy, bits)
    }

    @Test
    fun When_equals_on_empty_but_different_sizes_should_report_equal() {
        // content matters, not size.
        val a = Bits(1)
        val b = Bits(10)
        a[8] = true
        b[8] = true
        assertEquals(a, b)
    }

    @Test
    fun When_equals_on_empty_should_report_equal() {
        assertEquals(Bits(), Bits())
    }

    @Test
    fun When_equals_on_strucurally_differen_should_report_not_equal() {
        val a = Bits()
        val b = Bits()
        a[1] = true
        b[2] = true
        assertNotEquals(a, b)
    }

    @Test
    fun Should_properly_grow_to_requested_size() {
        val bits = Bits()
        bits.growIfNeeded(Bits.BIT_SIZE)
        assertTrue(bits.capacity() == Bits.BIT_SIZE)
        bits.growIfNeeded(Bits.BIT_SIZE + 1)
        assertTrue(bits.capacity() >= Bits.BIT_SIZE * 2)
        bits.growIfNeeded(Bits.BIT_SIZE * 4)
        assertTrue(bits.capacity() >= Bits.BIT_SIZE * 4)
    }

    @Test
    fun Should_not_explode_when_setting_bit_near_top_of_container_type_size() {
        val bits = Bits()
        bits[Bits.BIT_SIZE - 1] = true
        assertTrue(bits[Bits.BIT_SIZE - 1])
    }

    @Test
    fun When_set_outside_capacity_Should_grow() {
        val bits = Bits()
        bits[Bits.BIT_SIZE] = true
        assertTrue(bits.capacity() > Bits.BIT_SIZE)
    }

    @Test
    fun When_get_outside_capacity_Should_return_false_and_not_grow() {
        val bits = Bits()
        assertFalse(bits[Bits.BIT_SIZE])
        assertEquals(32, bits.capacity())
    }


    @Test
    fun When_newly_instanced_Should_be_pristine() {
        assertTrue(Bits().pristine)
    }

    @Test
    fun When_changed_Should_no_longer_be_pristine() {
        val bits = Bits.of(true)
        assertFalse(bits.pristine)
    }

    @Test
    fun When_setting_pristine_bitset_to_false_Should_remain_pristine() {
        val bits = Bits()
        bits[0] = false
        assertTrue(bits.pristine)
    }

    @Test
    fun When_cleared_Should_become_pristine() {
        val bits = Bits.of(true)
        bits.clear()
        assertTrue(bits.pristine)
    }
}