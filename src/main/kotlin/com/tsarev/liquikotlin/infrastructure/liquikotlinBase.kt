package com.tsarev.liquikotlin.infrastructure

import liquibase.resource.ResourceAccessor
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

typealias LbArg = Pair<String, ResourceAccessor>

open class LbDslNode<SelfT : LbDslNode<SelfT>>(
    override val thisClass: KClass<SelfT>
) : DefaultNode<SelfT>() {

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
    }

    data class RequiredFlag(var hasNone: Boolean = true)

    private val requiredFlags = ArrayList<RequiredFlag>()

    private fun getNewFlag() = requiredFlags.apply { add(RequiredFlag()) }.size - 1

}

data class PropertyMapping<FromT, ToT, PropertyT>(
    val getter: (FromT) -> PropertyT?,
    val setter: (ToT, PropertyT) -> Any?
) {
    fun map(from: FromT, to: ToT) {
        val value = getter(from)
        if (value != null) {
            setter(to, value)
        }
    }
}

open class LiquibaseIntegrator<NodeT : DefaultNode<NodeT>, LinkedT : Any, ParentT : Any>(
    val linkedConstructor: () -> LinkedT,
    val parentSetter: ((ParentT, LinkedT, NodeT, LbArg?) -> Unit)? = null
) : EvaluatableDslNode.Evaluator<NodeT, LinkedT, LbArg>() {

    val propertyMappings: MutableCollection<PropertyMapping<NodeT, LinkedT, *>> = ArrayList()

    override fun initResult(thisNode: NodeT, argument: LbArg?): LinkedT? = linkedConstructor()

    override fun eval(
        childEvaluations: Collection<Any>,
        argument: LbArg?,
        thisNode: NodeT,
        parentEval: Any?,
        resultEval: LinkedT?
    ): LinkedT {
        resultEval!!
        propertyMappings.forEach { it.map(thisNode, resultEval) }
        if (parentEval != null) {
            parentSetter?.let { it(parentEval as ParentT, resultEval, thisNode, argument) }
        }
        return resultEval
    }

    operator fun <PropertyT, BaseT : DslNode<NodeT>> KProperty1<BaseT, DslNode.Valuable<PropertyT>>.minus(
        setter: (LinkedT, PropertyT?) -> Any
    ) {
        // TODO Rework this
        propertyMappings.add(
            PropertyMapping({ node -> this.get(node as BaseT).current }, setter)
        )
    }

}