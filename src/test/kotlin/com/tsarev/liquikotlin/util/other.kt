package com.tsarev.liquikotlin.util

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.EvaluatableDslNode
import com.tsarev.liquikotlin.infrastructure.LbArg
import com.tsarev.liquikotlin.integration.*
import liquibase.change.core.*
import liquibase.changelog.DatabaseChangeLog
import liquibase.resource.ResourceAccessor
import java.io.File
import java.io.InputStream
import kotlin.reflect.KClass

fun <ArgT> EvaluatableDslNode<*>.generalEval(factory: EvaluatableDslNode.EvaluatorFactory<ArgT>, arg: ArgT? = null) =
    this.eval<Any, ArgT>(factory, arg)

/**
 * Path with replaced '\'s since liquibase interprets them incorrectly in win environment.
 */
val File.patchedAbs get() = this.absolutePath.replace('\\', '/')

/**
 * Dummy test accessor.
 */
class DummyAccessor(
    private val list: MutableSet<String> = HashSet(),
    private val resources: MutableSet<InputStream> = HashSet(),
    private val classLoader: ClassLoader = DummyAccessor::class.java.classLoader
) : ResourceAccessor {

    companion object {
        val instance = DummyAccessor()
    }

    override fun list(
        relativeTo: String?,
        path: String?,
        includeFiles: Boolean,
        includeDirectories: Boolean,
        recursive: Boolean
    ): MutableSet<String> = list

    override fun getResourcesAsStream(path: String?): MutableSet<InputStream> = resources

    override fun toClassLoader(): ClassLoader = classLoader
}

/**
 * Extended factory that ignores parent absence silently.
 */
class TestLiquibaseIntegrationFactory : LiquibaseIntegrationFactory() {

    private val dummy = { _: Any?, _: Any?, _: Any?, _: Any? -> }

    private val extendedSingle = HashMap(super.single).apply {
        putAll(
            arrayOf(
                LkAddColumnConfig::class to AddColumnConfigIntegration<AddColumnChange>(dummy),
                LkCommonColumnConfig::class to CommonColumnConfigIntegration<CreateTableChange>(dummy),
                LkLoadColumnConfig::class to LoadColumnConfigIntegration<LoadDataChange>(dummy),
                LkPrecondition::class to PreconditionContainerIntegration<ChangesHolder>(dummy)
            )
        )
    }

    private val extendedWithParent: Map<Pair<KClass<*>, KClass<*>>, EvaluatableDslNode.Evaluator<*, *, LbArg>> = mapOf(
        LkAddColumnConfig::class to LkAddColumn::class to AddColumnConfigIntegration<AddColumnChange>(dummy),
        LkAddColumnConfig::class to LkCreateIndex::class to AddColumnConfigIntegration<CreateIndexChange>(dummy),
        LkAddColumnConfig::class to LkInsert::class to AddColumnConfigIntegration<InsertDataChange>(dummy),
        LkCommonColumnConfig::class to LkCreateTable::class to CommonColumnConfigIntegration<CreateTableChange>(dummy),
        LkCommonColumnConfig::class to LkUpdate::class to CommonColumnConfigIntegration<UpdateDataChange>(dummy),
        LkLoadColumnConfig::class to LkLoadData::class to LoadColumnConfigIntegration<LoadDataChange>(dummy),
        LkLoadColumnConfig::class to LkLoadUpdateData::class to LoadColumnConfigIntegration<LoadUpdateDataChange>(dummy),
        LkPrecondition::class to LkChangeSet::class to PreconditionContainerIntegration<ChangesHolder>(dummy),
        LkPrecondition::class to LkChangeLog::class to PreconditionContainerIntegration<DatabaseChangeLog>(dummy)
    )

    override val single: Map<KClass<*>, EvaluatableDslNode.Evaluator<*, *, LbArg>>
        get() = extendedSingle

    override val withParent: Map<Pair<KClass<*>, KClass<*>>, EvaluatableDslNode.Evaluator<*, *, LbArg>>
        get() = extendedWithParent
}