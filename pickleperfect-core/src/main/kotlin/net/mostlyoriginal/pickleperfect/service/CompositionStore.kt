package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.common.*

/**
 * Tracks unique entity compositions.
 *
 * Each {P@see Bits} (distinct by content) gets assigned a unique composition ID.
 *
 * @todo perhaps introduce CompositionType with ordinal and bits, for reuse throughout.
 * @author Daan van Yperen
 */
class CompositionStore {
    // @todo Bits are mutable. Unreliable as index!
    val types = OrdinalTypeStore <Bits>();
    private val entityCompositionTypes = Bag<Int>()
    private val EMPTY_COMPOSITION = types.getOrCreate(Bits())

    /**
     * Register changes to entity composition.
     *
     * @param entityId entityId to override composition ID.
     * @param composition current composition of entity.
     * @return update composition ID for entity.
     */
    fun registerCompositionChange(entityId: Int, composition: Bits) {
        entityCompositionTypes[entityId] = types.getOrCreate(composition, Bits::copy)
    }

    /**
     * @return get composition ID for entity. Returns empty composition for unknown entities.
     * @todo empty composition might not be undesirable. Might be though, since entities are only registered after a composition change.
     */
    fun getCompositionId(entityId: Int): Int = entityCompositionTypes[entityId] ?: EMPTY_COMPOSITION

    /**
     * @return highest in-use composition ID.
     */
    fun highestId() = types.highestId()

    /**
     * Iterate over all type values
     * @param function Takes composition id, and related bits.
     */
    inline fun forEach(start: Int, endInclusive: Int, function: (Int, Bits) -> Unit) {
        for (index in start..endInclusive) {
            function(index, types.getValue(index))
        }
    }

    /**
     * Iterate over all type values
     * @param function Takes composition id, and related bits.
     */
    inline fun forEach(function: (Int, Bits) -> Unit) {
        for (index in 0..types.highestId()) {
            function(index, types.getValue(index))
        }
    }
}