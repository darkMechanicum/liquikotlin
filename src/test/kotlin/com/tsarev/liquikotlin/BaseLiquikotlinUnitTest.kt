package com.tsarev.liquikotlin

import com.tsarev.liquikotlin.infrastructure.api.Self
import com.tsarev.liquikotlin.infrastructure.default.DefaultNode
import com.tsarev.liquikotlin.util.*
import kotlin.reflect.KClass

/**
 * Base for unit tests.
 */
open class BaseLiquikotlinUnitTest {

    /**
     * Extended test integration factory.
     */
    private val liquibaseIntegration = TestLiquibaseIntegrationFactory()

    /**
     * Simple test wrapped to reduce boilerplate.
     */
    protected fun testEvaluation(
        node: Self<*, DefaultNode>,
        expectedClass: KClass<*>,
        vararg expectedFields: Any
    ) {
        val rootNode = node.node.letWhile { it.parent?.value }
        val evalResult = rootNode.generalEval(liquibaseIntegration, testPath to DummyAccessor.instance)
        assertType(expectedClass, evalResult)
        assertFields(evalResult, *expectedFields)
    }

}