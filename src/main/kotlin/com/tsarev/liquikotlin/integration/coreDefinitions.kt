package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.*
import liquibase.change.Change
import liquibase.changelog.ChangeSet
import liquibase.changelog.DatabaseChangeLog
import liquibase.changelog.IncludeAllFilter
import liquibase.exception.SetupException
import liquibase.precondition.core.PreconditionContainer
import liquibase.resource.ResourceAccessor

// --- Abstract base and utility classes ---

val comparator = Comparator<String> { o1, o2 -> o1.compareTo(o2) }

val defaultFilter = IncludeAllFilter { it?.endsWith(".kts") ?: false }

/**
 * Utility class to hide change add differency
 * between [ChangeSet] and [liquibase.changelog.RollbackContainer] present in [ChangeSet].
 */
open class ChangesHolder {
    open val changes: MutableCollection<Change> = ArrayList()
    open var preconditions: PreconditionContainer? = null
    open var rollback: ChangesHolder? = null
    open val comments: MutableCollection<String> = ArrayList()
    open val validCheckSums: MutableCollection<String> = ArrayList()
}

open class ChangeIntegration<NodeT : LbDslNode<NodeT>, ChangeT : Change>(
    linkedConstructor: () -> ChangeT
) : LiquibaseIntegrator<NodeT, ChangeT, ChangesHolder>(
    linkedConstructor,
    { holder, change, _, arg ->
        arg?.let { change.setResourceAccessor(it.second) }
        holder.changes.add(change)
    }
)

// --- Core definition classes ---

/**
 * Changelog. Entry point for rest nodes.
 */
open class ChangeLogIntegration : LiquibaseIntegrator<LkChangeLog, DatabaseChangeLog, Any>(::DatabaseChangeLog) {
    override fun initResult(thisNode: LkChangeLog, argument: LbArg?): DatabaseChangeLog? {
        val (physicalPath, _) = argument!!
        val result = DatabaseChangeLog(physicalPath)
        val resultPath = thisNode.logicalFilePath.current ?: physicalPath
        result.logicalFilePath = resultPath
        return result
    }
}

open class IncludeIntegration : LiquibaseIntegrator<LkInclude, Any, DatabaseChangeLog>(
    ::Any,
    { changeLog, _, self, arg ->
        val (_, resourceAccessor) = (arg as Pair<*, ResourceAccessor>)
        changeLog.include(self.path.current, self.relativeToChangelogFile.current, resourceAccessor)
    }
)

open class IncludeAllIntegration : LiquibaseIntegrator<LkIncludeAll, Any, DatabaseChangeLog>(
    ::Any,
    { changeLog, _, self, arg ->
        val (_, resourceAccessor) = arg!!
        val resourceFilterDef = self.resourceFilter.current
        var resourceFilter: IncludeAllFilter = defaultFilter
        if (!resourceFilterDef.isNullOrBlank()) {
            try {
                resourceFilter = Class.forName(resourceFilterDef).newInstance() as IncludeAllFilter
                resourceFilter =
                    IncludeAllFilter { path -> resourceFilter.include(path) && defaultFilter.include(path) }
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
)

/**
 * Changelog property.
 */
open class PropertyIntegration : LiquibaseIntegrator<LkProperty, Any, DatabaseChangeLog>(
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
)

open class ChangeSetIntegration : LiquibaseIntegrator<LkChangeSet, ChangesHolder, DatabaseChangeLog>(
    ::ChangesHolder,
    { changeLog, holder, self, _ ->
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
        holder.changes.forEach { result.addChange(it) }
        result.rollback.apply { holder.rollback?.changes?.forEach { this.changes.add(it) } }
        result.comments = holder.comments.joinToString()
        holder.validCheckSums.map { result.addValidCheckSum(it) }
        result.preconditions = holder.preconditions
        changeLog.addChangeSet(result)
    }
)

open class RollbackIntegration : LiquibaseIntegrator<LkRollback, ChangesHolder, ChangesHolder>(
    ::ChangesHolder,
    { holder, it, _, _ ->
        holder.rollback = it
    }
)

open class ValidCheckSumIntegration : LiquibaseIntegrator<LkValidCheckSum, Any, ChangesHolder>(
    ::Any,
    { holder, _, self, _ ->
        holder.validCheckSums.add(self.checkSum.current)
    }
)

open class CommentIntegration : LiquibaseIntegrator<LkComment, Any, ChangesHolder>(
    ::Any,
    { holder, _, self, _ ->
        holder.comments.add(self.text.current)
    }
)