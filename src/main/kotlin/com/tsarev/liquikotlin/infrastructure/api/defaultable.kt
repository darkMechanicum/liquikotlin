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
    override fun <FieldT : Any, SelfT : Self<SelfT>> createNlbDelegate(
        pClass: KClass<FieldT>,
        glue: Glue<NodeT>,
        self: SelfT
    ) = createNlbDelegate(pClass, glue, self, null)

    // Non nullable properties.
    override fun <FieldT : Any, SelfT : Self<SelfT>> createDelegate(
        pClass: KClass<FieldT>,
        glue: Glue<NodeT>,
        self: SelfT
    ) = createDelegate(pClass, glue, self, null)

    // Nullable properties with default value.
    fun <FieldT : Any, SelfT : Self<SelfT>> createNlbDelegate(
        pClass: KClass<FieldT>,
        glue: Glue<NodeT>,
        self: SelfT,
        default: FieldT? = null
    ) = createDelegate { pDef -> createNlbProp(pDef.name, pClass, glue, self).apply { this.default = default } }

    override fun <FieldT : Any, SelfT : Self<SelfT>> createNlbProp(
        pName: String,
        pClass: KClass<FieldT>,
        glue: Glue<NodeT>,
        self: SelfT
    ): DfltNlbChPr<SelfT, FieldT, NodeT>

    // Non nullable properties with default value.
    fun <FieldT : Any, SelfT : Self<SelfT>> createDelegate(
        pClass: KClass<FieldT>,
        glue: Glue<NodeT>,
        self: SelfT,
        default: FieldT? = null
    ) = createDelegate { pDef -> createProp(pDef.name, pClass, glue, self).apply { this.default = default } }

    override fun <FieldT : Any, SelfT : Self<SelfT>> createProp(
        pName: String,
        pClass: KClass<FieldT>,
        glue: Glue<NodeT>,
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

interface DfltNlbChPr<SelfT : Self<SelfT>, FieldT : Any, NodeT : DefaultPropertyAble<NodeT>>
    : NlbChPr<SelfT, FieldT, NodeT>, PropBase<FieldT, SelfT, NodeT>, DefaultAblePr<FieldT>

interface DfltChPr<SelfT : Self<SelfT>, FieldT : Any, NodeT : DefaultPropertyAble<NodeT>>
    : ChPr<SelfT, FieldT, NodeT>, PropBase<FieldT, SelfT, NodeT>, DefaultAblePr<FieldT>
