package com.tsarev.liquikotlin.infrastructure.api

import com.tsarev.liquikotlin.util.LazyObservingDelegate
import com.tsarev.liquikotlin.util.letWhile
import kotlin.reflect.KClass
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
     * Self link.
     */
    val self get() = selfClass.cast(this)

    /**
     * Closure support.
     */
    operator fun minus(modification: SelfT.() -> Unit): SelfT = self.apply(modification)
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

    /**
     * Child access listener.
     */
    internal abstract fun onChildAccessed(child: Self<*, NodeT>)

}

/**
 * Utility method to create child node.
 */
fun <ChildT, SelfT, NodeT> SelfT.child(childCtor: () -> ChildT)
        where SelfT : ChildAbleSelf<out SelfT, NodeT>,
              ChildT : ChildAbleSelf<ChildT, NodeT>,
              NodeT : TreeAble<NodeT> = LazyObservingDelegate(
    { childCtor().also { this.onChildAdded(it) } },
    { this.onChildAccessed(it) }
)

/**
 * Utility method to create non builder child node.
 */
fun <ChildT, SelfT, NodeT> SelfT.builtChild(childCtor: () -> ChildT)
        where SelfT : ChildAbleSelf<out SelfT, NodeT>,
              ChildT : ChildAbleSelf<ChildT, NodeT>,
              NodeT : TreeAble<NodeT>,
              NodeT : BuilderAble<NodeT> = LazyObservingDelegate(
    { childCtor().also { this.onChildAdded(it) } },
    { this.onChildAccessed(it); it.node.build() }
)

/**
 * Factory to obtain specific [EvalAction] for each node.
 */
interface EvalFactory<ArgT, NodeT : Node<NodeT>> {

    fun <EvalT : Any> getAction(node: NodeT): EvalAction<NodeT, EvalT, ArgT>
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
 * Utility method to obtain tree root from any tree element.
 */
val <NodeT : TreeAble<NodeT>> NodeT.root: NodeT get() = this.letWhile { it.parent?.value }