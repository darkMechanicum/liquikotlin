package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.*
import com.tsarev.liquikotlin.infrastructure.default.DefaultNode
import com.tsarev.liquikotlin.util.letTry
import liquibase.ContextExpression
import liquibase.LabelExpression
import liquibase.Scope
import liquibase.change.Change
import liquibase.changelog.ChangeLogParameters
import liquibase.changelog.ChangeSet
import liquibase.changelog.DatabaseChangeLog
import liquibase.changelog.IncludeAllFilter
import liquibase.exception.SetupException
import liquibase.precondition.core.PreconditionContainer
import liquibase.resource.ResourceAccessor

// --- Abstract base and utility classes ---

val comparator = Comparator<String> { o1, o2 -> o1.compareTo(o2) }

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

open class ChangeIntegration<ChangeT : Change>(
    linkedConstructor: () -> ChangeT,
    linkedClass: Class<ChangeT>,
    vararg childMappings: PropertyMapping<DefaultNode, ChangeT, *>
) : LiquibaseIntegrator<ChangeT, ChangesHolder>(
    linkedConstructor,
    linkedClass,
    { holder, change, _, arg ->
        holder.changes.add(change!!)
    }
) { init {
    propertyMappings.addAll(childMappings)
}
}

// --- Core definition classes ---

/**
 * Changelog. Entry point for rest nodes.
 */
open class ChangeLogIntegration : LiquibaseIntegrator<DatabaseChangeLog, Any>(
    ::DatabaseChangeLog,
    DatabaseChangeLog::class.java
) {
    override fun doBefore(thisNode: DefaultNode, argument: LbArg?): DatabaseChangeLog? {
        val (physicalPath, _) = argument!!
        val result = DatabaseChangeLog(physicalPath).apply { changeLogParameters = ChangeLogParameters() }
        val resultPath = thisNode.getNullable(LkChangeLog::logicalFilePath) ?: physicalPath
        result.logicalFilePath = resultPath
        return result
    }
}

open class IncludeIntegration : LiquibaseIntegrator<Any, DatabaseChangeLog>(
    ::Any,
    Any::class.java,
    { changeLog, _, self, arg ->
        val (_, resourceAccessor) = (arg as Pair<*, ResourceAccessor>)
        changeLog.include(
            self.get(LkInclude::path),
            self.get(LkInclude::relativeToChangelogFile),
            resourceAccessor,
            ContextExpression(self.getNullable(LkInclude::context)),
            LabelExpression(self.getNullable(LkInclude::labels)),
            self.get(LkInclude::ignore),
            true
        )
    }
)

open class IncludeAllIntegration : LiquibaseIntegrator<Any, DatabaseChangeLog>(
    ::Any,
    Any::class.java,
    { changeLog, _, self, arg ->
        val (_, resourceAccessor) = arg!!
        val resourceFilterDef: String? = self.getNullable(LkIncludeAll::resourceFilter)
        val resourceFilter: IncludeAllFilter? = resourceFilterDef
            .takeUnless { it.isNullOrBlank() }
            .letTry { Class.forName(it).newInstance() as IncludeAllFilter }(::SetupException)
        changeLog.includeAll(
            self.get(LkIncludeAll::path),
            self.get(LkIncludeAll::relativeToChangelogFile),
            resourceFilter,
            self.get(LkIncludeAll::errorIfMissingOrEmpty),
            comparator,
            resourceAccessor,
            ContextExpression(self.getNullable(LkInclude::context)),
            LabelExpression(self.getNullable(LkInclude::labels)),
            self.get(LkInclude::ignore)
        )
    }
)

/**
 * Changelog property.
 */
open class PropertyIntegration : LiquibaseIntegrator<Any, DatabaseChangeLog>(
    ::Any,
    Any::class.java,
    { changeLog, _, self, _ ->
        changeLog.changeLogParameters.set(
            self.get(LkProperty::name),
            self.get(LkProperty::value),
            self.getNullable(LkProperty::context),
            null,
            self.getNullable(LkProperty::dbms),
            true,
            changeLog
        )
    }
)

open class ChangeSetIntegration : LiquibaseIntegrator<ChangesHolder, DatabaseChangeLog>(
    ::ChangesHolder,
    ChangesHolder::class.java,
    { changeLog, holder, self, _ ->
        val result = ChangeSet(
            self.get(LkChangeSet::id).toString(),
            self.getNullable(LkChangeSet::author),
            self.getNullable(LkChangeSet::runAlways) ?: false,
            self.getNullable(LkChangeSet::runOnChange) ?: true,
            changeLog.physicalFilePath,
            self.getNullable(LkChangeSet::context),
            self.getNullable(LkChangeSet::dbms),
            changeLog
        )
        holder!!
        holder.changes.forEach { result.addChange(it) }
        result.rollback.apply { holder.rollback?.changes?.forEach { this.changes.add(it) } }
        result.comments = holder.comments.joinToString()
        result.failOnError = self.getNullable(LkChangeSet::failOnError)
        holder.validCheckSums.map { result.addValidCheckSum(it) }
        result.preconditions = holder.preconditions
        changeLog.addChangeSet(result)
    }
)

open class RollbackIntegration : LiquibaseIntegrator<ChangesHolder, ChangesHolder>(
    ::ChangesHolder,
    ChangesHolder::class.java,
    { holder, it, _, _ ->
        holder.rollback = it
    }
)

open class ValidCheckSumIntegration : LiquibaseIntegrator<Any, ChangesHolder>(
    ::Any,
    Any::class.java,
    { holder, _, self, _ ->
        holder.validCheckSums.add(self.get(LkValidCheckSum::checkSum))
    }
)

open class CommentIntegration : LiquibaseIntegrator<Any, ChangesHolder>(
    ::Any,
    Any::class.java,
    { holder, _, self, _ ->
        holder.comments.add(self.get(LkComment::text))
    }
)