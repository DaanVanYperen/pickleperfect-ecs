package net.mostlyoriginal.pickleperfect

import net.mostlyoriginal.pickleperfect.common.Bag
import net.mostlyoriginal.pickleperfect.common.ProcessingStrategy
import net.mostlyoriginal.pickleperfect.internal.Deleted
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
    var delta: Float = 0F

    /** Services */

    val updateService = StateUpdateService()
    val componentService = ComponentService(componentMutationListener = updateService)
    val compositionStore = CompositionStore()
    val subscriptionStore = SubscriptionStore()

    private val entityProducer = config.entityProducer
    val entityService: EntityService<Entity>

    private val subscriptionProducer = { p: BitPredicate -> subscriptionStore.add(Subscription(p), compositionStore) }
    private val componentBitResolver = { p: KClass<out Component> -> componentService.bitIndexOf(p) }

    val patternStore = PatternStore(componentBitResolver, subscriptionProducer)

    /** Strategies */

    private val processingStrategyWorldFacade: WorldFacade
    private val processingStrategy: ProcessingStrategy = config.processingStrategy

    init {
        // 1. stuff required for facade
        componentService.register(Deleted::class, ::Deleted) // system component for tracking alive state.
        componentService.register(config.componentTypes)
        // 2. stuff that requires component mappers initialized.
        entityService = EntityService(subscriptionStore, componentService, { id -> entityProducer(this, id) })
        processingStrategyWorldFacade = WorldFacade(this)
        // 3. initialize systems
        config.systems.forEach { systems.add(SystemHarness(this, it)) }
        initializeSystems()
    }

    private fun initializeSystems() {
        systems.forEach {
            it.initialize()
        }
    }

    fun process() {
        processingStrategy.process(processingStrategyWorldFacade)
    }

}