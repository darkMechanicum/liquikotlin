package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.minus
import liquibase.change.core.*

open class AlterSequenceIntegration : ChangeIntegration<LkAlterSequence, AlterSequenceChange>(
    ::AlterSequenceChange,
    LkAlterSequence::catalogName - AlterSequenceChange::setCatalogName,
    LkAlterSequence::incrementBy - AlterSequenceChange::setIncrementBy,
    LkAlterSequence::maxValue - AlterSequenceChange::setMaxValue,
    LkAlterSequence::minValue - AlterSequenceChange::setMinValue,
    LkAlterSequence::ordered - AlterSequenceChange::setOrdered,
    LkAlterSequence::schemaName - AlterSequenceChange::setSchemaName,
    LkAlterSequence::cacheSize - AlterSequenceChange::setCacheSize,
    LkAlterSequence::willCycle - AlterSequenceChange::setWillCycle,
    LkAlterSequence::sequenceName - AlterSequenceChange::setSequenceName
)

open class EmptyIntegration : ChangeIntegration<LkEmpty, EmptyChange>(::EmptyChange)

open class ExecuteCommandIntegration : ChangeIntegration<LkExecuteCommand, ExecuteShellCommandChange>(
    ::ExecuteShellCommandChange,
    LkExecuteCommand::executable - ExecuteShellCommandChange::setExecutable,
    LkExecuteCommand::os - ExecuteShellCommandChange::setOs
)

open class InsertIntegration : ChangeIntegration<LkInsert, InsertDataChange>(
    ::InsertDataChange,
    LkInsert::catalogName - InsertDataChange::setCatalogName,
    LkInsert::dbms - InsertDataChange::setDbms,
    LkInsert::schemaName - InsertDataChange::setSchemaName,
    LkInsert::tableName - InsertDataChange::setTableName
)

open class LoadDataIntegration : ChangeIntegration<LkLoadData, LoadDataChange>(
    ::LoadDataChange,
    LkLoadData::catalogName - LoadDataChange::setCatalogName,
    LkLoadData::encoding - LoadDataChange::setEncoding,
    LkLoadData::file - LoadDataChange::setFile,
    LkLoadData::quotchar - LoadDataChange::setQuotchar,
    LkLoadData::schemaName - LoadDataChange::setSchemaName,
    LkLoadData::separator - LoadDataChange::setSeparator,
    LkLoadData::tableName - LoadDataChange::setTableName,
    LkLoadData::relativeToChangelogFile - LoadDataChange::setRelativeToChangelogFile
)

open class LoadUpdateDataIntegration : ChangeIntegration<LkLoadUpdateData, LoadUpdateDataChange>(
    ::LoadUpdateDataChange,
    LkLoadUpdateData::catalogName - LoadUpdateDataChange::setCatalogName,
    LkLoadUpdateData::encoding - LoadUpdateDataChange::setEncoding,
    LkLoadUpdateData::file - LoadUpdateDataChange::setFile,
    LkLoadUpdateData::primaryKey - LoadUpdateDataChange::setPrimaryKey,
    LkLoadUpdateData::quotchar - LoadUpdateDataChange::setQuotchar,
    LkLoadUpdateData::schemaName - LoadUpdateDataChange::setSchemaName,
    LkLoadUpdateData::separator - LoadUpdateDataChange::setSeparator,
    LkLoadUpdateData::tableName - LoadUpdateDataChange::setTableName,
    LkLoadUpdateData::relativeToChangelogFile - LoadUpdateDataChange::setRelativeToChangelogFile
)

open class MergeColumnsIntegration : ChangeIntegration<LkMergeColumns, MergeColumnChange>(
    ::MergeColumnChange,
    LkMergeColumns::catalogName - MergeColumnChange::setCatalogName,
    LkMergeColumns::column1Name - MergeColumnChange::setColumn1Name,
    LkMergeColumns::column2Name - MergeColumnChange::setColumn2Name,
    LkMergeColumns::finalColumnName - MergeColumnChange::setFinalColumnName,
    LkMergeColumns::finalColumnType - MergeColumnChange::setFinalColumnType,
    LkMergeColumns::joinString - MergeColumnChange::setJoinString,
    LkMergeColumns::schemaName - MergeColumnChange::setSchemaName,
    LkMergeColumns::tableName - MergeColumnChange::setTableName
)

open class ModifyDataTypeIntegration : ChangeIntegration<LkModifyDataType, ModifyDataTypeChange>(
    ::ModifyDataTypeChange,
    LkModifyDataType::catalogName - ModifyDataTypeChange::setCatalogName,
    LkModifyDataType::columnName - ModifyDataTypeChange::setColumnName,
    LkModifyDataType::newDataType - ModifyDataTypeChange::setNewDataType,
    LkModifyDataType::schemaName - ModifyDataTypeChange::setSchemaName,
    LkModifyDataType::tableName - ModifyDataTypeChange::setTableName
)

open class RenameColumnIntegration : ChangeIntegration<LkRenameColumn, RenameColumnChange>(
    ::RenameColumnChange,
    LkRenameColumn::catalogName - RenameColumnChange::setCatalogName,
    LkRenameColumn::columnDataType - RenameColumnChange::setColumnDataType,
    LkRenameColumn::newColumnName - RenameColumnChange::setNewColumnName,
    LkRenameColumn::oldColumnName - RenameColumnChange::setOldColumnName,
    LkRenameColumn::remarks - RenameColumnChange::setRemarks,
    LkRenameColumn::schemaName - RenameColumnChange::setSchemaName,
    LkRenameColumn::tableName - RenameColumnChange::setTableName
)

open class RenameTableIntegration : ChangeIntegration<LkRenameTable, RenameTableChange>(
    ::RenameTableChange,
    LkRenameTable::catalogName - RenameTableChange::setCatalogName,
    LkRenameTable::newTableName - RenameTableChange::setNewTableName,
    LkRenameTable::oldTableName - RenameTableChange::setOldTableName,
    LkRenameTable::schemaName - RenameTableChange::setSchemaName
)

open class RenameViewIntegration : ChangeIntegration<LkRenameView, RenameViewChange>(
    ::RenameViewChange,
    LkRenameView::catalogName - RenameViewChange::setCatalogName,
    LkRenameView::newViewName - RenameViewChange::setNewViewName,
    LkRenameView::oldViewName - RenameViewChange::setOldViewName,
    LkRenameView::schemaName - RenameViewChange::setSchemaName
)

open class SqlIntegration : ChangeIntegration<LkSql, RawSQLChange>(
    ::RawSQLChange,
    LkSql::comment - RawSQLChange::setComment,
    LkSql::dbms - RawSQLChange::setDbms,
    LkSql::endDelimiter - RawSQLChange::setEndDelimiter,
    LkSql::splitStatements - RawSQLChange::setSplitStatements,
    LkSql::sql - RawSQLChange::setSql,
    LkSql::stripComments - RawSQLChange::setStripComments
)

open class SqlFileIntegration : ChangeIntegration<LkSqlFile, SQLFileChange>(
    ::SQLFileChange,
    LkSqlFile::dbms - SQLFileChange::setDbms,
    LkSqlFile::encoding - SQLFileChange::setEncoding,
    LkSqlFile::endDelimiter - SQLFileChange::setEndDelimiter,
    LkSqlFile::path - SQLFileChange::setPath,
    LkSqlFile::relativeToChangelogFile - SQLFileChange::setRelativeToChangelogFile,
    LkSqlFile::splitStatements - SQLFileChange::setSplitStatements,
    LkSqlFile::stripComments - SQLFileChange::setStripComments
)

open class StopIntegration : ChangeIntegration<LkStop, StopChange>(
    ::StopChange,
    LkStop::message - StopChange::setMessage
)

open class TagDatabaseIntegration : ChangeIntegration<LkTagDatabase, TagDatabaseChange>(
    ::TagDatabaseChange,
    LkTagDatabase::tag - TagDatabaseChange::setTag
)

open class UpdateIntegration : ChangeIntegration<LkUpdate, UpdateDataChange>(
    ::UpdateDataChange,
    LkUpdate::catalogName - UpdateDataChange::setCatalogName,
    LkUpdate::schemaName - UpdateDataChange::setSchemaName,
    LkUpdate::tableName - UpdateDataChange::setTableName,
    LkUpdate::where - UpdateDataChange::setWhere
)