package com.tsarev.liquikotlin.util

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Generate two way map from passed pairs.
 */
fun <KeyT> twoWayMap(vararg pairs: Pair<KeyT, KeyT>): Map<KeyT, KeyT> =
    HashMap<KeyT, KeyT>().apply {
        pairs.forEach { this[it.first] = it.second }
        pairs.forEach { this[it.second] = it.first }
    }

/**
 * Transform this value until transformer returns new not null values.
 */
fun <T> T.letWhile(transform: (T) -> T?): T {
    var current = this
    var next = transform(current)
    while (next != null) {
        current = next
        next = transform(current)
    }
    return current
}

/**
 * Helper class to write compact let try constructions.
 */
data class ClosureLetTry<T, out R>(val closure: (T) -> R, val value: T?) {
    operator fun invoke(exceptionSupplier: (Throwable) -> Throwable?) = try {
        value?.let { closure.invoke(it) }
    } catch (cause: Throwable) {
        exceptionSupplier(cause)?.run { throw this }
    }
}
/**
 * Nullable form of compact let try expression.
 */
fun <T, R> T?.letTry(closure: (T) -> R) = ClosureLetTry(closure, this)

/**
 * Simple function to escape `fun method() = smth.doSmth.let { }` in Unit methods.
 */
val Any?.ignore get() = run { }

/**
 * Observing delegate with lazy value initialization.
 */
class LazyObservingDelegate<T : Any>(
    private val ctor: () -> T,
    private val callback: (T) -> Unit
) : ReadOnlyProperty<Any, T> {
    val value by lazy(ctor)
    override fun getValue(thisRef: Any, property: KProperty<*>) = value.also { callback(it) }
}