package com.tsarev.liquikotlin.infrastructure.api

import com.tsarev.liquikotlin.util.LazyObservingDelegate

/**
 * A way to get glue.
 */
interface GlueProvider<NodeT : Node<NodeT>> {
    /**
     * Used glue.
     */
    fun glue(self: Self<*>): Glue<NodeT>
}

/**
 * A class that determines node creation and manipulation strategy
 * and `node - self` interaction.
 *
 * [Glue] is made for internal DSL building purpose,
 * so all its methods should be internal, thus - not accessible while
 * using DSL.
 */
abstract class Glue<NodeT : Node<NodeT>> {

    /**
     * Data node.
     */
    internal abstract val node: NodeT

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
 * Utility method to create already built (thus, non builder) child node.
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