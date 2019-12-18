package com.tsarev.liquikotlin.infrastructure.default

import com.tsarev.liquikotlin.infrastructure.api.*
import com.tsarev.liquikotlin.util.ignore
import org.jetbrains.kotlin.backend.common.push
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.cast
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.safeCast
import kotlin.reflect.jvm.jvmErasure

/**
 * Default [Node] implementation.
 */
abstract class NodeBase<NodeT : Node<NodeT>> : Node<NodeT> {

    private lateinit var node0: NodeT

    override val node by lazy { node0 }

    override fun init(root: NodeT) {
        node0 = root
        node
        initAfterBase(root)
    }

    /**
     * Additional fun in case someone forgets to call super.init.
     */
    open fun initAfterBase(root: NodeT): Any? = null

}

/**
 * Default [TreeAble] implementation.
 */
class DefaultTreeAble<NodeT> : NodeBase<NodeT>(), TreeAble<NodeT>
        where NodeT : TreeAble<NodeT>,
              NodeT : BuilderAble<NodeT>,
              NodeT : CopyAble<NodeT> {

    private var realParent: Lazy<NodeT>? = null

    override var parent
        get() = realParent
        set(value) = doSetParent(value)

    private fun doSetParent(newParent: Lazy<NodeT>?) {
        if (!node.isBuilder) {
            node.parent?.value?.children?.remove(node)
            newParent?.value?.children?.add(node)
        }
        this.realParent = newParent
    }

    override val children: MutableList<NodeT> = ArrayList()

    override fun initAfterBase(root: NodeT) {
        root.onCopy { new, old -> new.parent = old.parent }
        root.onBuilt { new, old ->
            val builtParent = old.parent?.value?.build()
            old.parent = builtParent?.let { lazy { it } }
            new.parent = builtParent?.let { lazy { it } }
        }
    }
}

/**
 * Default [EvaluateAble] implementation.
 */
class DefaultEvaluateAble<NodeT> : NodeBase<NodeT>(), EvaluateAble<NodeT>
        where NodeT : TreeAble<NodeT>,
              NodeT : EvaluateAble<NodeT> {

    override fun <EvalT : Any, ArgT> eval(factory: EvalFactory<ArgT, NodeT>, arg: ArgT?, parentEval: Any?): EvalT? {
        val action = factory.getAction<EvalT>(node)
        val result = action.doBefore(node, arg)
        val childEvaluations = node.children.map { it.eval<Any, ArgT>(factory, arg, result) }
        val childSelves = node.children.map { node }
        return action.doAfter(arg, node, childEvaluations, childSelves, parentEval, result)
    }

}

/**
 * Default [BuilderAble] implementation.
 */
class DefaultBuilderAble<NodeT>(
    override var ctor: NodeT.() -> NodeT
) : NodeBase<NodeT>(),
    BuilderAble<NodeT>
        where NodeT : BuilderAble<NodeT>,
              NodeT : CopyAble<NodeT> {

    override var isBuilder = true

    private val customizers = ArrayList<(NodeT, NodeT) -> Unit>()

    override fun initAfterBase(root: NodeT) = node.onCopy { new, old ->
        new.ctor = old.ctor
    }

    override fun build() = if (isBuilder) {
        node.ctor().apply {
            isBuilder = false
            customizers.forEach {
                it(node, this@DefaultBuilderAble.node)
            }
        }
    } else {
        node
    }

    override fun onBuilt(customizer: (NodeT, NodeT) -> Unit) = run { customizers.push(customizer) }.ignore
}

/**
 * Default [CopyAble] implementation.
 */
class DefaultCopyAble<NodeT>(
    val ctor: NodeT.() -> NodeT
) : NodeBase<NodeT>(),
    CopyAble<NodeT>
        where NodeT : CopyAble<NodeT> {

    private val customizers: MutableCollection<(NodeT, NodeT) -> Unit> = ArrayList()

    override fun copy() = node.ctor().apply {
        customizers.forEach {
            it(node, this@DefaultCopyAble.node)
        }
    }

    override fun onCopy(customizer: (NodeT, NodeT) -> Unit) = run { customizers.add(customizer) }.ignore

}

/**
 * Default [DfltNlbChPr] implementation.
 */
class DefaultNullableProperty<SelfT, FieldT, NodeT>(
    override val pName: String,
    override val pClass: KClass<FieldT>,
    override val self: SelfT
) : DfltNlbChPr<SelfT, FieldT, NodeT>
        where SelfT : Self<SelfT, NodeT>,
              FieldT : Any,
              NodeT : DefaultPropertyAble<NodeT>,
              NodeT : BuilderAble<NodeT> {

    override val current: FieldT? get() = pClass.safeCast(self.node.properties[pName])

    override fun invoke(value: FieldT?) = self.apply { this@apply.node.build().properties[pName] = value }

    override var default: FieldT?
        get() = if (self.node.isBuilder) this.current else null
        set(value) {
            if (self.node.isBuilder) {
                self.node.properties[pName] = value; self.node.hasDefault = true
            }
        }
}

/**
 * Default [DfltChPr] implementation.
 */
class DefaultProperty<SelfT, FieldT, NodeT>(
    override val pName: String,
    override val pClass: KClass<FieldT>,
    override val self: SelfT
) : DfltChPr<SelfT, FieldT, NodeT>
        where SelfT : Self<SelfT, NodeT>,
              FieldT : Any,
              NodeT : DefaultPropertyAble<NodeT>,
              NodeT : BuilderAble<NodeT> {

    override val current: FieldT get() = pClass.cast(self.node.properties[pName])

    override fun invoke(value: FieldT) = self.apply { this@apply.node.build().properties[pName] = value }

    override var default: FieldT?
        get() = if (self.node.isBuilder) this.current else null
        set(value) {
            if (self.node.isBuilder && value != null) {
                self.node.properties[pName] = value; self.node.hasDefault = true
            }
        }
}

/**
 * Naive implementation of [DefaultPropertyAble].
 */
class DefaultPropertyAbleImpl<NodeT> : NodeBase<NodeT>(),
    DefaultPropertyAble<NodeT>
        where NodeT : DefaultPropertyAble<NodeT>,
              NodeT : TreeAble<NodeT>,
              NodeT : CopyAble<NodeT>,
              NodeT : BuilderAble<NodeT> {

    private val propertiesCopier = { newNode: NodeT, oldNode: NodeT ->
        newNode.properties.putAll(oldNode.properties)
        newNode.metas.putAll(oldNode.metas)
    }

    override fun initAfterBase(root: NodeT) {
        root.onBuilt(propertiesCopier)
        root.onCopy(propertiesCopier)
    }

    override fun <FieldT : Any> getProperty(pClass: KClass<FieldT>, pName: String) =
        properties[pName]?.takeIf { getMeta(pName)?.type == pClass }?.let { pClass.cast(it) }!!

    override fun <FieldT : Any> getNullableProperty(pClass: KClass<FieldT>, pName: String) =
        properties[pName]?.takeIf { getMeta(pName)?.type == pClass }?.let { pClass.safeCast(it) }

    override fun <FieldT : Any> getAnyProperty(pClass: KClass<FieldT>, pName: String): FieldT? {
        val meta = getMeta(pName)
        return when {
            meta == null -> null
            meta.isNullable -> getNullableProperty(pClass, pName)
            else -> getProperty(pClass, pName)
        }
    }

    override fun <FieldT : Any, SelfT : Self<SelfT, NodeT>> createNlbProp(
        pName: String,
        pClass: KClass<FieldT>,
        self: SelfT
    ) = DefaultNullableProperty(pName, pClass, self)

    override fun <FieldT : Any, SelfT : Self<SelfT, NodeT>> createProp(
        pName: String,
        pClass: KClass<FieldT>,
        self: SelfT
    ) = DefaultProperty(pName, pClass, self)

    override val properties: MutableMap<String, Any?> = HashMap()

    override var hasDefault: Boolean = false

    private val metaMap = HashMap<String, MetaWithDefault>()

    override fun getMeta(pName: String): MetaWithDefault? = metaMap[pName]

    override val metas get() = metaMap

    override fun <FieldT : Any, SelfT : Self<SelfT, NodeT>, PropT : PropBase<FieldT, SelfT, NodeT>> createDelegate(
        constructor: (KProperty<*>) -> PropT
    ) = object : ChPrDlg<SelfT, FieldT, NodeT, PropT>(constructor) {
        override operator fun provideDelegate(thisRef: Any?, prop: KProperty<*>) =
            super.provideDelegate(thisRef, prop).apply {
                val isNullable = this.prop.pClass.isSubclassOf(NlbChPr::class)
                val fieldClass = prop.returnType.jvmErasure
                metaMap[prop.name] = MetaWithDefault(
                    prop.name,
                    this.prop.pClass,
                    prop.annotations,
                    prop,
                    { it.getAnyProperty(fieldClass, prop.name) },
                    isNullable,
                    { node.isBuilder && properties.containsKey(prop.name) }
                )
            }
    }

}

class DefaultSelfClassAble<NodeT>(
    override val selfClass: KClass<*>
) : NodeBase<NodeT>(), SelfClassAble<NodeT>
        where NodeT : SelfClassAble<NodeT>