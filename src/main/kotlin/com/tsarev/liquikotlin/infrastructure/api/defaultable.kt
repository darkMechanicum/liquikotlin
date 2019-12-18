package com.tsarev.liquikotlin.infrastructure.api

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Defaultable property meta information.
 */
open class MetaWithDefault(
    name: String,
    type: KClass<*>,
    annotations: List<Annotation>,
    definingProp: KProperty<*>,
    getter: (PropertyAble<*>) -> Any?,
    isNullable: Boolean,
    val hasDefault: () -> Boolean
) : PropMeta(name, type, annotations, definingProp, getter, isNullable)

/**
 * Node that can have managed chainable properties, which can have default values.
 */
interface DefaultPropertyAble<NodeT> : PropertyAble<NodeT>, MetaAble<MetaWithDefault>
        where NodeT : DefaultPropertyAble<NodeT> {

    // Nullable properties.
    override fun <FieldT : Any, SelfT : Self<SelfT, NodeT>> createNlbDelegate(pClass: KClass<FieldT>, self: SelfT) =
        createNlbDelegate(pClass, self, null)

    // Non nullable properties.
    override fun <FieldT : Any, SelfT : Self<SelfT, NodeT>> createDelegate(pClass: KClass<FieldT>, self: SelfT) =
        createDelegate(pClass, self, null)

    // Nullable properties with default value.
    fun <FieldT : Any, SelfT : Self<SelfT, NodeT>> createNlbDelegate(
        pClass: KClass<FieldT>,
        self: SelfT,
        default: FieldT? = null
    ) = createDelegate { pDef -> createNlbProp(pDef.name, pClass, self).apply { this.default = default } }

    override fun <FieldT : Any, SelfT : Self<SelfT, NodeT>> createNlbProp(
        pName: String,
        pClass: KClass<FieldT>,
        self: SelfT
    ): DfltNlbChPr<SelfT, FieldT, NodeT>

    // Non nullable properties with default value.
    fun <FieldT : Any, SelfT : Self<SelfT, NodeT>> createDelegate(
        pClass: KClass<FieldT>,
        self: SelfT,
        default: FieldT? = null
    ) = createDelegate { pDef -> createProp(pDef.name, pClass, self).apply { this.default = default } }

    override fun <FieldT : Any, SelfT : Self<SelfT, NodeT>> createProp(
        pName: String,
        pClass: KClass<FieldT>,
        self: SelfT
    ): DfltChPr<SelfT, FieldT, NodeT>

    /** Metas with [MetaWithDefault.hasDefault] flag. */
    override val metas: MutableMap<String, MetaWithDefault>

    /** Meta with [MetaWithDefault.hasDefault] flag. */
    override fun getMeta(pName: String): MetaWithDefault?

    var hasDefault: Boolean
}

interface DefaultAblePr<FieldT : Any> {
    var default: FieldT?
}

interface DfltNlbChPr<SelfT : Self<SelfT, NodeT>, FieldT : Any, NodeT : DefaultPropertyAble<NodeT>>
    : NlbChPr<SelfT, FieldT, NodeT>, PropBase<FieldT, SelfT, NodeT>, DefaultAblePr<FieldT>

interface DfltChPr<SelfT : Self<SelfT, NodeT>, FieldT : Any, NodeT : DefaultPropertyAble<NodeT>>
    : ChPr<SelfT, FieldT, NodeT>, PropBase<FieldT, SelfT, NodeT>, DefaultAblePr<FieldT>

fun <NodeT : DefaultPropertyAble<NodeT>, SelfT : Self<SelfT, NodeT>, FieldT : Any> SelfT.prop(
    pClass: KClass<FieldT>, default: FieldT? = null
) = this.node.createDelegate(pClass, this, default)

fun <NodeT : DefaultPropertyAble<NodeT>, SelfT : Self<SelfT, NodeT>, FieldT : Any> SelfT.nullable(
    pClass: KClass<FieldT>, default: FieldT? = null
) = this.node.createNlbDelegate(pClass, this, default)
