package net.mostlyoriginal.pickleperfect

import net.mostlyoriginal.pickleperfect.common.Bag
import net.mostlyoriginal.pickleperfect.internal.Subscription
import net.mostlyoriginal.pickleperfect.internal.SystemHarness
import net.mostlyoriginal.pickleperfect.predicate.BitPredicate
import net.mostlyoriginal.pickleperfect.service.*
import kotlin.reflect.KClass

/**
 * @author Daan van Yperen
 */
class World(config: WorldConfiguration) {

    private val entityProducer = config.entityProducer
    val systems = Bag<SystemHarness>()

    val updateService = StateUpdateService()

    val componentService = ComponentService(componentMutationListener = updateService)
    val compositionStore = CompositionStore()
    val subscriptionStore = SubscriptionStore()
    val entityStore = EntityStore({ id -> entityProducer(this, id) })

    val subscriptionProducer = { p: BitPredicate -> subscriptionStore.add(Subscription(p), compositionStore) }
    val componentBitResolver = { p: KClass<out Component> -> componentService.bitIndexOf(p) }

    val patternStore = PatternStore(componentBitResolver, subscriptionProducer)


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
        flush()
        systems.forEach {
            it.process()
            flush()
        }
    }

    /** Flush pending changes. */
    fun flush() {
        updateService.update(compositionStore, subscriptionStore)
    }

}