package com.tsarev.liquikotlin.bundled

import com.tsarev.liquikotlin.infrastructure.LbArg
import com.tsarev.liquikotlin.infrastructure.LbDslNode
import liquibase.change.Change
import liquibase.changelog.ChangeSet
import liquibase.changelog.DatabaseChangeLog
import liquibase.changelog.IncludeAllFilter
import liquibase.exception.SetupException
import liquibase.precondition.core.PreconditionContainer
import liquibase.resource.ResourceAccessor
import java.util.Comparator
import kotlin.reflect.KClass

// --- Abstract base and utility classes ---

val comparator = Comparator<String> { o1, o2 -> o1.compareTo(o2) }

val defaultFilter = IncludeAllFilter { it?.endsWith(".kts") ?: false }

/**
 * Utility class to hide change add differency
 * between [ChangeSet] and [liquibase.changelog.RollbackContainer] present in [ChangeSet].
 */
open class LkChangesHolder {
    open val changes: MutableCollection<Change> = ArrayList()
    open var preconditions: PreconditionContainer? = null
}

/**
 * Abstract change that add itself to changes list.
 */
abstract class
LkChange<SelfT : LkChange<SelfT, LinkedT>, LinkedT : Change>(
    thisClass: KClass<SelfT>,
    linkedConstructor: () -> LinkedT
) : LbDslNode<SelfT, LinkedT, LkChangesHolder>(
    thisClass,
    linkedConstructor,
    { holder, it, _, _ ->
        holder.changes.add(it)
    }
)

/**
 * Class container of all bundled refactorings.
 */
abstract class LkRefactorings<SelfT : LkRefactorings<SelfT, LinkedT, ParentLinkedT>, LinkedT : Any, ParentLinkedT>(
    thisClass: KClass<SelfT>,
    linkedConstructor: () -> LinkedT,
    linkedSetter: ((ParentLinkedT, LinkedT, SelfT) -> Unit)
) : LbDslNode<SelfT, LinkedT, ParentLinkedT>(
    thisClass,
    linkedConstructor,
    { parent, linked, self, _ -> linkedSetter(parent, linked, self) }
) {

    // Creating changes
    open val addAutoIncrement by child(::LkAddAutoIncrement)
    open val addColumn by child(::LkAddColumn)
    open val constraints by child(::LkConstraints)
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
val changelog = LkChangeLog()

/**
 * Changelog. Entry point for rest nodes.
 */
open class LkChangeLog : LbDslNode<LkChangeLog, DatabaseChangeLog, Any>(
    LkChangeLog::class,
    ::DatabaseChangeLog
) {
    open val logicalFilePath by nullable(String::class)

    open val precondition by child(::LkChangeLogPrecondition)
    open val property by child(::LkProperty)
    open val changeset by child(::LkChangeSet)
    open val include by child(::LkInclude)
    open val includeAll by child(::LkIncludeAll)

    /**
     * Intercept constructor evaluation to set file path.
     */
    override fun createEvalResult(argument: LbArg?): DatabaseChangeLog {
        val (physicalPath, _) = argument!!
        val result = DatabaseChangeLog(physicalPath)
        val resultPath = self.logicalFilePath.current ?: physicalPath
        result.logicalFilePath = resultPath
        return result
    }
}

open class LkInclude : LbDslNode<LkInclude, Any, DatabaseChangeLog>(
    LkInclude::class,
    ::Any,
    { changeLog, _, self, arg ->
        val (_, resourceAccessor) = (arg as Pair<*, ResourceAccessor>)
        changeLog.include(self.path.current, self.relativeToChangelogFile.current, resourceAccessor)
    }
) {
    open val path by nonNullable(String::class)
    open val relativeToChangelogFile by nonNullable(Boolean::class, false)
}

open class LkIncludeAll : LbDslNode<LkIncludeAll, Any, DatabaseChangeLog>(
    LkIncludeAll::class,
    ::Any,
    { changeLog, _, self, arg ->
        val (_, resourceAccessor) = arg!!
        val resourceFilterDef = self.resourceFilter.current
        var resourceFilter: IncludeAllFilter = defaultFilter
        if (!resourceFilterDef.isNullOrBlank()) {
            try {
                resourceFilter = Class.forName(resourceFilterDef).newInstance() as IncludeAllFilter
            } catch (e: Exception) {
                throw SetupException(e)
            }

        }
        changeLog.includeAll(
            self.path.current,
            self.relativeToChangelogFile.current,
            resourceFilter,
            self.errorIfMissingOrEmpty.current,
            comparator,
            resourceAccessor
        )
    }
) {
    open val path by nonNullable(String::class)
    open val resourceFilter by nullable(String::class)
    open val relativeToChangelogFile by nonNullable(Boolean::class, false)
    open val errorIfMissingOrEmpty by nonNullable(Boolean::class, true)
}

/**
 * Changelog property.
 */
open class LkProperty : LbDslNode<LkProperty, Any, DatabaseChangeLog>(
    LkProperty::class,
    ::Any,
    { changeLog, _, self, _ ->
        changeLog.changeLogParameters.set(
            self.name.current,
            self.value.current,
            self.context.current,
            null,
            self.dbms.current,
            true,
            changeLog
        )
    }
) {
    open val name by nonNullable(String::class)
    open val value by nonNullable(String::class)
    open val context by nullable(String::class)
    open val dbms by nullable(String::class)
}

open class LkChangeSet : LkRefactorings<LkChangeSet, LkChangesHolder, DatabaseChangeLog>(
    LkChangeSet::class,
    ::LkChangesHolder,
    { changeLog, it, self ->
        val result = ChangeSet(
            self.id.current.toString(),
            self.author.current,
            self.runAlways.current ?: false,
            self.runOnChange.current ?: true,
            null, //TODO
            self.context.current,
            self.dbms.current,
            changeLog
        )
        it.changes.forEach { result.addChange(it) }
        result.preconditions = it.preconditions
        changeLog.addChangeSet(result)
    }
) {
    open val id by nonNullable(Any::class)
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
    open val precondition by child(::LkChangeSetPrecondition)
}

open class LkRollback : LkRefactorings<LkRollback, LkChangesHolder, ChangeSet>(
    LkRollback::class,
    ::LkChangesHolder,
    { changeSet, it, _ ->
        changeSet.rollback.changes.addAll(it.changes)
    }
)

open class LkValidCheckSum : LbDslNode<LkValidCheckSum, Any, ChangeSet>(
    LkValidCheckSum::class,
    ::Any,
    { changeSet, _, self, _ -> changeSet.addValidCheckSum(self.checkSum.current) }
) {
    open val checkSum by nonNullable(String::class)
}

open class LkComment : LbDslNode<LkComment, Any, ChangeSet>(
    LkComment::class,
    ::Any,
    { changeSet, _, self, _ -> changeSet.comments += self.text.current }
) {
    open val text by nonNullable(String::class)
}