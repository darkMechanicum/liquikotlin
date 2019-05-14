package com.tsarev.liquikotlin.util

import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.TypeSafeDiagnosingMatcher
import org.jetbrains.kotlin.utils.addToStdlib.assertedCast
import org.junit.Assert
import kotlin.reflect.KClass

typealias Getter<T> = (T) -> Any?

/**
 * Matcher that invokes getter and compares result with expected value.
 */
open class GetterMatcher<T>(private val getter: (T) -> Any?, private val expected: Any? = null) :
    TypeSafeDiagnosingMatcher<T>() {
    override fun describeTo(description: Description?) = description?.appendText("$expected").let { }
    override fun matchesSafely(item: T, mismatchDescription: Description) =
        (getter.invoke(item)?.equals(expected) ?: (expected == null))
            .also { if (!it) mismatchDescription.appendText("not equal to ${getter.invoke(item)}") }
}

/**
 * Assert passed value has specfied type.
 */
fun assertType(expectedType: KClass<*>, value: Any?) =
    Assert.assertTrue("Expected type ${expectedType.qualifiedName} for $value.", expectedType.isInstance(value))

/**
 * Asserted cast with default message.
 */
inline fun <reified T : Any> Any?.assertedCast(): T =
    this.assertedCast { "$this should be of type ${T::class.qualifiedName}" }

/**
 * Bulk equals getters assertions.
 */
fun <T> assertFields(value: T, vararg raw: Any) =
    Assert.assertThat("Actual $value fields are not equal to expected.", value, CoreMatchers.allOf(
        raw.map {
            when (it) {
                is Pair<*, *> ->
                    if (it.first is Getter<*>) GetterMatcher(it.first as (T) -> Any?, it.second)
                    else throw RuntimeException("Can't pass pair without getter.")
                is Getter<*> ->
                    GetterMatcher(it as (T) -> Any?)
                else ->
                    throw RuntimeException("Invalid argument type.")
            }
        }
    ))

/**
 * Assert that passed list elements are of specified classes in the same order.
 */
fun List<*>.assertClasses(vararg kClass: KClass<*>) = kClass
    .mapIndexed { index, kclass -> Assert.assertTrue(this.size > index).let { this[index] to kclass } }
    .forEach { assertType(it.second, it.first) }

/**
 * Ignore exceptions within this block.
 */
fun <T> T.ignore(block: T.() -> Unit) = try {
    this.block()
} catch (ignore: Throwable) {
}