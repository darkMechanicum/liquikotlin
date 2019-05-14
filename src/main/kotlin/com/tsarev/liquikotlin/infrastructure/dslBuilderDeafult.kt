package com.tsarev.liquikotlin.infrastructure

import com.tsarev.liquikotlin.bundled.LkChangeLog
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.cast

const val enableLineNumberInfo = true

private val thisPackage = DefaultNode::class.qualifiedName?.substringBeforeLast('.')!!
private val budnledPackage = LkChangeLog::class.qualifiedName?.substringBeforeLast('.')!!

private fun getCallerLineInfo() = Thread.currentThread()
    .stackTrace
    .drop(1) // Skip Thread.currentThread().stackTrace method
    .dropWhile { it.className.startsWith(thisPackage) || it.className.startsWith(budnledPackage) }
    .firstOrNull()

private fun getLineInfo() = Any().takeIf { enableLineNumberInfo }
    ?.let { getCallerLineInfo() }
    .let { trace ->
        "Line number is ${trace?.lineNumber ?: "unknown"}." +
                trace?.let { "\n\t(IDE hint) at ${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})" }
                    .takeIf { trace?.className != null && trace.methodName != null && trace.fileName != null }
    }

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
            parent: SelfT, child: ChildT, asNode: Boolean = false
        ) {
            child.realParent = parent
            if (asNode) parent.addChild(child)
        }
    }

    var realParent: DefaultNode<*>? = null

    override val parent: EvaluatableDslNode<*>? get() = realParent

    override lateinit var parameters: MutableMap<String, Any?>

    override lateinit var children: MutableList<DefaultNode<*>>

    override lateinit var childBuilders: MutableMap<String, Lazy<DefaultNode<*>>>

    override var hasDefault: Boolean = false

    override val propertyMeta: MutableMap<String, PropertyMeta> = HashMap()

    private val nonNullableProperties = ArrayList<CommonProperty<*>>()

    var lineInfo: String = ""

    private fun initDefaults() {
        parameters = HashMap()
        children = ArrayList()
        childBuilders = HashMap()
        hasDefault = false
    }

    init {
        initDefaults()
    }

    override fun addChild(child: DslNode<*>) {
        if (child is DefaultNode<*>) {
            children.add(child)
        } else {
            throw IllegalArgumentException("Child with class ${child::class} cannot be set in $self")
        }
    }

    override fun buildNodeIfNeccessary(): SelfT = if (isBuilder) {
        constructor().apply {
            copyParametersFrom(this@DefaultNode, true)
            val builderParent = this@DefaultNode.realParent
            if (builderParent != null) {
                realParent = builderParent.buildNodeIfNeccessary()
                addChild(realParent as DslNode<*>, this)
            }
            isBuilder = false
            lineInfo = getLineInfo()
        }
    } else {
        self
    }

    final override fun <EvalT : Any, ArgT> eval(factory: EvaluatorFactory<ArgT>, arg: ArgT?, parentEval: Any?): EvalT? {
        // Validate all non null properties.
        nonNullableProperties.forEach { it.current }
        return super.eval(factory, arg, parentEval)
    }

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

    override val propertyFactory = DefaultPropertyFactory()

    /**
     * Default implementation of [DefaultableDslNode.DefaultablePropertyFactory].
     */
    inner class DefaultPropertyFactory : DefaultablePropertyFactory() {
        override fun <FieldT : Any> createNonNullable(propField: KProperty<*>, propClass: KClass<FieldT>) =
            CommonProperty(propClass, propField.name).also {
                propertyMeta[propField.name] =
                    PropertyMeta(propField.name, it, propClass, propField.annotations, propField, false, false)
            }

        override fun <FieldT : Any> createNullable(propField: KProperty<*>, propClass: KClass<FieldT>) =
            NullableCommonProperty(propClass, propField.name).also {
                propertyMeta[propField.name] =
                    PropertyMeta(propField.name, it, propClass, propField.annotations, propField, true, false)
            }
    }

    /**
     * Add meta info about default value.
     */
    final override fun <FieldT : Any> nonNullable(fieldType: KClass<FieldT>, default: FieldT?) =
        super.nonNullable(fieldType, default)
            .andLater { propertyMeta.computeIfPresent(propDefinition.name) { _, old -> old.copy(hasDefaultByDefinition = true) } }

    /**
     * Add meta info about default value.
     */
    final override fun <FieldT : Any> nullable(fieldType: KClass<FieldT>, default: FieldT?) =
        super.nullable(fieldType, default)
            .andLater { propertyMeta.computeIfPresent(propDefinition.name) { _, old -> old.copy(hasDefaultByDefinition = true) } }

    /**
     * Default non nullable property implementation.
     */
    open inner class CommonProperty<FieldT : Any>(
        override val propertyType: KClass<FieldT>,
        override val name: String
    ) :
        DefaultableProperty<FieldT>() {
        override fun invoke(value: FieldT) = self.buildNodeIfNeccessary().apply { this.parameters[name] = value }
        override val current: FieldT
            get() = propertyType.cast(
                parameters[name]
                    ?: throw IllegalAccessException("Property $name should be set.${this@DefaultNode.lineInfo}")
            )
    }

    /**
     * Default nullable property implementation.
     */
    open inner class NullableCommonProperty<FieldT : Any>(
        override val propertyType: KClass<FieldT>,
        override val name: String
    ) : NullableDefaultableProperty<FieldT>() {
        override fun invoke(value: FieldT?) = self.buildNodeIfNeccessary().apply { parameters[name] = value }
        override val current: FieldT? get() = parameters[name]?.let { propertyType.cast(it) }
    }

}