package com.tsarev.liquikotlin.printing

import com.tsarev.liquikotlin.infrastructure.DslNode
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

inline operator fun <reified NodeT : Any, FieldT> KProperty1<NodeT, DslNode.Valuable<FieldT>>.unaryMinus()
        : Pair<KClass<NodeT>, (Any) -> Any?> = NodeT::class to { node -> this.get(node as NodeT).current }

/**
 * Simple typealias for string mutations.
 */
typealias Pattern = (Any?) -> String

// Useful extension functions.
infix fun MutableList<String>.addStart(line: String?) = if (line != null) this.add(line) else null
infix fun MutableList<String>.addLine(line: String?) = if (line != null) this.add(line) else null
inline fun <reified T: Any> Collection<Any?>.safeCastMap(): List<T> = this.mapNotNull { if (it is T) it else null }
fun <T: Any> Iterator<T>.forFirst(action: (T) -> Unit): Iterator<T> = this
    .takeIf { this.hasNext() }
    ?.also { action(it.next()) } ?: this
fun <T: Any> Iterable<T>.forFirst(action: (T) -> Unit): List<T> = this
    .iterator()
    .forFirst(action)
    .asSequence()
    .toList()
fun <T: Any> List<T>.forEachExceptLast(action: (T) -> Unit): T? = when (val lastIndex = this.size - 1) {
    -1 -> null
    0 -> this.last()
    else -> { this.forEachIndexed { index, t -> if (index != lastIndex) action.invoke(t) } ; this.last() }
}