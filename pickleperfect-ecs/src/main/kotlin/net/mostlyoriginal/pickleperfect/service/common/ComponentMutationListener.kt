package net.mostlyoriginal.pickleperfect.service.common

/**
 * Listener for composition changes.
 *
 * @author Daan van Yperen
 */
interface ComponentMutationListener {
    fun componentAdded(entityId : Int, componentBit : Int)
    fun componentRemoved(entityId : Int, componentBit : Int)
}