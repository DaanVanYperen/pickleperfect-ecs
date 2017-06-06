package net.mostlyoriginal.pickleperfect

import net.mostlyoriginal.pickleperfect.common.Bag
import net.mostlyoriginal.pickleperfect.common.ProcessingStrategy
import net.mostlyoriginal.pickleperfect.internal.Subscription
import net.mostlyoriginal.pickleperfect.internal.SystemHarness
import net.mostlyoriginal.pickleperfect.internal.WorldFacade
import net.mostlyoriginal.pickleperfect.predicate.BitPredicate
import net.mostlyoriginal.pickleperfect.service.*
import kotlin.reflect.KClass

/**
 * @author Daan van Yperen
 */
class World(config: WorldConfiguration) {

    val systems = Bag<SystemHarness>()

    /** Services */

    val updateService = StateUpdateService()
    val componentService = ComponentService(componentMutationListener = updateService)
    val compositionStore = CompositionStore()
    val subscriptionStore = SubscriptionStore()

    private val entityProducer = config.entityProducer
    val entityStore = EntityStore({ id -> entityProducer(this, id) })

    private val subscriptionProducer = { p: BitPredicate -> subscriptionStore.add(Subscription(p), compositionStore) }
    private val componentBitResolver = { p: KClass<out Component> -> componentService.bitIndexOf(p) }

    val patternStore = PatternStore(componentBitResolver, subscriptionProducer)

    /** Strategies */

    private val processingStrategyWorldFacade = WorldFacade(this)
    private val processingStrategy: ProcessingStrategy = config.processingStrategy

    init {
        componentService.register(config.componentTypes)
        config.systems.forEach { systems.add(SystemHarness(this, it)) }
        initialize()
    }

    fun initialize() {
        systems.forEach {
            it.initialize()
        }
    }

    fun process() {
        processingStrategy.process(processingStrategyWorldFacade)
    }

    /** Flush pending changes. */
    fun flush() {
    }

}