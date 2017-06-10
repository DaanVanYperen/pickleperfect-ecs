package net.mostlyoriginal.pickleperfect.common


class InvalidConfigurationException(message: String) : RuntimeException(message)
class AccessingRecycledEntityException(message: String) : RuntimeException(message)

/**
 * @author Daan van Yperen
 */
fun <T : Any> T?.orConfigurationError(message: String): T {
    return if (this == null) throw InvalidConfigurationException(message) else this
}

/**
 * @author Daan van Yperen
 */
fun <T : Any> T?.orRuntimeError(message: String): T {
    return if (this == null) throw RuntimeException(message) else this
}