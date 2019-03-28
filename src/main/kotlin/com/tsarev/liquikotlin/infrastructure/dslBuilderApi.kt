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
    abstract val parent: DslNode<*>?

    /**
     * Children nodes.
     */
    protected abstract val children: MutableList<out DslNode<*>>

    protected abstract fun addChild(child: DslNode<*>)

    /**
     * Children builders.
     */
    protected abstract val childBuilders: MutableMap<String, out Lazy<DslNode<*>>>

    /**
     * Node parameters. Holds actual or default values depending on node's type (builder/non builder).
     */
    protected abstract val parameters: MutableMap<String, Any?>

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
     * Property that can be invoked to set its value and return
     * parent object for chaining.
     */
    abstract inner class ChainableProperty<FieldT : Any> : NameableProperty<FieldT>() {

        /**
         * Invocation style property setter.
         */
        abstract operator fun invoke(value: FieldT): SelfT

        /**
         * Current property value.
         */
        abstract val current: FieldT
    }

    /**
     * Property that can be invoked to set its value and return
     * parent object for chaining.
     */
    abstract inner class NullableChainableProperty<FieldT : Any> : NameableProperty<FieldT>() {
        /**
         * Invocation style property setter.
         */
        abstract operator fun invoke(value: FieldT?): SelfT

        /**
         * Current property value.
         */
        abstract val current: FieldT?
    }

    abstract inner class DelegateBase {
        lateinit var propertyName: String
    }

    /**
     * Delegate of declared node property.
     */
    abstract inner class ChainableDelegate<FieldT : Any> : DelegateBase() {
        abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): ChainableProperty<FieldT>
    }

    /**
     * Delegate of declared nullable node property
     */
    abstract inner class NullableChainableDelegate<FieldT : Any> : DelegateBase() {
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
    protected abstract override val children: MutableList<out DefaultableDslNode<*>>

    /**
     * Child builders.
     */
    protected abstract override val childBuilders: MutableMap<String, out Lazy<DefaultableDslNode<*>>>

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

    protected final override fun <FieldT : Any> nonNullable(fieldType: KClass<FieldT>) =
        nonNullable(fieldType, null)

    protected final override fun <FieldT : Any> nullable(fieldType: KClass<FieldT>) =
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
        abstract override operator fun getValue(thisRef: Any?, property: KProperty<*>): DefaultableProperty<FieldT>
    }

    /**
     * Delegate of declared nullable node property
     */
    abstract inner class NullableDefaultableDelegate<FieldT : Any> : NullableChainableDelegate<FieldT>() {
        abstract val default: FieldT?
        abstract override operator fun getValue(
            thisRef: Any?,
            property: KProperty<*>
        ): NullableDefaultableProperty<FieldT>
    }

}

/**
 * Base node class with property defaults support.
 */
abstract class EvaluatableDslNode<SelfT : EvaluatableDslNode<SelfT, EvalT, ArgT>, EvalT, ArgT> :
    DefaultableDslNode<SelfT>() {

    /**
     * Parent node.
     */
    abstract override val parent: EvaluatableDslNode<*, *, ArgT>?

    /**
     * Children nodes.
     */
    protected abstract override val children: MutableList<out EvaluatableDslNode<*, *, ArgT>>

    /**
     * Child builders.
     */
    protected abstract override val childBuilders: MutableMap<String, out Lazy<EvaluatableDslNode<*, *, ArgT>>>

    /**
     * Property evaluations.
     */
    protected var propertyEvaluationChain: MutableMap<String, (EvalT, SelfT, ArgT?) -> Unit> = HashMap()

    /**
     * Evaluate current node.
     */
    protected abstract fun createEvalResult(argument: ArgT?): EvalT

    /**
     * Perform children evaluation.
     */
    protected open fun evalChild(evalResult: EvalT, child: EvaluatableDslNode<*, *, ArgT>, argument: ArgT?): EvalT {
        child.eval(argument, self, evalResult)
        return evalResult
    }

    /**
     * Perform children evaluation.
     */
    protected open fun evalChildren(evalResult: EvalT, argument: ArgT?): EvalT {
        children.forEach { evalChild(evalResult, it, argument) }
        return evalResult
    }

    /**
     * Perform all node evaluation.
     */
    open fun eval(
        argument: ArgT? = null,
        parent: EvaluatableDslNode<*, *, ArgT>? = null,
        parentEval: Any? = null
    ): EvalT {
        val evalResult = createEvalResult(argument)
        propertyEvaluationChain.values.forEach { it(evalResult, self, argument) }
        evalChildren(evalResult, argument)
        return evalResult
    }


}