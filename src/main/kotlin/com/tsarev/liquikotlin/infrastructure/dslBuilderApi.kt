package com.tsarev.liquikotlin.infrastructure

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.cast

typealias Modifier<T> = (T.() -> Unit)

/**
 * Interface that can retrieve actual class instance.
 */
interface Selfable<SelfT : Selfable<SelfT>> {

    /**
     * Self class.
     */
    val thisClass: KClass<SelfT>

    val self get() = thisClass.cast(this)
}

/**
 * Base node interface.
 */
abstract class DslNode<SelfT : DslNode<SelfT>> : Selfable<SelfT> {

    companion object {
        fun addChild(parent: DslNode<*>, child: DslNode<*>) {
            parent.addChild(child)
        }
    }

    /**
     * How ordinary node should be instantiated.
     */
    protected open val constructor: (() -> SelfT) = { thisClass.java.newInstance() }

    /**
     * If we act as a builder now.
     */
    protected var isBuilder = true

    /**
     * Parent node.
     */
    internal abstract val parent: DslNode<*>?

    /**
     * Children nodes.
     */
    protected abstract val children: MutableList<out DslNode<*>>

    /**
     * Add child to this node.
     */
    protected abstract fun addChild(child: DslNode<*>)

    /**
     * Children builders.
     */
    protected abstract val childBuilders: MutableMap<String, out Lazy<DslNode<*>>>

    /**
     * Node parameters. Holds actual or default values depending on node's type (builder/non builder).
     */
    internal abstract val parameters: MutableMap<String, Any?>

    /**
     * Operator for nicer declarations.
     */
    operator fun minus(modifier: Modifier<SelfT>) = buildNodeIfNeccessary().apply(modifier)

    /**
     * Build new instance if this is builder or return self.
     */
    protected abstract fun buildNodeIfNeccessary(): SelfT

    /**
     * Create non nullable property.
     */
    protected abstract fun <FieldT : Any> nonNullable(fieldType: KClass<FieldT>): ChainableDelegate<FieldT>

    /**
     * Create nullable property.
     */
    protected abstract fun <FieldT : Any> nullable(fieldType: KClass<FieldT>): NullableChainableDelegate<FieldT>

    /**
     * Abstract property with name.
     */
    abstract inner class NameableProperty<FieldT : Any> {

        /**
         * Property name.
         */
        protected abstract val name: String

        /**
         * Property class.
         */
        abstract val propertyType: KClass<FieldT>

    }

    /**
     * Property, whose current value can be obtained.
     */
    interface Valuable<FieldT> {
        /**
         * Current property value.
         */
        val current: FieldT
    }

    /**
     * Property that can be invoked to set its value and return
     * parent object for chaining.
     */
    abstract inner class ChainableProperty<FieldT : Any> : NameableProperty<FieldT>(), Valuable<FieldT> {

        /**
         * Invocation style property setter.
         */
        abstract operator fun invoke(value: FieldT): SelfT
    }

    /**
     * Property that can be invoked to set its value and return
     * parent object for chaining.
     */
    abstract inner class NullableChainableProperty<FieldT : Any> : NameableProperty<FieldT>(), Valuable<FieldT?> {
        /**
         * Invocation style property setter.
         */
        abstract operator fun invoke(value: FieldT?): SelfT
    }

    abstract inner class DelegateBase {

        lateinit var propertyName: String

        /**
         * Set property name for this delegate.
         */
        open operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) =
            this.apply { propertyName = property.name }.apply { doInit(thisRef, property) }

        /**
         * Additional delegate initialization.
         */
        open fun doInit(thisRef: Any?, property: KProperty<*>) {}
    }

    /**
     * Delegate of declared node property.
     */
    abstract inner class ChainableDelegate<FieldT : Any> : DelegateBase() {
        override fun provideDelegate(thisRef: Any?, property: KProperty<*>) =
            super.provideDelegate(thisRef, property).let { this }
        abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): ChainableProperty<FieldT>
    }

    /**
     * Delegate of declared nullable node property
     */
    abstract inner class NullableChainableDelegate<FieldT : Any> : DelegateBase() {
        override fun provideDelegate(thisRef: Any?, property: KProperty<*>) =
            super.provideDelegate(thisRef, property).let { this }
        abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): NullableChainableProperty<FieldT>
    }

}

/**
 * Base node class with property defaults support.
 */
abstract class DefaultableDslNode<SelfT : DefaultableDslNode<SelfT>> : DslNode<SelfT>() {

    /**
     * Parent node.
     */
    abstract override val parent: DefaultableDslNode<*>?

    /**
     * Children nodes.
     */
    abstract override val children: MutableList<out DefaultableDslNode<*>>

    /**
     * Child builders.
     */
    abstract override val childBuilders: MutableMap<String, out Lazy<DefaultableDslNode<*>>>

    /**
     * Flag indicating that default parameters has been set for this node or its child.
     */
    protected abstract var hasDefault: Boolean

    /**
     * Set default flag for hierarchy.
     */
    protected fun setHasDefault() {
        var current: DefaultableDslNode<*>? = this
        while (current != null) {
            current.hasDefault = true
            current = current.parent
        }
    }

    /**
     * Create non nullable property.
     */
    protected abstract fun <FieldT : Any> nonNullable(
        fieldType: KClass<FieldT>,
        default: FieldT? = null
    ): DefaultableDelegate<FieldT>

    /**
     * Create nullable property.
     */
    protected abstract fun <FieldT : Any> nullable(
        fieldType: KClass<FieldT>,
        default: FieldT? = null
    ): NullableDefaultableDelegate<FieldT>

    final override fun <FieldT : Any> nonNullable(fieldType: KClass<FieldT>) =
        nonNullable(fieldType, null)

    final override fun <FieldT : Any> nullable(fieldType: KClass<FieldT>) =
        nullable(fieldType, null)

    /**
     * Property that can be invoked to set its value and return
     * parent object for chaining.
     */
    abstract inner class DefaultableProperty<FieldT : Any> : ChainableProperty<FieldT>() {
        /**
         * If this is builder node property,
         * so set its value. Otherwise no effect.
         */
        fun setDefault(value: FieldT?): SelfT {
            if (isBuilder && value != null) {
                setHasDefault()
                parameters[name] = value
            }
            return self
        }

    }

    /**
     * Property that can be invoked to set its value and return
     * parent object for chaining.
     */
    abstract inner class NullableDefaultableProperty<FieldT : Any> : NullableChainableProperty<FieldT>() {
        /**
         * If this is builder node property,
         * so set its value. Otherwise no effect.
         */
        fun setDefault(value: FieldT?): SelfT {
            if (isBuilder) {
                setHasDefault()
                parameters[name] = value
            }
            return self
        }
    }

    /**
     * Delegate of declared node property.
     */
    abstract inner class DefaultableDelegate<FieldT : Any> : ChainableDelegate<FieldT>() {
        abstract val default: FieldT?
        override fun provideDelegate(thisRef: Any?, property: KProperty<*>) =
            super.provideDelegate(thisRef, property).let { this }
        abstract override operator fun getValue(thisRef: Any?, property: KProperty<*>): DefaultableProperty<FieldT>
    }

    /**
     * Delegate of declared nullable node property
     */
    abstract inner class NullableDefaultableDelegate<FieldT : Any> : NullableChainableDelegate<FieldT>() {
        abstract val default: FieldT?
        override fun provideDelegate(thisRef: Any?, property: KProperty<*>) =
            super.provideDelegate(thisRef, property).let { this }
        abstract override operator fun getValue(
            thisRef: Any?,
            property: KProperty<*>
        ): NullableDefaultableProperty<FieldT>
    }

}

/**
 * Base node class with property defaults support.
 */
abstract class EvaluatableDslNode<SelfT : EvaluatableDslNode<SelfT>> :
    DefaultableDslNode<SelfT>() {

    /**
     * Parent node.
     */
    abstract override val parent: EvaluatableDslNode<*>?

    /**
     * Children nodes.
     */
    abstract override val children: MutableList<out EvaluatableDslNode<*>>

    /**
     * Child builders.
     */
    abstract override val childBuilders: MutableMap<String, out Lazy<EvaluatableDslNode<*>>>

    /**
     * Get evaluator from this node.
     */
    fun <EvalT : Any, ArgT> getEvaluator(factory: EvaluatorFactory<ArgT>)
            = factory.getEvaluatorFor<SelfT, EvalT>(this.self)

    /**
     * Evaluate node with null check.
     */
    open fun <EvalT : Any, ArgT> evalSafe(factory: EvaluatorFactory<ArgT>, arg: ArgT?, parentEval: Any? = null): EvalT =
        eval(factory, arg, parentEval)!!

    /**
     * Evaluate node.
     */
    open fun <EvalT : Any, ArgT> eval(factory: EvaluatorFactory<ArgT>, arg: ArgT?, parentEval: Any? = null): EvalT? {
        val evaluator = factory.getEvaluatorFor<SelfT, EvalT>(this.self)
        val result = evaluator.initResult(self, arg)
        val childEvaluations = children.map { it.eval<Any, ArgT>(factory, arg, result) }
        return evaluator.eval(childEvaluations, arg, self, parentEval, result)
    }

    /**
     * Entry point for any evaluation process.
     */
    abstract class EvaluatorFactory<ArgT> {

        /**
         * Get evaluator for node.
         */
        abstract fun <NodeT: DslNode<NodeT>, EvalT : Any> getEvaluatorFor(node: NodeT): Evaluator<NodeT, EvalT, ArgT>
    }

    /**
     * Hierarchy evaluation worker.
     */
    abstract class Evaluator<NodeT : Selfable<NodeT>, EvalT : Any, ArgT> {

        /**
         * Create empty eval result if need.
         */
        internal open fun initResult(thisNode: NodeT, argument: ArgT?): EvalT? = null

        /**
         * Perform all node evaluation.
         */
        abstract fun eval(
            childEvaluations: Collection<Any?> = emptyList(),
            argument: ArgT?,
            thisNode: NodeT,
            parentEval: Any?,
            resultEval: EvalT?
        ): EvalT?
    }
}