package com.tsarev.liquikotlin.infrastructure.api

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Node that is aware of its own type.
 */
interface Node<NodeT : Node<NodeT>> {

    val node: NodeT

    /**
     * Node initialization.
     *
     * @param root Node's real implementation.
     */
    fun init(root: NodeT) {}

}

/**
 * Node that can build itself.
 */
interface BuilderAble<NodeT : BuilderAble<NodeT>> : Node<NodeT> {

    /**
     * If this node is builder.
     */
    var isBuilder: Boolean

    /**
     * Constructor for node building.
     */
    var ctor: NodeT.() -> NodeT

    /**
     * Build non builder instance if this is builder.
     */
    fun build(): NodeT

    /**
     * Add additional customization to the new node.
     */
    fun onBuilt(customizer: (NodeT, NodeT) -> Unit)

}

/**
 * Node that can build itself.
 */
interface CopyAble<NodeT : CopyAble<NodeT>> : Node<NodeT> {

    /**
     * Copy node.
     */
    fun copy(): NodeT

    /**
     * Add additional customization to the new node.
     */
    fun onCopy(customizer: (NodeT, NodeT) -> Unit)

}

/**
 * Node that can have managed chainable properties.
 */
interface PropertyAble<NodeT : PropertyAble<NodeT>> : Node<NodeT> {

    companion object {
        inline fun <reified FieldT : Any> PropertyAble<*>.getAnyProperty(pName: String) =
            this.getAnyProperty(FieldT::class, pName)
    }

    val properties: MutableMap<String, Any?>

    fun <FieldT: Any> getProperty(pClass: KClass<FieldT>, pName: String): FieldT

    fun <FieldT: Any> getNullableProperty(pClass: KClass<FieldT>, pName: String): FieldT?

    fun <FieldT: Any> getAnyProperty(pClass: KClass<FieldT>, pName: String): FieldT?

    // Nullable properties.
    fun <FieldT : Any, SelfT : Self<SelfT, NodeT>> createNlbDelegate(pClass: KClass<FieldT>, self: SelfT) =
        createDelegate { pDef -> createNlbProp(pDef.name, pClass, self) }

    fun <FieldT : Any, SelfT : Self<SelfT, NodeT>> createNlbProp(
        pName: String,
        pClass: KClass<FieldT>,
        self: SelfT
    ): NlbChPr<SelfT, FieldT, NodeT>

    // Non nullable properties.
    fun <FieldT : Any, SelfT : Self<SelfT, NodeT>> createDelegate(pClass: KClass<FieldT>, self: SelfT) =
        createDelegate { pDef -> createProp(pDef.name, pClass, self) }

    fun <FieldT : Any, SelfT : Self<SelfT, NodeT>> createProp(
        pName: String,
        pClass: KClass<FieldT>,
        self: SelfT
    ): ChPr<SelfT, FieldT, NodeT>

    fun <FieldT : Any, SelfT : Self<SelfT, NodeT>, PropT : PropBase<FieldT, SelfT, NodeT>> createDelegate(
        constructor: (KProperty<*>) -> PropT
    ) = ChPrDlg(constructor)
}

/**
 * Node that know its managed (which is created with delgation) properties meta info.
 */
interface MetaAble<MetaT : PropMeta> {

    val metas: MutableMap<String, MetaT>

    fun getMeta(pName: String): MetaT?
}

/**
 * Node that can be evaluated within chosen [EvalFactory].
 */
interface EvaluateAble<NodeT : EvaluateAble<NodeT>> : Node<NodeT> {

    fun <EvalT : Any, ArgT> eval(factory: EvalFactory<ArgT, NodeT>, arg: ArgT?, parentEval: Any? = null): EvalT?

    fun <EvalT : Any, ArgT> evalSafe(factory: EvalFactory<ArgT, NodeT>, arg: ArgT?, parentEval: Any? = null) =
        eval<EvalT, ArgT>(factory, arg, parentEval)!!

}

/**
 * Node that has parent and children, thus forming a tree.
 */
interface TreeAble<NodeT : TreeAble<NodeT>> : Node<NodeT> {

    /**
     * Node parent.
     */
    var parent: Lazy<NodeT>?

    /**
     * Node children.
     */
    val children: MutableList<NodeT>

}

/**
 * Node, that knows its self class.
 */
interface SelfClassAble<NodeT : SelfClassAble<NodeT>> : Node<NodeT> {

    /**
     * Self class.
     */
    val selfClass: KClass<*>
}