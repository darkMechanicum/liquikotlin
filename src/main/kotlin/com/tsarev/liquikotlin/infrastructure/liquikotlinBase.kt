package com.tsarev.liquikotlin.infrastructure

import liquibase.resource.ResourceAccessor
import java.lang.RuntimeException
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

typealias LbArg = Pair<String, ResourceAccessor>

open class LbDslNode<SelfT : LbDslNode<SelfT, LinkedT, ParentLinkedT>, LinkedT : Any, ParentLinkedT>(
    override val thisClass: KClass<SelfT>,
    protected val linkedConstructor: () -> LinkedT,
    private val linkedSetter: ((ParentLinkedT, LinkedT, SelfT, LbArg?) -> Unit)? = null
) : DefaultNode<SelfT, LinkedT, LbArg>() {

    protected companion object {
        /**
         * Add child builder.
         */
        inline fun <ArgT, reified ChildT : DefaultNode<ChildT, *, ArgT>, SelfT : DefaultNode<SelfT, *, ArgT>>
                DefaultNode<SelfT, *, ArgT>.child(noinline constructor: () -> ChildT): Lazy<ChildT> {
            val result = lazy {
                constructor().also {
                    addChild(
                        this.self,
                        it
                    )
                }
            }
            addChildBuilder(
                this.self,
                ChildT::class.simpleName!!,
                result
            )
            return result
        }
    }

    data class RequiredFlag(var hasNone: Boolean = true)

    private val requiredFlags = ArrayList<RequiredFlag>()

    private fun getNewFlag() = requiredFlags.apply { add(RequiredFlag()) }.size - 1

    override fun copySelfParameters(other: DefaultNode<SelfT, LinkedT, LbArg>) {
        val otherFlags = other.self.requiredFlags
        if (requiredFlags.size == otherFlags.size) {
            otherFlags.forEachIndexed { index, _ -> requiredFlags[index].hasNone = otherFlags[index].hasNone }
        }
    }

    override fun createEvalResult(argument: LbArg?): LinkedT = linkedConstructor()

    override fun evalChildren(evalResult: LinkedT, argument: LbArg?): LinkedT {
        if (requiredFlags.any { it.hasNone }) {
            throw IllegalArgumentException("Some of the properties of node <${self::class}> is not present.")
        }
        return super.evalChildren(evalResult, argument)
    }

    override fun eval(argument: LbArg?, parent: EvaluatableDslNode<*, *, LbArg>?, parentEval: Any?) =
        super.eval(argument, parent, parentEval).also {
            if (parentEval != null) {
                linkedSetter?.invoke(parentEval as ParentLinkedT, it, self, argument)
            }
        }

    /**
     * Add default property.
     */
    protected fun <FieldT : Any> nonNullableWS(
        fieldClass: KClass<FieldT>,
        setter: (LinkedT, FieldT) -> Any,
        default: FieldT? = null
    ): DefaultableDelegate<FieldT> {
        val newFlagIndex = getNewFlag()
        return nonNullableWithCallback(
            fieldClass,
            default
        ) { property, propertyName ->
            self.requiredFlags[newFlagIndex].hasNone = false
            setHasDefault()
            propertyEvaluationChain[propertyName] = { evalRes, self, _ ->
                val nonNullableValue = self.parameters[propertyName]
                    ?: throw RuntimeException("No value found for required property $propertyName")
                if (property.propertyType.isInstance(nonNullableValue)) {
                    setter(evalRes, property.propertyType.cast(nonNullableValue))
                } else {
                    throw RuntimeException("Property <$propertyName> is supplied with wrong type <${nonNullableValue::class}>.")
                }

            }
        }
    }

    /**
     * Add default property.
     */
    protected fun <FieldT : Any> nullableWS(
        fieldClass: KClass<FieldT>,
        setter: (LinkedT, FieldT) -> Any,
        default: FieldT? = null
    ): NullableDefaultableDelegate<FieldT> = nullableWithCallback(
        fieldClass,
        default
    ) { property, propertyName ->
        setHasDefault()
        propertyEvaluationChain[propertyName] = { evalRes, self, _ ->
            self.parameters[propertyName]?.let {
                if (property.propertyType.isInstance(it)) {
                    setter(evalRes, property.propertyType.cast(it))
                } else {
                    throw RuntimeException("Property <$propertyName> is supplied with wrong type <${it::class}>.")
                }

            }
        }
    }

}