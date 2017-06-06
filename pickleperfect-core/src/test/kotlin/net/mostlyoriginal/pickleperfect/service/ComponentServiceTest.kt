package net.mostlyoriginal.pickleperfect.service

import net.mostlyoriginal.pickleperfect.IgnoreComponentMutationListener
import net.mostlyoriginal.pickleperfect.TestComponent1
import net.mostlyoriginal.pickleperfect.TestComponent2
import net.mostlyoriginal.pickleperfect.common.InvalidConfigurationException
import org.junit.Test
import kotlin.test.*

/**
 * @author Daan van Yperen
 * @see ComponentService
 */
class ComponentServiceTest {

    @Test
    fun When_registered_component_Then_can_resolve_component_mapper() {
        val service = ComponentService(IgnoreComponentMutationListener())
        service.register(TestComponent1::class, { TestComponent1() })
        assertNotNull(service.getStore(TestComponent1::class))
    }

    @Test
    fun When_registered_component_twice_Then_exception() {
        assertFailsWith<InvalidConfigurationException> {
            val service = ComponentService(IgnoreComponentMutationListener())
            service.register(TestComponent1::class, { TestComponent1() })
            service.register(TestComponent1::class, { TestComponent1() })
        }
    }

    @Test
    fun When_fetching_invalid_mapper_Then_exception() {
        assertFailsWith<InvalidConfigurationException> {
            ComponentService(IgnoreComponentMutationListener()).getStore(TestComponent1::class)
        }
    }

    @Test
    fun When_multiple_mappers_Then_resolve_correct_one() {
        val service = ComponentService(IgnoreComponentMutationListener())
        service.register(TestComponent1::class, { TestComponent1() })
        service.register(TestComponent2::class, { TestComponent2() })
        assertNotEquals(
                service.getStore(TestComponent1::class) as Any,
                service.getStore(TestComponent2::class) as Any)
    }
}