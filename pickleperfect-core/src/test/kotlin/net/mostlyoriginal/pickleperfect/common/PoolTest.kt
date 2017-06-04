package net.mostlyoriginal.pickleperfect.common

import net.mostlyoriginal.pickleperfect.TestComponent1
import org.junit.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * @author Daan van Yperen
 */
class PoolTest {

    @Test
    fun Should_instance_new_objects_when_depleted() {
        val pool = Pool({ TestComponent1() })
        assertNotNull(pool.obtain())
    }

    @Test
    fun Should_recycle_released_objects() {
        val pool = Pool({ TestComponent1() })
        val a = pool.obtain()
        pool.release(a)
        val b = pool.obtain()
        assertTrue(a === b)
    }

    @Test
    fun Should_reset_recycled_objects() {
        val pool = Pool({ TestComponent1() })
        val a = pool.obtain()
        a.text = "bla"
        pool.release(a)
        val b = pool.obtain()
        assertNotEquals("bla", b.text)
    }
}