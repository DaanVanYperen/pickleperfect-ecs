package net.mostlyoriginal.pickleperfect.internal

import net.mostlyoriginal.pickleperfect.TestComponent1
import net.mostlyoriginal.pickleperfect.TestComponent2
import net.mostlyoriginal.pickleperfect.TestComponent3
import net.mostlyoriginal.pickleperfect.TestUniverse.Companion.testComponentBitLookup
import net.mostlyoriginal.pickleperfect.common.Bits
import net.mostlyoriginal.pickleperfect.common.EntityPattern
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author Daan van Yperen
 */
class EntityPatternCompilerTest {

    internal val compiler = EntityPatternCompiler(::testComponentBitLookup)

    @Test
    fun Should_support_all_matches() {
        val predicate = compiler.compile(EntityPattern().all(TestComponent1::class, TestComponent3::class))
        assertTrue(predicate.matches(Bits.of(true, false, true))) // comp1 = bit0, comp2 = bit1, etc.
    }

    @Test
    fun Should_support_any_matches() {
        val predicate = compiler.compile(EntityPattern().any(TestComponent1::class, TestComponent3::class))
        assertTrue(predicate.matches(Bits.of(true)))
    }

    @Test
    fun Should_support_none_matches() {
        val predicate = compiler.compile(EntityPattern().none(TestComponent1::class, TestComponent3::class))
        assertFalse(predicate.matches(Bits.of(true)))
        assertFalse(predicate.matches(Bits.of(false, false, true)))
        assertTrue(predicate.matches(Bits.of(false)))
    }


    @Test
    fun Should_support_combined_matches() {
        val predicate = compiler.compile(
                EntityPattern()
                        .any(TestComponent2::class)
                        .none(TestComponent1::class))
        assertFalse(predicate.matches(Bits.of(true, true)))
        assertTrue(predicate.matches(Bits.of(false, true)))
    }
}