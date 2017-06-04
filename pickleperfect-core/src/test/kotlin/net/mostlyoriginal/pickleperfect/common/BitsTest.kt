package net.mostlyoriginal.pickleperfect.common

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
    fun Should_properly_grow_to_requested_size() {
        val bits = Bits()
        bits.growIfNeeded(Bits.BIT_SIZE)
        assertTrue(bits.capacity() == Bits.BIT_SIZE)
        bits.growIfNeeded(Bits.BIT_SIZE+1)
        assertTrue(bits.capacity() >= Bits.BIT_SIZE*2)
        bits.growIfNeeded(Bits.BIT_SIZE*4)
        assertTrue(bits.capacity() >= Bits.BIT_SIZE*4)
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

}