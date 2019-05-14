package com.tsarev.liquikotlin.util

import org.junit.Rule
import org.junit.rules.ExternalResource
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Test that hides rule chain and can apply rules in order in which they are defined.
 */
open class RuleChainAwareTest {

    private val definedRules = ArrayList<TestRule>()

    @get:Rule
    val chain: RuleChain
        get() {
            var resultChain = RuleChain.emptyRuleChain()
            definedRules.forEach { resultChain = resultChain.around(it) }
            return resultChain
        }

    /**
     * Delegate that adds itself to rule chain if defined.
     */
    abstract class ChainAwareRuleDelegate<R : RuleChainAwareTest, T : Any>(
        val constructor: () -> T,
        private val outerTest: R
    ) : ReadOnlyProperty<R, T>, ExternalResource() {
        lateinit var value: T
        override fun getValue(thisRef: R, property: KProperty<*>): T = value
        open operator fun provideDelegate(thisRef: R, prop: KProperty<*>) = also { outerTest.definedRules.add(this) }
        override fun before() = run { if (!this@ChainAwareRuleDelegate::value.isInitialized) value = constructor() }
        override fun after() {}
    }

    /**
     * Initializes resource every time the test is started.
     */
    fun <T : Any> init(constructor: () -> T) = object : RuleChainAwareTest.ChainAwareRuleDelegate<RuleChainAwareTest, T>(constructor, this) {
        override fun before() = run { value = constructor() }
    }

    /**
     * Initializes resource every time the test is started and closes it afterwards.
     */
    fun <T : AutoCloseable> resource(constructor: () -> T) = object : RuleChainAwareTest.ChainAwareRuleDelegate<RuleChainAwareTest, T>(constructor, this) {
        override fun before() = run { value = constructor() }
        override fun after() = value.close()
    }

    /**
     * Explicit use of some rule instead of delegating rule logic.
     */
    fun <T : TestRule> rule(constructor: () -> T) =
        object : ChainAwareRuleDelegate<RuleChainAwareTest, T>(constructor, this) {
            override fun apply(base: Statement?, description: Description?) = value.apply(base, description)
            override fun provideDelegate(thisRef: RuleChainAwareTest, prop: KProperty<*>):
                    ChainAwareRuleDelegate<RuleChainAwareTest, T> =
                super.provideDelegate(thisRef, prop).also { value = constructor() }
        }
}