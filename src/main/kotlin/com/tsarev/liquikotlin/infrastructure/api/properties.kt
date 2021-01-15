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
interface PropBase<FieldT : Any, SelfT : Self<SelfT>, NodeT : PropertyAble<NodeT>> {
    val pName: String
    val pClass: KClass<FieldT>
    val glue: Glue<NodeT>
}

/**
 * Nullable chainable property interface.
 */
interface NlbChPr<SelfT : Self<SelfT>, FieldT : Any, NodeT : PropertyAble<NodeT>>
    : Valuable<FieldT?>, PropBase<FieldT, SelfT, NodeT> {
    operator fun invoke(value: FieldT?): SelfT
}

/**
 * Non nullable chainable property interface.
 */
interface ChPr<SelfT : Self<SelfT>, FieldT : Any, NodeT : PropertyAble<NodeT>>
    : Valuable<FieldT>, PropBase<FieldT, SelfT, NodeT> {
    operator fun invoke(value: FieldT): SelfT
}

/**
 * Property delegate acting as delegate and delegate factory for any properties.
 */
open class ChPrDlg<SelfT : Self<SelfT>, FieldT : Any, NodeT : PropertyAble<NodeT>, out PropT : PropBase<FieldT, SelfT, NodeT>>(
    private val constructor: (KProperty<*>) -> PropT
) : ReadOnlyProperty<Any?, PropT> {
    private lateinit var pDef: KProperty<*>
    val prop by lazy { constructor(pDef) }
    open operator fun provideDelegate(thisRef: Any?, prop: KProperty<*>) = this.also { pDef = prop }
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prop
}

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