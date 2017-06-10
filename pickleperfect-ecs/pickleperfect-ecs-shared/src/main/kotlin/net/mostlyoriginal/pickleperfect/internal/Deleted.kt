package net.mostlyoriginal.pickleperfect.internal

import net.mostlyoriginal.pickleperfect.Component

/**
 * Framework internal component.
 *
 * Entities with this component are considered deleted, but can be accessed until game state has updated.
 *
 * @author Daan van Yperen
 */
class Deleted : Component