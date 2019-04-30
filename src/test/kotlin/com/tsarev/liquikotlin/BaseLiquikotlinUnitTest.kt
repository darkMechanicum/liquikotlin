package com.tsarev.liquikotlin

import com.tsarev.liquikotlin.infrastructure.LbDslNode
import com.tsarev.liquikotlin.util.*
import org.junit.Before
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
     * Node that is set in current test.
     */
    private var currentNode: LbDslNode<*>? = null

    /**
     * Evaluation result of the current test.
     */
    private var currentEvaluatedNode: Any? = null

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

    /**
     * Clear current node fields.
     */
    @Before
    open fun clearFields() {
        currentNode = null
        currentEvaluatedNode = null
    }
}