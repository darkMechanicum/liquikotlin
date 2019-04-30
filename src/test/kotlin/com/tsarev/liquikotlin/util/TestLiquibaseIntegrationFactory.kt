package com.tsarev.liquikotlin.util

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.EvaluatableDslNode
import com.tsarev.liquikotlin.infrastructure.LbArg
import com.tsarev.liquikotlin.integration.*
import liquibase.change.core.*
import liquibase.changelog.DatabaseChangeLog
import kotlin.reflect.KClass

/**
 * Extended factory that ignores parent absence silently.
 */
class TestLiquibaseIntegrationFactory : LiquibaseIntegrationFactory() {

    private val DUMMY = { _: Any?, _: Any?, _: Any?, _: Any? -> }

    private val extendedSingle = HashMap(super.single).apply {
        putAll(
            arrayOf(
                LkAddColumnConfig::class to AddColumnConfigIntegration<AddColumnChange>(DUMMY),
                LkCommonColumnConfig::class to CommonColumnConfigIntegration<CreateTableChange>(DUMMY),
                LkLoadColumnConfig::class to LoadColumnConfigIntegration<LoadDataChange>(DUMMY),
                LkPrecondition::class to PreconditionContainerIntegration<ChangesHolder>(DUMMY)
            )
        )
    }

    private val extendedWithParent: Map<Pair<KClass<*>, KClass<*>>, EvaluatableDslNode.Evaluator<*, *, LbArg>> = mapOf(
        LkAddColumnConfig::class to LkAddColumn::class to AddColumnConfigIntegration<AddColumnChange>(DUMMY),
        LkAddColumnConfig::class to LkCreateIndex::class to AddColumnConfigIntegration<CreateIndexChange>(DUMMY),
        LkAddColumnConfig::class to LkInsert::class to AddColumnConfigIntegration<InsertDataChange>(DUMMY),
        LkCommonColumnConfig::class to LkCreateTable::class to CommonColumnConfigIntegration<CreateTableChange>(DUMMY),
        LkCommonColumnConfig::class to LkUpdate::class to CommonColumnConfigIntegration<UpdateDataChange>(DUMMY),
        LkLoadColumnConfig::class to LkLoadData::class to LoadColumnConfigIntegration<LoadDataChange>(DUMMY),
        LkLoadColumnConfig::class to LkLoadUpdateData::class to LoadColumnConfigIntegration<LoadUpdateDataChange>(DUMMY),
        LkPrecondition::class to LkChangeSet::class to PreconditionContainerIntegration<ChangesHolder>(DUMMY),
        LkPrecondition::class to LkChangeLog::class to PreconditionContainerIntegration<DatabaseChangeLog>(DUMMY)
    )

    override val single: Map<KClass<*>, EvaluatableDslNode.Evaluator<*, *, LbArg>>
        get() = extendedSingle

    override val withParent: Map<Pair<KClass<*>, KClass<*>>, EvaluatableDslNode.Evaluator<*, *, LbArg>>
        get() = extendedWithParent
}