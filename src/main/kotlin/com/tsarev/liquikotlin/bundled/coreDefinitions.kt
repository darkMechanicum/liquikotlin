package com.tsarev.liquikotlin.bundled

import com.tsarev.liquikotlin.infrastructure.LbDslNode
import com.tsarev.liquikotlin.infrastructure.default.builtChild
import com.tsarev.liquikotlin.infrastructure.default.child
import com.tsarev.liquikotlin.infrastructure.default.nullable
import com.tsarev.liquikotlin.infrastructure.default.prop
import kotlin.reflect.KClass

// --- Abstract base and utility classes ---

/**
 * Class container of all bundled refactorings.
 */
abstract class LkRefactorings<SelfT : LkRefactorings<SelfT>>(thisClass: KClass<SelfT>) : LbDslNode<SelfT>(thisClass) {

    // Creating changes
    open val addAutoIncrement by child(::LkAddAutoIncrement)
    open val addColumn by child(::LkAddColumn)
    open val addDefaultValue by child(::LkAddDefaultValue)
    open val addForeignKeyConstraint by child(::LkAddForeignKeyConstraint)
    open val addLookupTable by child(::LkAddLookupTable)
    open val addNotNullConstraint by child(::LkAddNotNullConstraint)
    open val addPrimaryKey by child(::LkAddPrimaryKey)
    open val addUniqueConstraint by child(::LkAddUniqueConstraint)
    open val createIndex by child(::LkCreateIndex)
    open val createProcedure by child(::LkCreateProcedure)
    open val createSequence by child(::LkCreateSequence)
    open val createTable by child(::LkCreateTable)
    open val createView by child(::LkCreateView)

    // Deleting changes
    open val delete by child(::LkDelete)
    open val dropAllForeignKeyConstraints by child(::LkDropAllForeignKeyConstraints)
    open val dropColumn by child(::LkDropColumn)
    open val dropDefaultValue by child(::LkDropDefaultValue)
    open val dropForeignKeyConstraint by child(::LkDropForeignKeyConstraint)
    open val dropIndex by child(::LkDropIndex)
    open val dropNotNullConstraint by child(::LkDropNotNullConstraint)
    open val dropPrimaryKey by child(::LkDropPrimaryKey)
    open val dropProcedure by child(::LkDropProcedure)
    open val dropSequence by child(::LkDropSequence)
    open val dropTable by child(::LkDropTable)
    open val dropUniqueConstraint by child(::LkDropUniqueConstraint)
    open val dropView by child(::LkDropView)

    // Other changes
    open val alterSequence by child(::LkAlterSequence)
    open val empty by child(::LkEmpty)
    open val executeCommand by child(::LkExecuteCommand)
    open val insert by child(::LkInsert)
    open val loadData by child(::LkLoadData)
    open val loadUpdateData by child(::LkLoadUpdateData)
    open val mergeColumns by child(::LkMergeColumns)
    open val modifyDataType by child(::LkModifyDataType)
    open val renameColumn by child(::LkRenameColumn)
    open val renameTable by child(::LkRenameTable)
    open val renameView by child(::LkRenameView)
    open val sql by child(::LkSql)
    open val sqlFile by child(::LkSqlFile)
    open val stop by child(::LkStop)
    open val tagDatabase by child(::LkTagDatabase)
    open val update by child(::LkUpdate)
}

// --- Core definition classes ---

/**
 * Entry point for script.
 */
var changelog = LkChangeLog()

/**
 * Changelog. Entry point for rest nodes.
 */
@LKDsl
open class LkChangeLog : LbDslNode<LkChangeLog>(LkChangeLog::class) {
    @Primary
    open val logicalFilePath by nullable(String::class)

    open val precondition by child(::LkPrecondition)
    open val property by child(::LkProperty)
    open val changeset by child(::LkChangeSet)
    open val include by child(::LkInclude)
    open val includeAll by child(::LkIncludeAll)
}

@LKDsl
open class LkInclude : LbDslNode<LkInclude>(LkInclude::class) {
    @Primary
    open val path by prop(String::class)
    open val relativeToChangelogFile by prop(Boolean::class, false)
    open val context by nullable(String::class)
    open val labels by nullable(String::class)
    open val ignore by prop(Boolean::class, false)
}

@LKDsl
open class LkIncludeAll : LbDslNode<LkIncludeAll>(LkIncludeAll::class) {
    @Primary
    open val path by prop(String::class)
    open val resourceFilter by nullable(String::class)
    open val relativeToChangelogFile by prop(Boolean::class, false)
    open val errorIfMissingOrEmpty by prop(Boolean::class, true)
    open val context by nullable(String::class)
    open val labels by nullable(String::class)
    open val ignore by prop(Boolean::class, false)
}

/**
 * Changelog property.
 */
@LKDsl
open class LkProperty : LbDslNode<LkProperty>(LkProperty::class) {
    @Primary
    open val name by prop(String::class)
    open val value by prop(String::class)
    open val context by nullable(String::class)
    open val dbms by nullable(String::class)
}

@LKDsl
open class LkChangeSet : LkRefactorings<LkChangeSet>(LkChangeSet::class) {
    @Primary
    open val id by prop(Any::class)
    @Primary
    open val author by nullable(String::class)
    open val dbms by nullable(String::class)
    open val runAlways by nullable(Boolean::class, true) // TODO What is correct default?
    open val runOnChange by nullable(Boolean::class, true) // TODO What is correct default?
    open val context by nullable(String::class)
    open val runInTransaction by nullable(Boolean::class)
    open val failOnError by nullable(Boolean::class)

    open val rollback by child(::LkRollback)
    open val comment by child(::LkComment)
    open val validCheckSum by child(::LkValidCheckSum)
    open val precondition by child(::LkPrecondition)
}

@LKDsl
open class LkRollback : LkRefactorings<LkRollback>(LkRollback::class)

@LKDsl
open class LkValidCheckSum : LbDslNode<LkValidCheckSum>(LkValidCheckSum::class) {
    @Primary
    open val checkSum by prop(String::class)
}

@LKDsl
open class LkComment : LbDslNode<LkComment>(LkComment::class) {
    @Primary
    open val text by prop(String::class)
}