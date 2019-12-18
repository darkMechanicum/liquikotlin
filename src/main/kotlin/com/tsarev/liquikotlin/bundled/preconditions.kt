package com.tsarev.liquikotlin.bundled

import com.tsarev.liquikotlin.infrastructure.LbDslNode
import com.tsarev.liquikotlin.infrastructure.api.child
import com.tsarev.liquikotlin.infrastructure.api.nullable
import com.tsarev.liquikotlin.infrastructure.api.prop
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
    open val onFailMessage by nullable(String::class)
    open val onErrorMessage by nullable(String::class)
    open val onSqlOutput by nullable(String::class)
}

open class LkAndPrecondition : LkPreconditionLogic<LkAndPrecondition>(LkAndPrecondition::class)

open class LkOrPrecondition : LkPreconditionLogic<LkOrPrecondition>(LkOrPrecondition::class)

open class LkDbmsPrecondition : LbDslNode<LkDbmsPrecondition>(LkDbmsPrecondition::class) {
    @Primary
    open val type by nullable(String::class)
}

open class LkRunningAsPrecondition : LbDslNode<LkRunningAsPrecondition>(LkRunningAsPrecondition::class) {
    @Primary
    open val username by nullable(String::class)
}

open class LkChangeSetExecutedPrecondition :
    LbDslNode<LkChangeSetExecutedPrecondition>(LkChangeSetExecutedPrecondition::class) {
    @Primary
    open val id by nullable(String::class)
    @Primary
    open val author by nullable(String::class)
    open val changeLogFile by nullable(String::class)
}

open class LkColumnExistsPrecondition :
    PreconditionWithSchAndCat<LkColumnExistsPrecondition>(LkColumnExistsPrecondition::class) {
    @Primary
    open val tableName by nullable(String::class)
    @Primary
    open val columnName by nullable(String::class)
}

open class LkTableExistsPrecondition :
    PreconditionWithSchAndCat<LkTableExistsPrecondition>(LkTableExistsPrecondition::class) {
    @Primary
    open val tableName by nullable(String::class)
}

open class LkViewExistsPrecondition :
    PreconditionWithSchAndCat<LkViewExistsPrecondition>(LkViewExistsPrecondition::class) {
    @Primary
    open val viewName by nullable(String::class)
}

open class LkForeignKeyConstraintExistsPrecondition :
    PreconditionWithSchAndCat<LkForeignKeyConstraintExistsPrecondition>(LkForeignKeyConstraintExistsPrecondition::class) {
    @Primary
    open val foreignKeyName by nullable(String::class)
    open val foreignKeyTableName by nullable(String::class)
}

open class LkIndexExistsPrecondition :
    PreconditionWithSchAndCat<LkIndexExistsPrecondition>(LkIndexExistsPrecondition::class) {
    @Primary
    open val indexName by nullable(String::class)
    open val tableName by nullable(String::class)
    open val columnNames by nullable(String::class)
}

open class LkSequenceExistsPrecondition :
    PreconditionWithSchAndCat<LkSequenceExistsPrecondition>(LkSequenceExistsPrecondition::class) {
    @Primary
    open val sequenceName by nullable(String::class)
}

open class LkPrimaryKeyExistsPrecondition :
    PreconditionWithSchAndCat<LkPrimaryKeyExistsPrecondition>(LkPrimaryKeyExistsPrecondition::class) {
    @Primary
    open val primaryKeyName by prop(String::class)
    open val tableName by nullable(String::class)
}

open class LkSqlCheckPrecondition : LbDslNode<LkSqlCheckPrecondition>(LkSqlCheckPrecondition::class) {
    open val expectedResult by nullable(String::class)
    @Primary
    open val sql by nullable(String::class)
}

open class LkChangeLogPropertyDefinedPrecondition :
    LbDslNode<LkChangeLogPropertyDefinedPrecondition>(LkChangeLogPropertyDefinedPrecondition::class) {
    @Primary
    open val property by nullable(String::class)
    open val value by nullable(String::class)
}

open class LkCustomPrecondition : LbDslNode<LkCustomPrecondition>(LkCustomPrecondition::class) {
    @Primary
    open val className by nullable(String::class)
    open val param by child(::LkCustomPreconditionParam)
}

open class LkCustomPreconditionParam : LbDslNode<LkCustomPreconditionParam>(LkCustomPreconditionParam::class) {
    @Primary
    open val name by prop(String::class)
    open val value by nullable(Any::class)
}
