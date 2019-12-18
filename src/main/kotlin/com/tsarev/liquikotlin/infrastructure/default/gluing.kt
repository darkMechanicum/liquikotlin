package com.tsarev.liquikotlin.infrastructure.default

import com.tsarev.liquikotlin.infrastructure.api.*
import java.util.*
import kotlin.reflect.KClass

/**
 * Class that aggregates all needed functionality by
 * delegation.
 */
abstract class SkeletonNode<NodeT : SkeletonNode<NodeT>>(
    private val treeAble: TreeAble<NodeT>,
    private val builderAble: BuilderAble<NodeT>,
    private val defaultPropertyAble: DefaultPropertyAble<NodeT>,
    private val evaluateAble: EvaluateAble<NodeT>,
    private val copyAble: CopyAble<NodeT>,
    private val selfClassAble: SelfClassAble<NodeT>
) : TreeAble<NodeT> by treeAble,
    BuilderAble<NodeT> by builderAble,
    DefaultPropertyAble<NodeT> by defaultPropertyAble,
    EvaluateAble<NodeT> by evaluateAble,
    CopyAble<NodeT> by copyAble,
    SelfClassAble<NodeT> by selfClassAble {

    override fun init(root: NodeT) {}

    final override val node by lazy { realNode }

    abstract val realNode: NodeT

    init {
        copyAble.init(node)
        treeAble.init(node)
        builderAble.init(node)
        defaultPropertyAble.init(node)
        evaluateAble.init(node)
    }
}

/**
 * Implementation of [SkeletonNode] with default delegates.
 */
class DefaultNode(
    selfClass: KClass<*>,
    buildCtor: DefaultNode.() -> DefaultNode = { this } // No building by default.
) : SkeletonNode<DefaultNode>(
    DefaultTreeAble(),
    DefaultBuilderAble(buildCtor),
    DefaultPropertyAbleImpl(),
    DefaultEvaluateAble(),
    DefaultCopyAble { DefaultNode(selfClass) },
    DefaultSelfClassAble(selfClass)
) {
    override val realNode get() = this
}

/**
 * Default base class for all DSL nodes.
 */
open class DefaultSelf<out SelfT : DefaultSelf<SelfT>>(selfClass: KClass<SelfT>) : ChildAbleSelf<SelfT>(selfClass) {
    @Suppress("LeakingThis") // Here we should leak `this` link to provide callback from glue.
    val managedGlue = DefaultGlue(this)
    override fun onChildAdded(child: Self<*>) = DefaultGlueProvider.glue(self).onChildAdded(child)
    override fun onChildAccessed(child: Self<*>) = DefaultGlueProvider.glue(self).onChildAccessed(child)
}

/**
 * Default glue provider, based on mapping.
 */
object DefaultGlueProvider : GlueProvider<DefaultNode> {
    override fun glue(self: Self<*>): Glue<DefaultNode> {
        return (self as? DefaultSelf<*>)?.let { doGlue(it) }
            ?: throw RuntimeException("Not supported SelfT type: ${self::class}")
    }
    fun doGlue(self: DefaultSelf<*>) = self.managedGlue
}

/**
 * Node and Self linking logic.
 */
open class DefaultGlue(
    private val self: Any
) : Glue<DefaultNode>() {

    override fun onChildAdded(child: Self<*>) {
        (child as? DefaultSelf<*>)?.let { doOnChildAdded(it) }
            ?: throw RuntimeException("Unexpected type for child node: ${child::class}")
    }

    override fun onChildAccessed(child: Self<*>) {
        (child as? DefaultSelf<*>)?.let { doOnChildAccessed(it) }
            ?: throw RuntimeException("Unexpected type for child node: ${child::class}")
    }

    private fun doOnChildAdded(it: DefaultSelf<*>) {
        val childGlue = DefaultGlueProvider.doGlue(it)
        childGlue.parent = this
        this.children.add(childGlue)
        childGlue.node.parent = lazy { this@DefaultGlue.node }
    }

    private fun doOnChildAccessed(it: DefaultSelf<*>) {
        val childGlue = DefaultGlueProvider.doGlue(it)
        if (childGlue.realNode != null) {
            childGlue.clearRealNodeRec()
            childGlue.popTemplateRec()
        }
        childGlue.copyTemplateRec()
    }

    private val templateStack = ArrayList<DefaultNode>()

    private var realNode: DefaultNode? = null

    private var parent: DefaultGlue? = null

    private val children = ArrayList<DefaultGlue>()

    override val node: DefaultNode
        get() = realNode ?: lastTemplate

    private val lastTemplate get() = templateStack.lastOrNull() ?: initNode()

    private fun initNode() = DefaultNode(this@DefaultGlue.self::class) {
        DefaultNode(this@DefaultGlue.self::class).also { this@DefaultGlue.realNode = it }
    }.also { templateStack.add(it) }

    private fun clearRealNodeRec() {
        if (parent != null) realNode = null
        children.forEach { it.clearRealNodeRec() }
    }

    private fun copyTemplateRec() {
        templateStack.add(lastTemplate.copy().also {
            it.parent = this@DefaultGlue.parent?.let { lazy { it.node } }
        })
        children.forEach { it.copyTemplateRec() }
    }

    private fun popTemplateRec() {
        if (templateStack.size > 0) templateStack.removeAt(templateStack.size - 1)
        children.forEach { it.popTemplateRec() }
    }

}