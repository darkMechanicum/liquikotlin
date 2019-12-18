package com.tsarev.liquikotlin.infrastructure.api

import com.tsarev.liquikotlin.util.LazyObservingDelegate
import com.tsarev.liquikotlin.util.letWhile
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

/**
 * Class to allow chaining in DSL.
 */
abstract class Self<out SelfT : Self<SelfT>>(
    private val selfClass: KClass<SelfT>
) {

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
abstract class ChildAbleSelf<out SelfT : ChildAbleSelf<SelfT>>(selfClass: KClass<SelfT>) :
    Self<SelfT>(selfClass) {

    /**
     * Child add listener.
     */
    internal abstract fun onChildAdded(child: Self<*>)

    /**
     * Child access listener.
     */
    internal abstract fun onChildAccessed(child: Self<*>)

}

/**
 * Utility method to create child node.
 */
fun <ChildT, SelfT, NodeT> GlueProvider<NodeT>.child(
    childCtor: () -> ChildT,
    self: SelfT
) where SelfT : ChildAbleSelf<SelfT>,
        ChildT : ChildAbleSelf<ChildT>,
        NodeT : TreeAble<NodeT> = glue(self).run {
    LazyObservingDelegate(
        { childCtor().also { self.onChildAdded(it) } },
        { self.onChildAccessed(it) }
    )
}


/**
 * Utility method to create non builder child node.
 */
fun <ChildT, SelfT, NodeT> GlueProvider<NodeT>.builtChild(
    childCtor: () -> ChildT,
    self: SelfT
) where SelfT : ChildAbleSelf<SelfT>,
        ChildT : ChildAbleSelf<ChildT>,
        NodeT : TreeAble<NodeT>,
        NodeT : BuilderAble<NodeT> = glue(self).run {
    LazyObservingDelegate(
        { childCtor().also { self.onChildAdded(it) } },
        { self.onChildAccessed(it); this.node.build() }
    )
}

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