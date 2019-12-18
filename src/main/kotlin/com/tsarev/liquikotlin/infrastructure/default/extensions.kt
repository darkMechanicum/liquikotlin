package com.tsarev.liquikotlin.infrastructure.default

import com.tsarev.liquikotlin.infrastructure.api.*
import kotlin.reflect.KClass

/**
 * Create nullable property with default, within glue context.
 */
fun <NodeT : DefaultPropertyAble<NodeT>, SelfT : Self<SelfT>, FieldT : Any> prop(
    pClass: KClass<FieldT>,
    self: SelfT,
    glueProvider: GlueProvider<NodeT>, // shuffle parameters, as here can be declaration clash
    default: FieldT? = null
) = glueProvider.glue(self).run { node.createNlbDelegate(pClass, this, self, default) }

/**
 * Create non null property with default, within glue context.
 */
fun <NodeT : DefaultPropertyAble<NodeT>, SelfT, FieldT : Any> Self<SelfT>.prop(
    pClass: KClass<FieldT>,
    default: FieldT? = null
) where SelfT : Self<SelfT>,
        SelfT : GlueProvider<NodeT> =
    self.glue(self).run { node.createDelegate(pClass, this, self, default) }

/**
 * Create nullable property with default, within glue context.
 */
fun <NodeT : DefaultPropertyAble<NodeT>, SelfT : Self<SelfT>, FieldT : Any> nullable(
    pClass: KClass<FieldT>,
    self: SelfT,
    glueProvider: GlueProvider<NodeT>, // shuffle parameters, as here can be declaration clash
    default: FieldT? = null
) = glueProvider.glue(self).run { node.createNlbDelegate(pClass, this, self, default) }

/**
 * Create nullable property with default, within glue context.
 */
fun <NodeT : DefaultPropertyAble<NodeT>, SelfT, FieldT : Any> Self<SelfT>.nullable(
    pClass: KClass<FieldT>,
    default: FieldT? = null
) where SelfT : Self<SelfT>,
        SelfT : GlueProvider<NodeT> =
    self.glue(self).run { node.createNlbDelegate(pClass, this, self, default) }

/**
 * Create child with default glue.
 */
fun <ChildT, SelfT> SelfT.child(childCtor: () -> ChildT)
        where SelfT : ChildAbleSelf<SelfT>,
              ChildT : ChildAbleSelf<ChildT> = DefaultGlueProvider.child(childCtor, this)

/**
 * Create built child with default glue.
 */
fun <ChildT, SelfT> SelfT.builtChild(childCtor: () -> ChildT)
        where SelfT : ChildAbleSelf<SelfT>,
              ChildT : ChildAbleSelf<ChildT> = DefaultGlueProvider.builtChild(childCtor, this)