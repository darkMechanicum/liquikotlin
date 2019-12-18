package com.tsarev.liquikotlin.infrastructure.api

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