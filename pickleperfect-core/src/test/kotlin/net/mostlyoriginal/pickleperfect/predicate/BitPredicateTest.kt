package net.mostlyoriginal.pickleperfect.predicate

import net.mostlyoriginal.pickleperfect.common.Bits
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author Daan van Yperen
 */
class BitPredicateTest {

    @Test
    fun ContainsAll_with_no_matching_bits_Should_return_false() {
        val match = Bits()
        match[1] = true
        match[2] = true
        match[Bits.BIT_SIZE+10] = true

        val predicate = ContainsAllBitPredicate(match)

        val candidate = Bits()
        candidate[1] = true
        candidate[Bits.BIT_SIZE+10] = true

        assertFalse(predicate.matches(candidate))
    }


    @Test
    fun ContainsAll_with_at_least_all_requested_bits_matching_Should_return_true() {
        val match = Bits()
        match[1] = true
        match[2] = true
        match[Bits.BIT_SIZE+10] = true

        val predicate = ContainsAllBitPredicate(match)

        val candidate = Bits()
        candidate[1] = true
        candidate[2] = true
        candidate[3] = true
        candidate[Bits.BIT_SIZE+10] = true

        assertTrue(predicate.matches(candidate))
    }


    @Test
    fun ContainsAny_with_no_similar_bits_Should_return_false() {
        val match = Bits()
        match[1] = true
        match[2] = true

        val predicate = ContainsAllBitPredicate(match)

        val candidate = Bits()
        candidate[10] = true
        candidate[11] = true

        assertFalse(predicate.matches(candidate))
    }


    @Test
    fun ContainsAny_with_matching_bit_Should_return_true() {
        val match = Bits()
        match[Bits.BIT_SIZE+10] = true
        match[8] = true

        val predicate = ContainsAnyBitPredicate(match)

        val candidate = Bits()
        candidate[1] = true
        candidate[2] = true
        candidate[Bits.BIT_SIZE+10] = true

        assertTrue(predicate.matches(candidate))
    }



}