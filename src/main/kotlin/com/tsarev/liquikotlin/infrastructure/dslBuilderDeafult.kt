package com.tsarev.liquikotlin.infrastructure

import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.cast

/**
 * Base node implementation with field initializers.
 */
abstract class DefaultNode<SelfT : DefaultNode<SelfT>> :
    EvaluatableDslNode<SelfT>() {

    companion object {
        fun <SelfT : DefaultNode<SelfT>> addChildBuilder(
            self: SelfT,
            childName: String,
            child: Lazy<DefaultNode<*>>
        ) {
            self.childBuilders[childName] = child
        }

        fun <SelfT : DefaultNode<SelfT>, ChildT : DefaultNode<ChildT>> addChild(
            parent: SelfT, child: ChildT
        ) {
            child.realParent = parent
        }
    }

    var realParent: DefaultNode<*>? = null

    override val parent: EvaluatableDslNode<*>? get() = realParent

    override val parameters: MutableMap<String, Any?> = HashMap()

    override val children: MutableList<DefaultNode<*>> = ArrayList()

    override val childBuilders: MutableMap<String, Lazy<DefaultNode<*>>> = HashMap()

    override var hasDefault: Boolean = false

    override fun addChild(child: DslNode<*>) {
        if (child is DefaultNode<*>) {
            children.add(child as DefaultNode<*>)
        } else {
            throw IllegalArgumentException("Child with class ${child::class} cannot be set in $self")
        }
    }

    override fun buildNodeIfNeccessary(): SelfT = if (isBuilder) {
        if (realParent?.isBuilder == true) {
            realParent = realParent!!.buildNodeIfNeccessary() as DefaultNode<*>
        }

        val result = constructor()
        result.copyParametersFrom(this, true)
        if (parent != null) {
            val parenCast = parent as DslNode<*>
            DslNode.addChild(parenCast, result)
            result.realParent = self.realParent
        }
        result.isBuilder = false
        result
    } else {
        self
    }

    final override fun <FieldT : Any> nonNullable(fieldType: KClass<FieldT>, default: FieldT?): DefaultableDelegate<FieldT> =
        CommonDelegate(fieldType, default)

    final override fun <FieldT : Any> nullable(
        fieldType: KClass<FieldT>,
        default: FieldT?
    ): NullableDefaultableDelegate<FieldT> = NullableCommonDelegate(fieldType, default)

    protected fun <FieldT : Any> nonNullableWithCallback(
        fieldType: KClass<FieldT>,
        default: FieldT?,
        callback: ((NameableProperty<FieldT>, String) -> Unit)
    ): DefaultableDelegate<FieldT> =
        CommonDelegate(fieldType, default, callback)

    protected fun <FieldT : Any> nullableWithCallback(
        fieldType: KClass<FieldT>,
        default: FieldT?,
        callback: ((NameableProperty<FieldT>, String) -> Unit)
    ): NullableDefaultableDelegate<FieldT> = NullableCommonDelegate(fieldType, default, callback)


    private fun copyParametersFrom(other: DefaultNode<*>, shouldContinue: Boolean) {
        if (shouldContinue) {
            copySelfParameters(other as DefaultNode<SelfT>)
            this.childBuilders.entries.forEach { (builderName, thisBuilder) ->
                other.childBuilders[builderName]?.let {
                    thisBuilder.value.copyParametersFrom(
                        it.value,
                        it.value.hasDefault
                    )
                }
            }
        }
    }

    protected open fun copySelfParameters(other: DefaultNode<SelfT>) {
        this.parameters.putAll(other.parameters)
        this.hasDefault = other.hasDefault
    }

    open inner class CommonProperty<FieldT : Any>(
        override val propertyType: KClass<FieldT>,
        override val name: String,
        default: FieldT?
    ) :
        DefaultableProperty<FieldT>() {
        init {
            setDefault(default)
        }

        override fun invoke(value: FieldT) = self.buildNodeIfNeccessary().apply {
            this.parameters[name] = value
        }
        override val current: FieldT
            get() = propertyType.cast(
                parameters[name] ?: throw IllegalAccessException("Property $name should be set.")
            )
    }

    open inner class NullableCommonProperty<FieldT : Any>(
        override val propertyType: KClass<FieldT>,
        override val name: String,
        default: FieldT?
    ) : NullableDefaultableProperty<FieldT>() {
        init {
            setDefault(default)
        }

        override fun invoke(value: FieldT?) = self.buildNodeIfNeccessary().apply { parameters[name] = value }
        override val current: FieldT? get() = parameters[name]?.let { propertyType.cast(it) }
    }

    inner class CommonDelegate<FieldT : Any>(
        private val propertyType: KClass<FieldT>,
        override val default: FieldT?,
        private val callback: ((NameableProperty<FieldT>, String) -> Unit)? = null
    ) : DefaultableDelegate<FieldT>() {
        private val commonProperty by lazy { CommonProperty(propertyType, propertyName, default) }
        override operator fun getValue(thisRef: Any?, property: KProperty<*>): DefaultableProperty<FieldT> {
            propertyName = property.name
            callback?.invoke(commonProperty, propertyName)
            return commonProperty
        }
    }

    inner class NullableCommonDelegate<FieldT : Any>(
        private val propertyType: KClass<FieldT>,
        override val default: FieldT?,
        private val callback: ((NameableProperty<FieldT>, String) -> Unit)? = null
    ) : NullableDefaultableDelegate<FieldT>() {
        private val nullableCommonProperty by lazy { NullableCommonProperty(propertyType, propertyName, default) }
        override operator fun getValue(thisRef: Any?, property: KProperty<*>): NullableDefaultableProperty<FieldT> {
            propertyName = property.name
            callback?.invoke(nullableCommonProperty, propertyName)
            return nullableCommonProperty
        }
    }

}