package com.tsarev.liquikotlin

import com.tsarev.liquikotlin.infrastructure.LbDslNode
import com.tsarev.liquikotlin.util.*
import kotlin.reflect.KClass

/**
 * Base for unit tests.
 */
open class BaseLiquikotlinUnitTest {

    companion object {
        val dummyPath = "some"
    }

    /**
     * Extended test integration factory.
     */
    private val liquibaseIntegration = TestLiquibaseIntegrationFactory()

    /**
     * Simple test wrapped to reduce boilerplate.
     */
    protected fun testEvaluation(
        node: LbDslNode<*>,
        expectedClass: KClass<*>,
        vararg expectedFields: Any
    ) {
        val evalResult = node.generalEval(liquibaseIntegration, dummyPath to DummyAccessor.instance)
        assertType(expectedClass, evalResult)
        assertFields(evalResult, *expectedFields)
    }

}