package com.tsarev.liquikotlin.infrastructure

import liquibase.resource.ResourceAccessor
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

typealias LbArg = Pair<String, ResourceAccessor>

open class LbDslNode<SelfT : LbDslNode<SelfT>>(
    override val thisClass: KClass<SelfT>
) : DefaultNode<SelfT>() {

    override val parent: LbDslNode<*>?
        get() = super.parent as LbDslNode<*>?

    protected companion object {
        /**
         * Add child builder.
         */
        inline fun <reified ChildT : DefaultNode<ChildT>, SelfT : DefaultNode<SelfT>>
                DefaultNode<SelfT>.child(noinline constructor: () -> ChildT): Lazy<ChildT> {
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

        /**
         * Add lazy child node.
         */
        inline fun <reified ChildT : DefaultNode<ChildT>, SelfT : DefaultNode<SelfT>>
                DefaultNode<SelfT>.builtChild(noinline constructor: () -> ChildT): Lazy<ChildT> {
            return lazy {
                constructor().also {
                    addChild(
                        self,
                        it,
                        true
                    )
                }
            }
        }
    }

    data class RequiredFlag(var hasNone: Boolean = true)

    private val requiredFlags = ArrayList<RequiredFlag>()

    private fun getNewFlag() = requiredFlags.apply { add(RequiredFlag()) }.size - 1

}

/**
 * Single property mapping to original domain model.
 */
data class PropertyMapping<FromT, ToT, PropertyT>(
    val getter: (FromT) -> PropertyT?,
    val setter: (ToT?, PropertyT) -> Any?
) {
    fun map(from: FromT, to: ToT?) {
        val value = getter(from)
        if (value != null) {
            setter(to, value)
        }
    }
}

/**
 * Base integrator class for liquibase domain model.
 */
open class LiquibaseIntegrator<NodeT : DefaultNode<NodeT>, LinkedT : Any, ParentT : Any>(
    val linkedConstructor: () -> LinkedT,
    private val parentSetter: ((ParentT, LinkedT?, NodeT, LbArg?) -> Unit)? = null,
    vararg mappings: PropertyMapping<NodeT, LinkedT, *>
) : EvaluatableDslNode.Evaluator<NodeT, LinkedT, LbArg>() {

    protected val propertyMappings: MutableCollection<PropertyMapping<NodeT, LinkedT, *>> =
        ArrayList<PropertyMapping<NodeT, LinkedT, *>>().also {
            it.addAll(mappings)
        }

    override fun initResult(thisNode: NodeT, argument: LbArg?): LinkedT? = linkedConstructor()

    override fun eval(
        childEvaluations: Collection<Any?>,
        argument: LbArg?,
        thisNode: NodeT,
        parentEval: Any?,
        resultEval: LinkedT?
    ): LinkedT? {
        propertyMappings.forEach { it.map(thisNode, resultEval) }
        if (parentEval != null) {
            parentSetter?.let { it(parentEval as ParentT, resultEval, thisNode, argument) }
        }
        return resultEval
    }

}

/**
 * Util function for fast [PropertyMapping] creation.
 */
operator fun <LinkedT : SelfLinkedT, SelfLinkedT : Any, NodeT : DefaultNode<NodeT>, SelfT : DefaultNode<NodeT>, PropertyT>
        KProperty1<SelfT, DslNode.Valuable<PropertyT>>.minus(setter: (SelfLinkedT, PropertyT?) -> Any) =
    PropertyMapping<NodeT, LinkedT, PropertyT>({ node -> this.get(node as SelfT).current }, { linked, prop -> linked?.run { setter.invoke(this, prop) } })