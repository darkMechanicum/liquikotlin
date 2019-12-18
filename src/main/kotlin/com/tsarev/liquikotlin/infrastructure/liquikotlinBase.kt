package com.tsarev.liquikotlin.infrastructure

import com.tsarev.liquikotlin.infrastructure.api.*
import com.tsarev.liquikotlin.infrastructure.default.DefaultGlueProvider
import com.tsarev.liquikotlin.infrastructure.default.DefaultNode
import com.tsarev.liquikotlin.infrastructure.default.DefaultSelf
import liquibase.resource.ResourceAccessor
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

typealias LbArg = Pair<String, ResourceAccessor>

typealias LkGlueProvider = DefaultGlueProvider

open class LbDslNode<SelfT : LbDslNode<SelfT>>(
    selfClass: KClass<SelfT>
) : DefaultSelf<SelfT>(selfClass), GlueProvider<DefaultNode> by LkGlueProvider {
    internal val node get() = glue(this).node
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
open class LiquibaseIntegrator<LinkedT : Any, ParentT : Any>(
    val linkedConstructor: () -> LinkedT,
    private val parentSetter: ((ParentT, LinkedT?, DefaultNode, LbArg?) -> Unit)? = null,
    vararg mappings: PropertyMapping<DefaultNode, LinkedT, *>
) : EvalAction<DefaultNode, LinkedT, LbArg> {

    protected val propertyMappings: MutableCollection<PropertyMapping<DefaultNode, LinkedT, *>> =
        ArrayList<PropertyMapping<DefaultNode, LinkedT, *>>().also {
            it.addAll(mappings)
        }

    override fun doBefore(thisNode: DefaultNode, argument: LbArg?): LinkedT? = linkedConstructor()

    override fun doAfter(
        argument: LbArg?,
        thisNode: DefaultNode,
        childEvaluations: Collection<Any?>,
        childNodes: Collection<DefaultNode>,
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
internal inline operator fun <LinkedT : Any, SelfT : Self<SelfT>, reified PropertyT : Any>
        KProperty1<*, NlbChPr<SelfT, PropertyT, DefaultNode>>.minus(crossinline setter: (LinkedT, PropertyT?) -> Any) =
    PropertyMapping<DefaultNode, LinkedT, PropertyT>(
        { node -> node.node.getNullableProperty(PropertyT::class, this.name) },
        { linked, prop -> linked?.run { setter.invoke(this, prop) } })

internal inline infix fun <LinkedT : Any, SelfT : Self<SelfT>, reified PropertyT : Any>
        KProperty1<*, ChPr<SelfT, PropertyT, DefaultNode>>.notNull(crossinline setter: (LinkedT, PropertyT?) -> Any) =
    PropertyMapping<DefaultNode, LinkedT, PropertyT>(
        { node -> node.node.getProperty(PropertyT::class, this.name) },
        { linked, prop -> linked?.run { setter.invoke(this, prop) } })

inline fun <reified SelfT : DefaultSelf<SelfT>, reified PropertyT : Any> DefaultNode.getNullable(prop: KProperty1<SelfT, NlbChPr<SelfT, PropertyT, DefaultNode>>) =
    this.getNullableProperty(PropertyT::class, prop.name)

inline fun <reified SelfT : DefaultSelf<SelfT>, reified PropertyT : Any> DefaultNode.get(prop: KProperty1<SelfT, ChPr<SelfT, PropertyT, DefaultNode>>) =
    this.getProperty(PropertyT::class, prop.name)