package com.tsarev.liquikotlin.bundled

import com.tsarev.liquikotlin.infrastructure.LbDslNode
import liquibase.changelog.DatabaseChangeLog
import liquibase.precondition.CustomPreconditionWrapper
import liquibase.precondition.Precondition
import liquibase.precondition.PreconditionLogic
import liquibase.precondition.core.*
import kotlin.reflect.KClass

// --- Abstract base classes ---

/**
 * Precondition base including bundled preconditions
 * and and/or grouping.
 */
abstract class
LkPreconditionLogic<SelfT : LkPreconditionLogic<SelfT, LinkedT, ParentLinkedT>,
        LinkedT : Any,
        ParentLinkedT>(
    thisClass: KClass<SelfT>,
    linkedConstructor: () -> LinkedT,
    linkedSetter: ((ParentLinkedT, LinkedT, SelfT) -> Unit)?
) : LbDslNode<SelfT, LinkedT, ParentLinkedT>(
    thisClass,
    linkedConstructor,
    { parent, linked, self, _ -> linkedSetter?.invoke(parent, linked, self) }
) {
    val and by child(::LkAndPrecondition)
    val or by child(::LkOrPrecondition)
    val dbms by child(::LkDbmsPrecondition)
    val runningAs by child(::LkRunningAsPrecondition)
    val changeSetExecuted by child(::LkChangeSetExecutedPrecondition)
    val columnExists by child(::LkColumnExistsPrecondition)
    val tableExists by child(::LkTableExistsPrecondition)
    val viewExists by child(::LkViewExistsPrecondition)
    val foreignKeyConstraintExists: LkForeignKeyConstraintExistsPrecondition by child(
        ::LkForeignKeyConstraintExistsPrecondition
    )
    val indexExists by child(::LkIndexExistsPrecondition)
    val sequenceExists by child(::LkSequenceExistsPrecondition)
    val primaryKeyExists by child(::LkPrimaryKeyExistsPrecondition)
    val sqlCheck by child(::LkSqlCheckPrecondition)
    val changeLogPropertyDefined: LkChangeLogPropertyDefinedPrecondition by child(
        ::LkChangeLogPropertyDefinedPrecondition
    )
    val customPrecondition by child(::LkCustomPrecondition)
}

/**
 * Nested precondition that sets itself into preconfition container.
 */
abstract class AbstractNestedPrecondition<SelfT : AbstractNestedPrecondition<SelfT, LinkedT>, LinkedT : Precondition>(
    thisClass: KClass<SelfT>,
    linkedConstructor: () -> LinkedT
) : LbDslNode<SelfT, LinkedT, PreconditionLogic>(
    thisClass,
    linkedConstructor,
    { logic, it, _, _ -> logic.nestedPreconditions.add(it) }
)

/**
 * Base class for []LkChangeSetPrecondition] and []LkChangeLogPrecondition]
 * since they have to set themselves into different parent nodes.
 */
open class LkBasePrecondition<SelfT : LkBasePrecondition<SelfT, ParentT>, ParentT>(
    thisClass: KClass<SelfT>,
    linkedSetter: ((ParentT, PreconditionContainer, SelfT) -> Unit)?
) : LkPreconditionLogic<SelfT, PreconditionContainer, ParentT>(
    thisClass,
    ::PreconditionContainer,
    linkedSetter
) {
    val onFail by nullableWS(String::class, PreconditionContainer::setOnFail, "HALT")
    val onError by nullableWS(String::class, PreconditionContainer::setOnError, "HALT")
    val onUpdateSQL by nullableWS(String::class, PreconditionContainer::setOnSqlOutput)
    val onFailMessage by nullableWS(String::class, PreconditionContainer::setOnFailMessage)
    val onErrorMessage by nullableWS(String::class, PreconditionContainer::setOnErrorMessage)
}

/**
 * Precondition with catalog name and schema name since
 * they apperar together very often.
 */
abstract class PreconditionWithSchAndCat
<SelfT : PreconditionWithSchAndCat<SelfT, LinkedT>, LinkedT : Precondition>(
    selfClass: KClass<SelfT>,
    linkedConstructor: () -> LinkedT,
    schemaSetter: (LinkedT, String) -> Unit,
    catalogSetter: (LinkedT, String) -> Unit
) : AbstractNestedPrecondition<SelfT, LinkedT>(selfClass, linkedConstructor) {
    val schemaName by nullableWS(String::class, schemaSetter)
    val catalogName by nullableWS(String::class, catalogSetter)
}

// --- Bundled preconditions ---

open class LkChangeLogPrecondition :
    LkBasePrecondition<LkChangeLogPrecondition, DatabaseChangeLog>(
        LkChangeLogPrecondition::class,
        { changeLog, it, _ -> changeLog.preconditions = it }
    )

open class LkChangeSetPrecondition :
    LkBasePrecondition<LkChangeSetPrecondition, LkChangesHolder>(
        LkChangeSetPrecondition::class,
        { holder, it, _ -> holder.preconditions = it }
    )

open class LkAndPrecondition : LkPreconditionLogic<LkAndPrecondition, AndPrecondition, PreconditionLogic>(
    LkAndPrecondition::class, ::AndPrecondition, { logic, it, _ -> logic.nestedPreconditions.add(it) }
)

open class LkOrPrecondition : LkPreconditionLogic<LkOrPrecondition, OrPrecondition, PreconditionLogic>(
    LkOrPrecondition::class, ::OrPrecondition, { logic, it, _ -> logic.nestedPreconditions.add(it) }
)

open class LkDbmsPrecondition : AbstractNestedPrecondition<LkDbmsPrecondition, DBMSPrecondition>(
    LkDbmsPrecondition::class, ::DBMSPrecondition
) {
    val type by nullableWS(String::class, DBMSPrecondition::setType)

    operator fun invoke(type: String) = type(type)
}

open class LkRunningAsPrecondition : AbstractNestedPrecondition<LkRunningAsPrecondition, RunningAsPrecondition>(
    LkRunningAsPrecondition::class, ::RunningAsPrecondition
) {
    val username by nullableWS(String::class, RunningAsPrecondition::setUsername)

    operator fun invoke(username: String) = username(username)
}

open class LkChangeSetExecutedPrecondition :
    AbstractNestedPrecondition<LkChangeSetExecutedPrecondition, ChangeSetExecutedPrecondition>(
        LkChangeSetExecutedPrecondition::class, ::ChangeSetExecutedPrecondition
    ) {
    val id by nullableWS(String::class, ChangeSetExecutedPrecondition::setId)
    val author by nullableWS(String::class, ChangeSetExecutedPrecondition::setAuthor)
    val changeLogFile by nullableWS(String::class, ChangeSetExecutedPrecondition::setChangeLogFile)

    operator fun invoke(id: String) = id(id)
}

open class LkColumnExistsPrecondition :
    PreconditionWithSchAndCat<LkColumnExistsPrecondition, ColumnExistsPrecondition>(
        LkColumnExistsPrecondition::class,
        ::ColumnExistsPrecondition,
        ColumnExistsPrecondition::setSchemaName,
        ColumnExistsPrecondition::setCatalogName
    ) {
    val tableName by nullableWS(String::class, ColumnExistsPrecondition::setTableName)
    val columnName by nullableWS(String::class, ColumnExistsPrecondition::setColumnName)

    operator fun invoke(tableName: String, columnName: String) =
        tableName(tableName).columnName(columnName)
}

open class LkTableExistsPrecondition :
    PreconditionWithSchAndCat<LkTableExistsPrecondition, TableExistsPrecondition>(
        LkTableExistsPrecondition::class,
        ::TableExistsPrecondition,
        TableExistsPrecondition::setSchemaName,
        TableExistsPrecondition::setCatalogName
    ) {
    val tableName by nullableWS(String::class, TableExistsPrecondition::setTableName)

    operator fun invoke(tableName: String) = tableName(tableName)
}

open class LkViewExistsPrecondition :
    PreconditionWithSchAndCat<LkViewExistsPrecondition, ViewExistsPrecondition>(
        LkViewExistsPrecondition::class,
        ::ViewExistsPrecondition,
        ViewExistsPrecondition::setSchemaName,
        ViewExistsPrecondition::setCatalogName
    ) {
    val viewName by nullableWS(String::class, ViewExistsPrecondition::setViewName)

    operator fun invoke(viewName: String) = viewName(viewName)
}

open class LkForeignKeyConstraintExistsPrecondition :
    PreconditionWithSchAndCat<LkForeignKeyConstraintExistsPrecondition, ForeignKeyExistsPrecondition>(
        LkForeignKeyConstraintExistsPrecondition::class,
        ::ForeignKeyExistsPrecondition,
        ForeignKeyExistsPrecondition::setSchemaName,
        ForeignKeyExistsPrecondition::setCatalogName
    ) {
    val foreignKeyName by nullableWS(String::class, ForeignKeyExistsPrecondition::setForeignKeyName)
    val foreignKeyTableName by nullableWS(String::class, ForeignKeyExistsPrecondition::setForeignKeyTableName)

    operator fun invoke(foreignKeyTableName: String, foreignKeyName: String) =
        foreignKeyTableName(foreignKeyTableName).foreignKeyName(foreignKeyName)
}

open class LkIndexExistsPrecondition :
    PreconditionWithSchAndCat<LkIndexExistsPrecondition, IndexExistsPrecondition>(
        LkIndexExistsPrecondition::class,
        ::IndexExistsPrecondition,
        IndexExistsPrecondition::setSchemaName,
        IndexExistsPrecondition::setCatalogName
    ) {
    val indexName by nullableWS(String::class, IndexExistsPrecondition::setIndexName)
    val tableName by nullableWS(String::class, IndexExistsPrecondition::setTableName)

    operator fun invoke(tableName: String, indexName: String) = tableName(tableName).indexName(indexName)
}

open class LkSequenceExistsPrecondition :
    PreconditionWithSchAndCat<LkSequenceExistsPrecondition, SequenceExistsPrecondition>(
        LkSequenceExistsPrecondition::class,
        ::SequenceExistsPrecondition,
        SequenceExistsPrecondition::setSchemaName,
        SequenceExistsPrecondition::setCatalogName
    ) {
    val sequenceName by nullableWS(String::class, SequenceExistsPrecondition::setSequenceName)

    operator fun invoke(sequenceName: String) = sequenceName(sequenceName)
}

open class LkPrimaryKeyExistsPrecondition :
    PreconditionWithSchAndCat<LkPrimaryKeyExistsPrecondition, PrimaryKeyExistsPrecondition>(
        LkPrimaryKeyExistsPrecondition::class,
        ::PrimaryKeyExistsPrecondition,
        PrimaryKeyExistsPrecondition::setSchemaName,
        PrimaryKeyExistsPrecondition::setCatalogName
    ) {
    val primaryKeyName by nonNullableWS(String::class, PrimaryKeyExistsPrecondition::setPrimaryKeyName)
    val tableName by nullableWS(String::class, PrimaryKeyExistsPrecondition::setTableName)

    operator fun invoke(tableName: String, primaryKeyName: String) =
        tableName(tableName).primaryKeyName(primaryKeyName)
}

open class LkSqlCheckPrecondition : AbstractNestedPrecondition<LkSqlCheckPrecondition, SqlPrecondition>(
    LkSqlCheckPrecondition::class, ::SqlPrecondition
) {
    val expectedResult by nullableWS(String::class, SqlPrecondition::setExpectedResult)
    val sql by nullableWS(String::class, SqlPrecondition::setSql)

    operator fun invoke(expectedResult: String) = expectedResult(expectedResult)
    operator fun minus(sql: String) {
        sql(sql)
    }
}

open class LkChangeLogPropertyDefinedPrecondition :
    AbstractNestedPrecondition<LkChangeLogPropertyDefinedPrecondition, ChangeLogPropertyDefinedPrecondition>(
        LkChangeLogPropertyDefinedPrecondition::class, ::ChangeLogPropertyDefinedPrecondition
    ) {
    val property by nullableWS(String::class, ChangeLogPropertyDefinedPrecondition::setProperty)
    val value by nullableWS(String::class, ChangeLogPropertyDefinedPrecondition::setValue)

    operator fun invoke(property: String) = property(property)
    operator fun invoke(property: String, value: String) = property(property).value(value)
}

open class LkCustomPrecondition : AbstractNestedPrecondition<LkCustomPrecondition, CustomPreconditionWrapper>(
    LkCustomPrecondition::class,
    ::CustomPreconditionWrapper
) {
    val className by nullableWS(String::class, CustomPreconditionWrapper::setClassName)
    val param by child(::LkCustomPreconditionParam)

    operator fun invoke(className: String, vararg params: Pair<String, String>) =
        className(className).apply {
            params.forEach { (name, value) -> this.param.name(name).value(value) }
        }
}

open class LkCustomPreconditionParam : LbDslNode<LkCustomPreconditionParam, Any, CustomPreconditionWrapper>(
    LkCustomPreconditionParam::class,
    ::Any,
    { wrapper, _, self, _ -> wrapper.setParam(self.name.current, self.value.current?.toString()) }
) {
    val name by nullable(String::class)
    val value by nullable(Any::class)
}
