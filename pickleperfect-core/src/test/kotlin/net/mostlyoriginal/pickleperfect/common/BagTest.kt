package net.mostlyoriginal.pickleperfect.common

import net.mostlyoriginal.pickleperfect.Component
import net.mostlyoriginal.pickleperfect.TestComponent1
import net.mostlyoriginal.pickleperfect.TestComponent2
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * @author Daan van Yperen
 */
class BagTest {

    @Test
    fun When_getting_missing_value_Should_return_null() {
        val bag = Bag<Component>(startingCapacity = 1)
        assertEquals(1, bag.capacity())
        assertNull(bag[1000])
        assertEquals(1, bag.capacity())
    }

    @Test
    fun When_setting_outside_capacity_Should_grow_to_required_capacity() {
        val bag = Bag<Component>(startingCapacity = 1)
        assertEquals(1, bag.capacity())
        bag[99] = TestComponent2()
        assertTrue(bag.capacity() >= 99)
        assertNotNull(bag[99])
    }

    @Test
    fun Should_retain_values_after_growth() {
        val bag = Bag<Component>(startingCapacity = 1)
        val c1 = TestComponent1()
        val c2 = TestComponent2()
        bag.add(c1)
        // growth!
        bag.add(c2)

        assertEquals(bag[0], c1)
        assertEquals(bag[1], c2)
    }
}