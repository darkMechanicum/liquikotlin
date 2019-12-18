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
open class DefaultSelf<SelfT : DefaultSelf<SelfT>>(selfClass: KClass<SelfT>) :
    ChildAbleSelf<SelfT, DefaultNode>(selfClass) {

    override fun onChildAdded(child: Self<*, DefaultNode>) {
        (child as? DefaultSelf<*>)?.let {
            it.parent = this
            this.children.add(it)
            it.node.parent = lazy { this@DefaultSelf.node }
        }
    }

    private val templateStack = ArrayList<DefaultNode>()

    private var realNode: DefaultNode? = null

    var parent: DefaultSelf<*>? = null

    private val children = ArrayList<DefaultSelf<*>>()

    override val node: DefaultNode
        get() = realNode ?: lastTemplate

    private val lastTemplate get () = templateStack.lastOrNull() ?: initNode()

    private fun initNode() = DefaultNode(this@DefaultSelf::class) {
        DefaultNode(this@DefaultSelf::class).also { this@DefaultSelf.realNode = it }
    }.also { templateStack.add(it) }

    private fun clearRealNodeRec() {
        if (parent != null) realNode = null
        children.forEach { it.clearRealNodeRec() }
    }

    private fun copyTemplateRec() {
        templateStack.add(lastTemplate.copy().also {
            it.parent = this@DefaultSelf.parent?.let { lazy { it.node } }
        })
        children.forEach { it.copyTemplateRec() }
    }

    private fun popTemplateRec() {
        if (templateStack.size > 0) templateStack.removeAt(templateStack.size - 1)
        children.forEach { it.popTemplateRec() }
    }

    private var shouldNodeUp = false

    override fun nodeDown() {
        if (realNode == null) {
            children.forEach { it.copyTemplateRec() }
            lastTemplate.build()
            shouldNodeUp = true
        }
    }

    override fun nodeUp() {
        if (shouldNodeUp) {
            children.forEach { it.popTemplateRec() }
            shouldNodeUp = false
        }
        clearRealNodeRec()
    }

}
