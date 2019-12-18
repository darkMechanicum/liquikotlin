package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.EvaluatableDslNode
import com.tsarev.liquikotlin.infrastructure.LbArg
import com.tsarev.liquikotlin.infrastructure.api.EvalAction
import com.tsarev.liquikotlin.infrastructure.api.EvalFactory
import com.tsarev.liquikotlin.infrastructure.default.DefaultNode
import liquibase.change.core.*
import liquibase.changelog.DatabaseChangeLog
import kotlin.reflect.KClass

/**
 * [EvaluatableDslNode.Evaluator]
 */
open class LiquibaseIntegrationFactory : EvalFactory<LbArg, DefaultNode> {

    protected open val withParent: Map<Pair<KClass<*>, KClass<*>>, EvalAction<DefaultNode, *, LbArg>> = mapOf(
        LkAddColumnConfig::class to LkAddColumn::class to
                AddColumnConfigIntegration<AddColumnChange> ({ change, it, _, _ -> change.addColumn(it) }),
        LkAddColumnConfig::class to LkCreateIndex::class to
                AddColumnConfigIntegration<CreateIndexChange> ({ change, it, _, _ -> change.addColumn(it) }),
        LkAddColumnConfig::class to LkInsert::class to
                AddColumnConfigIntegration<InsertDataChange> ({ change, it, _, _ -> change.addColumn(it) }),
        LkCommonColumnConfig::class to LkCreateTable::class to
                CommonColumnConfigIntegration<CreateTableChange> ({ change, it, _, _ -> change.addColumn(it) }),
        LkCommonColumnConfig::class to LkUpdate::class to
                CommonColumnConfigIntegration<UpdateDataChange> ({ change, it, _, _ -> change.addColumn(it) }),
        LkLoadColumnConfig::class to LkLoadData::class to
                LoadColumnConfigIntegration<LoadDataChange> ({ change, it, _, _ -> change.addColumn(it) }),
        LkLoadColumnConfig::class to LkLoadUpdateData::class to
                LoadColumnConfigIntegration<LoadUpdateDataChange> ({ change, it, _, _ -> change.addColumn(it) }),
        LkPrecondition::class to LkChangeSet::class to
                PreconditionContainerIntegration<ChangesHolder> { holder, it, _, _ -> holder.preconditions = it },
        LkPrecondition::class to LkChangeLog::class to
                PreconditionContainerIntegration<DatabaseChangeLog> { changeLog, it, _, _ ->
                    changeLog.preconditions = it
                }
    )

    protected open val single: Map<KClass<*>, EvalAction<DefaultNode, *, LbArg>> = mapOf(
        LkAddAutoIncrement::class to AddAutoIncrementIntegration(),
        LkAddColumn::class to AddColumnIntegration(),
        LkConstraints::class to ConstraintsIntegration(),
        LkAddDefaultValue::class to AddDefaultValueIntegration(),
        LkAddForeignKeyConstraint::class to AddForeignKeyConstraintIntegration(),
        LkAddLookupTable::class to AddLookupTableIntegration(),
        LkAddNotNullConstraint::class to AddNotNullConstraintIntegration(),
        LkAddPrimaryKey::class to AddPrimaryKeyIntegration(),
        LkAddUniqueConstraint::class to AddUniqueConstraintIntegration(),
        LkCreateIndex::class to CreateIndexIntegration(),
        LkCreateProcedure::class to CreateProcedureIntegration(),
        LkCreateSequence::class to CreateSequenceIntegration(),
        LkCreateTable::class to CreateTableIntegration(),
        LkCreateView::class to CreateViewIntegration(),
        LkDelete::class to DeleteIntegration(),
        LkDropAllForeignKeyConstraints::class to DropAllForeignKeyConstraintsIntegration(),
        LkDropColumn::class to DropColumnIntegration(),
        LkDropDefaultValue::class to DropDefaultValueIntegration(),
        LkDropForeignKeyConstraint::class to DropForeignKeyConstraintIntegration(),
        LkDropIndex::class to DropIndexIntegration(),
        LkDropNotNullConstraint::class to DropNotNullConstraintIntegration(),
        LkDropPrimaryKey::class to DropPrimaryKeyIntegration(),
        LkDropProcedure::class to DropProcedureIntegration(),
        LkDropSequence::class to DropSequenceIntegration(),
        LkDropTable::class to DropTableIntegration(),
        LkDropUniqueConstraint::class to DropUniqueConstraintIntegration(),
        LkDropView::class to DropViewIntegration(),
        LkAlterSequence::class to AlterSequenceIntegration(),
        LkEmpty::class to EmptyIntegration(),
        LkExecuteCommand::class to ExecuteCommandIntegration(),
        LkInsert::class to InsertIntegration(),
        LkLoadData::class to LoadDataIntegration(),
        LkLoadUpdateData::class to LoadUpdateDataIntegration(),
        LkMergeColumns::class to MergeColumnsIntegration(),
        LkModifyDataType::class to ModifyDataTypeIntegration(),
        LkRenameColumn::class to RenameColumnIntegration(),
        LkRenameTable::class to RenameTableIntegration(),
        LkRenameView::class to RenameViewIntegration(),
        LkSql::class to SqlIntegration(),
        LkSqlFile::class to SqlFileIntegration(),
        LkStop::class to StopIntegration(),
        LkTagDatabase::class to TagDatabaseIntegration(),
        LkUpdate::class to UpdateIntegration(),
        LkChangeLog::class to ChangeLogIntegration(),
        LkInclude::class to IncludeIntegration(),
        LkIncludeAll::class to IncludeAllIntegration(),
        LkProperty::class to PropertyIntegration(),
        LkChangeSet::class to ChangeSetIntegration(),
        LkRollback::class to RollbackIntegration(),
        LkValidCheckSum::class to ValidCheckSumIntegration(),
        LkComment::class to CommentIntegration(),
        LkDbmsPrecondition::class to DbmsPreconditionIntegration(),
        LkRunningAsPrecondition::class to RunningAsPreconditionIntegration(),
        LkChangeSetExecutedPrecondition::class to ChangeSetExecutedPreconditionIntegration(),
        LkColumnExistsPrecondition::class to ColumnExistsPreconditionIntegration(),
        LkTableExistsPrecondition::class to TableExistsPreconditionIntegration(),
        LkViewExistsPrecondition::class to ViewExistsPreconditionIntegration(),
        LkForeignKeyConstraintExistsPrecondition::class to ForeignKeyConstraintExistsPreconditionIntegration(),
        LkIndexExistsPrecondition::class to IndexExistsPreconditionIntegration(),
        LkSequenceExistsPrecondition::class to SequenceExistsPreconditionIntegration(),
        LkPrimaryKeyExistsPrecondition::class to PrimaryKeyExistsPreconditionIntegration(),
        LkSqlCheckPrecondition::class to SqlCheckPreconditionIntegration(),
        LkChangeLogPropertyDefinedPrecondition::class to ChangeLogPropertyDefinedPreconditionIntegration(),
        LkCustomPrecondition::class to CustomPreconditionIntegration(),
        LkCustomPreconditionParam::class to CustomPreconditionParamIntegration(),
        LkAndPrecondition::class to AndPreconditionIntegration(),
        LkOrPrecondition::class to OrPreconditionIntegration()
    )

    @Suppress("UNCHECKED_CAST")
    override fun <EvalT : Any> getAction(node: DefaultNode): EvalAction<DefaultNode, EvalT, LbArg> {
        val nodeParent = node.parent
        if (nodeParent != null) {
            val key = node.selfClass to nodeParent.value.selfClass
            withParent[key]?.let { return@getAction it as EvalAction<DefaultNode, EvalT, LbArg> }
        }
        return single.getValue(node.selfClass) as EvalAction<DefaultNode, EvalT, LbArg>
    }

}