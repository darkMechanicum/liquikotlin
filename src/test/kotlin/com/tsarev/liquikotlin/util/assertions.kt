package com.tsarev.liquikotlin.util

import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.TypeSafeDiagnosingMatcher
import org.junit.Assert
import kotlin.reflect.KClass

typealias Getter<T> = (T) -> Any?

open class GetterMatcher<T>(private val getter: (T) -> Any?, private val expected: Any? = null) :
    TypeSafeDiagnosingMatcher<T>() {
    override fun describeTo(description: Description?) = description?.appendText("$expected").let { }
    override fun matchesSafely(item: T, mismatchDescription: Description) =
        (getter.invoke(item)?.equals(expected) ?: (expected == null))
            .also { if (!it) mismatchDescription.appendText("not equal to ${getter.invoke(item)}") }
}

fun assertType(expectedType: KClass<*>, value: Any) =
    Assert.assertTrue("Expected type ${expectedType.qualifiedName} for $value.", expectedType.isInstance(value))

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