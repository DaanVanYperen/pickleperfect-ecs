package net.mostlyoriginal.pickleperfect.internal

import net.mostlyoriginal.pickleperfect.common.ProcessingStrategy

/**
 * @author Daan van Yperen
 */
class DefaultProcessingStrategy : ProcessingStrategy {

    override fun process(w: WorldFacade) {
        w.flush()
        w.systems().forEach {
            it.process()
            w.flush()
        }
    }
}