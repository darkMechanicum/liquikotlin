package com.tsarev.liquikotlin.infrastructure.api

import com.tsarev.liquikotlin.util.letWhile
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.cast

/**
 * Class to hide implementation details.
 */
abstract class Self<SelfT : Self<SelfT, NodeT>, NodeT : Node<NodeT>>(
    private val selfClass: KClass<SelfT>
) {

    /**
     * Real node data.
     */
    internal abstract val node: NodeT

    /**
     * Get in the context.
     */
    protected abstract fun nodeDown()

    /**
     * Get out of the context.
     */
    protected abstract fun nodeUp()

    /**
     * Self link.
     */
    val self get() = selfClass.cast(this)

    /**
     * Closure support.
     */
    operator fun minus(modification: SelfT.() -> Unit): SelfT = self.apply {
        try {
            nodeDown()
            modification()
        } finally {
            nodeUp()
        }
    }
}

/**
 * Self that has support for children.
 */
abstract class ChildAbleSelf<SelfT : ChildAbleSelf<SelfT, NodeT>, NodeT : Node<NodeT>>(selfClass: KClass<SelfT>) :
    Self<SelfT, NodeT>(selfClass) {

    /**
     * Child add listener.
     */
    internal abstract fun onChildAdded(child: Self<*, NodeT>)

}

/**
 * Utility method to create child node.
 */
fun <ChildT, SelfT, NodeT> SelfT.child(childCtor: () -> ChildT)
        where SelfT : ChildAbleSelf<SelfT, NodeT>,
              ChildT : ChildAbleSelf<ChildT, NodeT>,
              NodeT : TreeAble<NodeT> = lazy { childCtor().also { this.onChildAdded(it) } }

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

    val properties: MutableMap<String, Any?>

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

/**
 * Node that know its managed (which is created with delgation) properties meta info.
 */
interface MetaAble {

    val metas: Collection<PropMeta>

    fun getMeta(pName: String): PropMeta?
}

/**
 * Node that can be evaluated within chosen [EvalFactory].
 */
interface EvaluateAble<NodeT : EvaluateAble<NodeT>> : Node<NodeT> {

    fun <EvalT : Any, ArgT> eval(factory: EvalFactory<ArgT>, arg: ArgT?, parentEval: Any? = null): EvalT?
}

/**
 * Factory to obtain specific [EvalAction] for each node.
 */
interface EvalFactory<ArgT> {

    fun <NodeT : Node<NodeT>, EvalT : Any> getAction(node: NodeT): EvalAction<NodeT, EvalT, ArgT>
}

/**
 * Hierarchy evaluation worker.
 */
interface EvalAction<NodeT : Node<NodeT>, EvalT : Any, ArgT> {

    /**
     * Perform evaluation before children.
     */
    fun doBefore(
        thisNode: NodeT,
        argument: ArgT?
    ): EvalT?

    /**
     * Perform evaluation after children.
     */
    fun doAfter(
        argument: ArgT?,
        thisNode: NodeT,
        childEvaluations: Collection<Any?>,
        childNodes: Collection<NodeT>,
        parentEval: Any?,
        resultEval: EvalT?
    ): EvalT?
}

/**
 * Node that has parent and children, thus forming a tree.
 */
interface TreeAble<NodeT : TreeAble<NodeT>> : Node<NodeT> {

    /**
     * Node parent.
     */
    var parent: NodeT?

    /**
     * Node children.
     */
    val children: MutableList<NodeT>

}

/**
 * Utility method to obtain tree root from any tree element.
 */
val <NodeT : TreeAble<NodeT>> NodeT.root: NodeT get() = this.letWhile { it.parent }