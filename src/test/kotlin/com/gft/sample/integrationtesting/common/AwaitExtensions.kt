package com.gft.sample.integrationtesting.common

import org.awaitility.core.ConditionFactory
import kotlin.reflect.KClass

val DEFAULT_RETRIED_EXCEPTIONS: Set<KClass<out Throwable>> =
    setOf(NoSuchElementException::class, NullPointerException::class)

inline fun ConditionFactory.untilCompleted(
    retriedExceptions: Set<KClass<out Throwable>> = DEFAULT_RETRIED_EXCEPTIONS,
    crossinline block: () -> Unit
): Unit = untilAsserted {
    runCatching(block)
        .getOrElse { ex ->
            throw if (retriedExceptions.any { it.isInstance(ex) }) AssertionError(ex.message, ex) else ex
        }
}
