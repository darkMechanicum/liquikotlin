package com.tsarev.liquikotlin.bundled

import com.tsarev.liquikotlin.infrastructure.LbDslNode
import kotlin.reflect.KClass

// --- Abstract base classes ---

/**
 * Precondition base including bundled preconditions
 * and and/or grouping.
 */
abstract class LkPreconditionLogic<SelfT : LkPreconditionLogic<SelfT>>(
    thisClass: KClass<SelfT>
) : LbDslNode<SelfT>(thisClass) {
    open val and by child(::LkAndPrecondition)
    open val or by child(::LkOrPrecondition)
    open val dbms by child(::LkDbmsPrecondition)
    open val runningAs by child(::LkRunningAsPrecondition)
    open val changeSetExecuted by child(::LkChangeSetExecutedPrecondition)
    open val columnExists by child(::LkColumnExistsPrecondition)
    open val tableExists by child(::LkTableExistsPrecondition)
    open val viewExists by child(::LkViewExistsPrecondition)
    open val foreignKeyConstraintExists: LkForeignKeyConstraintExistsPrecondition by child(
        ::LkForeignKeyConstraintExistsPrecondition
    )
    open val indexExists by child(::LkIndexExistsPrecondition)
    open val sequenceExists by child(::LkSequenceExistsPrecondition)
    open val primaryKeyExists by child(::LkPrimaryKeyExistsPrecondition)
    open val sqlCheck by child(::LkSqlCheckPrecondition)
    open val changeLogPropertyDefined: LkChangeLogPropertyDefinedPrecondition by child(
        ::LkChangeLogPropertyDefinedPrecondition
    )
    open val customPrecondition by child(::LkCustomPrecondition)
}

/**
 * Precondition with catalog name and schema name since
 * they apperar together very often.
 */
abstract class PreconditionWithSchAndCat<SelfT : PreconditionWithSchAndCat<SelfT>>(selfClass: KClass<SelfT>) :
    LbDslNode<SelfT>(selfClass) {
    open val schemaName by nullable(String::class)
    open val catalogName by nullable(String::class)
}

// --- Bundled preconditions ---

open class LkPrecondition : LkPreconditionLogic<LkPrecondition>(LkPrecondition::class) {
    open val onFail by nullable(String::class)
    open val onError by nullable(String::class)
    open val onUpdateSQL by nullable(String::class)
    open val onFailMessage by nullable(String::class)
    open val onErrorMessage by nullable(String::class)
}

open class LkAndPrecondition : LkPreconditionLogic<LkAndPrecondition>(LkAndPrecondition::class)

open class LkOrPrecondition : LkPreconditionLogic<LkOrPrecondition>(LkOrPrecondition::class)

open class LkDbmsPrecondition : LbDslNode<LkDbmsPrecondition>(LkDbmsPrecondition::class) {
    open val type by nullable(String::class)
}

open class LkRunningAsPrecondition : LbDslNode<LkRunningAsPrecondition>(LkRunningAsPrecondition::class) {
    open val username by nullable(String::class)
}

open class LkChangeSetExecutedPrecondition :
    LbDslNode<LkChangeSetExecutedPrecondition>(LkChangeSetExecutedPrecondition::class) {
    open val id by nullable(String::class)
    open val author by nullable(String::class)
    open val changeLogFile by nullable(String::class)
}

open class LkColumnExistsPrecondition :
    PreconditionWithSchAndCat<LkColumnExistsPrecondition>(LkColumnExistsPrecondition::class) {
    open val tableName by nullable(String::class)
    open val columnName by nullable(String::class)
}

open class LkTableExistsPrecondition :
    PreconditionWithSchAndCat<LkTableExistsPrecondition>(LkTableExistsPrecondition::class) {
    open val tableName by nullable(String::class)
}

open class LkViewExistsPrecondition :
    PreconditionWithSchAndCat<LkViewExistsPrecondition>(LkViewExistsPrecondition::class) {
    open val viewName by nullable(String::class)
}

open class LkForeignKeyConstraintExistsPrecondition :
    PreconditionWithSchAndCat<LkForeignKeyConstraintExistsPrecondition>(LkForeignKeyConstraintExistsPrecondition::class) {
    open val foreignKeyName by nullable(String::class)
    open val foreignKeyTableName by nullable(String::class)
}

open class LkIndexExistsPrecondition :
    PreconditionWithSchAndCat<LkIndexExistsPrecondition>(LkIndexExistsPrecondition::class) {
    open val indexName by nullable(String::class)
    open val tableName by nullable(String::class)
}

open class LkSequenceExistsPrecondition :
    PreconditionWithSchAndCat<LkSequenceExistsPrecondition>(LkSequenceExistsPrecondition::class) {
    open val sequenceName by nullable(String::class)
}

open class LkPrimaryKeyExistsPrecondition :
    PreconditionWithSchAndCat<LkPrimaryKeyExistsPrecondition>(LkPrimaryKeyExistsPrecondition::class) {
    open val primaryKeyName by nonNullable(String::class)
    open val tableName by nullable(String::class)
}

open class LkSqlCheckPrecondition : LbDslNode<LkSqlCheckPrecondition>(LkSqlCheckPrecondition::class) {
    open val expectedResult by nullable(String::class)
    open val sql by nullable(String::class)
}

open class LkChangeLogPropertyDefinedPrecondition :
    LbDslNode<LkChangeLogPropertyDefinedPrecondition>(LkChangeLogPropertyDefinedPrecondition::class) {
    open val property by nullable(String::class)
    open val value by nullable(String::class)
}

open class LkCustomPrecondition : LbDslNode<LkCustomPrecondition>(LkCustomPrecondition::class) {
    open val className by nullable(String::class)
    open val param by child(::LkCustomPreconditionParam)
}

open class LkCustomPreconditionParam : LbDslNode<LkCustomPreconditionParam>(LkCustomPreconditionParam::class) {
    open val name by nullable(String::class)
    open val value by nullable(Any::class)
}
