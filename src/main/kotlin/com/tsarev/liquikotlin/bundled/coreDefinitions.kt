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
class LkChangesHolder {
    val changes: MutableCollection<Change> = ArrayList()
    var preconditions: PreconditionContainer? = null
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
abstract class Refactorings<SelfT : Refactorings<SelfT, LinkedT, ParentLinkedT>, LinkedT : Any, ParentLinkedT>(
    thisClass: KClass<SelfT>,
    linkedConstructor: () -> LinkedT,
    linkedSetter: ((ParentLinkedT, LinkedT, SelfT) -> Unit)
) : LbDslNode<SelfT, LinkedT, ParentLinkedT>(
    thisClass,
    linkedConstructor,
    { parent, linked, self, _ -> linkedSetter(parent, linked, self) }
) {

    // Creating changes
    val addAutoIncrement by child(::LkAddAutoIncrement)
    val addColumn by child(::LkAddColumn)
    val constraints by child(::LkConstraints)
    val addDefaultValue by child(::LkAddDefaultValue)
    val addForeignKeyConstraint by child(::LkAddForeignKeyConstraint)
    val addLookupTable by child(::LkAddLookupTable)
    val addNotNullConstraint by child(::LkAddNotNullConstraint)
    val addPrimaryKey by child(::LkAddPrimaryKey)
    val addUniqueConstraint by child(::LkAddUniqueConstraint)
    val createIndex by child(::LkCreateIndex)
    val createProcedure by child(::LkCreateProcedure)
    val createSequence by child(::LkCreateSequence)
    val createTable by child(::LkCreateTable)
    val createView by child(::LkCreateView)

    // Deleting changes
    val delete by child(::LkDelete)
    val dropAllForeignKeyConstraints by child(::LkDropAllForeignKeyConstraints)
    val dropColumn by child(::LkDropColumn)
    val dropDefaultValue by child(::LkDropDefaultValue)
    val dropForeignKeyConstraint by child(::LkDropForeignKeyConstraint)
    val dropIndex by child(::LkDropIndex)
    val dropNotNullConstraint by child(::LkDropNotNullConstraint)
    val dropPrimaryKey by child(::LkDropPrimaryKey)
    val dropProcedure by child(::LkDropProcedure)
    val dropSequence by child(::LkDropSequence)
    val dropTable by child(::LkDropTable)
    val dropUniqueConstraint by child(::LkDropUniqueConstraint)
    val dropView by child(::LkDropView)

    // Other changes
    val alterSequence by child(::LkAlterSequence)
    val empty by child(::LkEmpty)
    val executeCommand by child(::LkExecuteCommand)
    val insert by child(::LkInsert)
    val loadData by child(::LkLoadData)
    val loadUpdateData by child(::LkLoadUpdateData)
    val mergeColumns by child(::LkMergeColumns)
    val modifyDataType by child(::LkModifyDataType)
    val renameColumn by child(::LkRenameColumn)
    val renameTable by child(::LkRenameTable)
    val renameView by child(::LkRenameView)
    val sql by child(::LkSql)
    val sqlFile by child(::LkSqlFile)
    val stop by child(::LkStop)
    val tagDatabase by child(::LkTagDatabase)
    val update by child(::LkUpdate)
}

// --- Core definition classes ---

/**
 * Entry point for script.
 */
val changelog = ChangeLog()

/**
 * Changelog. Entry point for rest nodes.
 */
open class ChangeLog : LbDslNode<ChangeLog, DatabaseChangeLog, Any>(
    ChangeLog::class,
    ::DatabaseChangeLog
) {
    val logicalFilePath by nullable(String::class)

    val precondition by child(::LkChangeLogPrecondition)
    val property by child(::LkProperty)
    val changeset by child(::LkChangeSet)
    val include by child(::Include)
    val includeAll by child(::IncludeAll)

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

open class Include : LbDslNode<Include, Any, DatabaseChangeLog>(
    Include::class,
    ::Any,
    { changeLog, _, self, arg ->
        val (_, resourceAccessor) = (arg as Pair<*, ResourceAccessor>)
        changeLog.include(self.path.current, self.relativeToChangelogFile.current, resourceAccessor)
    }
) {
    val path by nonNullable(String::class)
    val relativeToChangelogFile by nonNullable(Boolean::class, false)

    operator fun invoke(path: String) = path(path)
}

open class IncludeAll : LbDslNode<IncludeAll, Any, DatabaseChangeLog>(
    IncludeAll::class,
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
    val path by nonNullable(String::class)
    val resourceFilter by nullable(String::class)
    val relativeToChangelogFile by nonNullable(Boolean::class, false)
    val errorIfMissingOrEmpty by nonNullable(Boolean::class, true)

    operator fun invoke(path: String) = path(path)
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
    val name by nonNullable(String::class)
    val value by nonNullable(String::class)
    val context by nullable(String::class)
    val dbms by nullable(String::class)

    operator fun invoke(name: String, value: String) = name(name).value(value)
}

class LkChangeSet : Refactorings<LkChangeSet, LkChangesHolder, DatabaseChangeLog>(
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
    val id by nonNullable(Any::class)
    val author by nullable(String::class)
    val dbms by nullable(String::class)
    val runAlways by nullable(Boolean::class, true) // TODO What is correct default?
    val runOnChange by nullable(Boolean::class, true) // TODO What is correct default?
    val context by nullable(String::class)
    val runInTransaction by nullable(Boolean::class)
    val failOnError by nullable(Boolean::class)

    val rollback by child(::LkRollback)
    val comment by child(::LkComment)
    val validCheckSum by child(::LkValidCheckSum)
    val precondition by child(::LkChangeSetPrecondition)

    operator fun invoke(id: Any) = id(id)
    operator fun invoke(id: Any, author: String) = id(id).author(author)
}

open class LkRollback : Refactorings<LkRollback, LkChangesHolder, ChangeSet>(
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
    val checkSum by nonNullable(String::class)
    operator fun invoke(checkSum: String) = checkSum(checkSum)
}

open class LkComment : LbDslNode<LkComment, Any, ChangeSet>(
    LkComment::class,
    ::Any,
    { changeSet, _, self, _ -> changeSet.comments += self.text.current }
) {
    val text by nonNullable(String::class)
    operator fun invoke(text: String) = text(text)
    operator fun minus(text: String) {
        text(text)
    }
}