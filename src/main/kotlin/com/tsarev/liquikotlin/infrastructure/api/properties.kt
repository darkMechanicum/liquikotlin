package com.tsarev.liquikotlin.infrastructure.api

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Base interface for property that can return some value.
 */
interface Valuable<FieldT> {
    val current: FieldT
}

/**
 * Base interface for property.
 */
interface PropBase<FieldT : Any, SelfT : Self<SelfT, NodeT>, NodeT : PropertyAble<NodeT>> {
    val pName: String
    val pClass: KClass<FieldT>
    val self: SelfT
}

/**
 * Nullable chainable property interface.
 */
interface NlbChPr<SelfT : Self<SelfT, NodeT>, FieldT : Any, NodeT : PropertyAble<NodeT>>
    : Valuable<FieldT?>, PropBase<FieldT, SelfT, NodeT> {
    operator fun invoke(value: FieldT?): SelfT
}

/**
 * Non nullable chainable property interface.
 */
interface ChPr<SelfT : Self<SelfT, NodeT>, FieldT : Any, NodeT : PropertyAble<NodeT>>
    : Valuable<FieldT>, PropBase<FieldT, SelfT, NodeT> {
    operator fun invoke(value: FieldT): SelfT
}

/**
 * Property delegate acting as delegate and delegate factory for any properies.
 */
open class ChPrDlg<SelfT : Self<SelfT, NodeT>, FieldT : Any, NodeT : PropertyAble<NodeT>, out PropT : PropBase<FieldT, SelfT, NodeT>>(
    private val constructor: (KProperty<*>) -> PropT
) : ReadOnlyProperty<Any?, PropT> {
    private lateinit var pDef: KProperty<*>
    val prop by lazy { constructor(pDef) }
    open operator fun provideDelegate(thisRef: Any?, prop: KProperty<*>) = this.also { pDef = prop }
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prop
}

/**
 * Utility method to define non nullable properties easier.
 */
fun <NodeT : PropertyAble<NodeT>, SelfT : Self<SelfT, NodeT>, FieldT : Any> Self<SelfT, NodeT>.prop(
    pClass: KClass<FieldT>
) = this.node.createDelegate(pClass, this.self)

/**
 * Utility method to define nullable properties easier.
 */
fun <NodeT : PropertyAble<NodeT>, SelfT : Self<SelfT, NodeT>, FieldT : Any> Self<SelfT, NodeT>.nullable(
    pClass: KClass<FieldT>
) = this.node.createNlbDelegate(pClass, this.self)

/**
 * Basic property meta info.
 */
open class PropMeta(
    val name: String,
    val type: KClass<*>,
    val annotations: List<Annotation>,
    val definingProp: KProperty<*>,
    val getter: (PropertyAble<*>) -> Any?,
    val isNullable: Boolean
)