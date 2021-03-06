package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.minus
import com.tsarev.liquikotlin.infrastructure.notNull
import liquibase.change.core.*

open class AlterSequenceIntegration : ChangeIntegration<AlterSequenceChange>(
    ::AlterSequenceChange,
    AlterSequenceChange::class.java,
    LkAlterSequence::catalogName - AlterSequenceChange::setCatalogName,
    LkAlterSequence::incrementBy - AlterSequenceChange::setIncrementBy,
    LkAlterSequence::maxValue - AlterSequenceChange::setMaxValue,
    LkAlterSequence::minValue - AlterSequenceChange::setMinValue,
    LkAlterSequence::ordered - AlterSequenceChange::setOrdered,
    LkAlterSequence::schemaName - AlterSequenceChange::setSchemaName,
    LkAlterSequence::cacheSize - AlterSequenceChange::setCacheSize,
    LkAlterSequence::sequenceName - AlterSequenceChange::setSequenceName
)

open class EmptyIntegration : ChangeIntegration<EmptyChange>(
    ::EmptyChange,
    EmptyChange::class.java
)

open class ExecuteCommandIntegration : ChangeIntegration<ExecuteShellCommandChange>(
    ::ExecuteShellCommandChange,
    ExecuteShellCommandChange::class.java,
    LkExecuteCommand::executable - ExecuteShellCommandChange::setExecutable,
    LkExecuteCommand::os - ExecuteShellCommandChange::setOs
)

open class InsertIntegration : ChangeIntegration<InsertDataChange>(
    ::InsertDataChange,
    InsertDataChange::class.java,
    LkInsert::catalogName - InsertDataChange::setCatalogName,
    LkInsert::dbms - InsertDataChange::setDbms,
    LkInsert::schemaName - InsertDataChange::setSchemaName,
    LkInsert::tableName - InsertDataChange::setTableName
)

open class LoadDataIntegration : ChangeIntegration<LoadDataChange>(
    ::LoadDataChange,
    LoadDataChange::class.java,
    LkLoadData::catalogName - LoadDataChange::setCatalogName,
    LkLoadData::encoding - LoadDataChange::setEncoding,
    LkLoadData::file - LoadDataChange::setFile,
    LkLoadData::quotchar - LoadDataChange::setQuotchar,
    LkLoadData::schemaName - LoadDataChange::setSchemaName,
    LkLoadData::separator - LoadDataChange::setSeparator,
    LkLoadData::tableName - LoadDataChange::setTableName,
    LkLoadData::relativeToChangelogFile - LoadDataChange::setRelativeToChangelogFile
)

open class LoadUpdateDataIntegration : ChangeIntegration<LoadUpdateDataChange>(
    ::LoadUpdateDataChange,
    LoadUpdateDataChange::class.java,
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

open class MergeColumnsIntegration : ChangeIntegration<MergeColumnChange>(
    ::MergeColumnChange,
    MergeColumnChange::class.java,
    LkMergeColumns::catalogName - MergeColumnChange::setCatalogName,
    LkMergeColumns::column1Name - MergeColumnChange::setColumn1Name,
    LkMergeColumns::column2Name - MergeColumnChange::setColumn2Name,
    LkMergeColumns::finalColumnName - MergeColumnChange::setFinalColumnName,
    LkMergeColumns::finalColumnType - MergeColumnChange::setFinalColumnType,
    LkMergeColumns::joinString - MergeColumnChange::setJoinString,
    LkMergeColumns::schemaName - MergeColumnChange::setSchemaName,
    LkMergeColumns::tableName - MergeColumnChange::setTableName
)

open class ModifyDataTypeIntegration : ChangeIntegration<ModifyDataTypeChange>(
    ::ModifyDataTypeChange,
    ModifyDataTypeChange::class.java,
    LkModifyDataType::catalogName - ModifyDataTypeChange::setCatalogName,
    LkModifyDataType::columnName - ModifyDataTypeChange::setColumnName,
    LkModifyDataType::newDataType - ModifyDataTypeChange::setNewDataType,
    LkModifyDataType::schemaName - ModifyDataTypeChange::setSchemaName,
    LkModifyDataType::tableName - ModifyDataTypeChange::setTableName
)

open class RenameColumnIntegration : ChangeIntegration<RenameColumnChange>(
    ::RenameColumnChange,
    RenameColumnChange::class.java,
    LkRenameColumn::catalogName - RenameColumnChange::setCatalogName,
    LkRenameColumn::columnDataType - RenameColumnChange::setColumnDataType,
    LkRenameColumn::newColumnName - RenameColumnChange::setNewColumnName,
    LkRenameColumn::oldColumnName - RenameColumnChange::setOldColumnName,
    LkRenameColumn::remarks - RenameColumnChange::setRemarks,
    LkRenameColumn::schemaName - RenameColumnChange::setSchemaName,
    LkRenameColumn::tableName - RenameColumnChange::setTableName
)

open class RenameTableIntegration : ChangeIntegration<RenameTableChange>(
    ::RenameTableChange,
    RenameTableChange::class.java,
    LkRenameTable::catalogName - RenameTableChange::setCatalogName,
    LkRenameTable::newTableName - RenameTableChange::setNewTableName,
    LkRenameTable::oldTableName - RenameTableChange::setOldTableName,
    LkRenameTable::schemaName - RenameTableChange::setSchemaName
)

open class RenameViewIntegration : ChangeIntegration<RenameViewChange>(
    ::RenameViewChange,
    RenameViewChange::class.java,
    LkRenameView::catalogName - RenameViewChange::setCatalogName,
    LkRenameView::newViewName - RenameViewChange::setNewViewName,
    LkRenameView::oldViewName - RenameViewChange::setOldViewName,
    LkRenameView::schemaName - RenameViewChange::setSchemaName
)

open class SqlIntegration : ChangeIntegration<RawSQLChange>(
    ::RawSQLChange,
    RawSQLChange::class.java,
    LkSql::comment - RawSQLChange::setComment,
    LkSql::dbms - RawSQLChange::setDbms,
    LkSql::endDelimiter - RawSQLChange::setEndDelimiter,
    LkSql::splitStatements - RawSQLChange::setSplitStatements,
    LkSql::sql notNull RawSQLChange::setSql,
    LkSql::stripComments - RawSQLChange::setStripComments
)

open class SqlFileIntegration : ChangeIntegration<SQLFileChange>(
    ::SQLFileChange,
    SQLFileChange::class.java,
    LkSqlFile::dbms - SQLFileChange::setDbms,
    LkSqlFile::encoding - SQLFileChange::setEncoding,
    LkSqlFile::endDelimiter - SQLFileChange::setEndDelimiter,
    LkSqlFile::path - SQLFileChange::setPath,
    LkSqlFile::relativeToChangelogFile - SQLFileChange::setRelativeToChangelogFile,
    LkSqlFile::splitStatements - SQLFileChange::setSplitStatements,
    LkSqlFile::stripComments - SQLFileChange::setStripComments
)

open class StopIntegration : ChangeIntegration<StopChange>(
    ::StopChange,
    StopChange::class.java,
    LkStop::message - StopChange::setMessage
)

open class TagDatabaseIntegration : ChangeIntegration<TagDatabaseChange>(
    ::TagDatabaseChange,
    TagDatabaseChange::class.java,
    LkTagDatabase::tag - TagDatabaseChange::setTag
)

open class UpdateIntegration : ChangeIntegration<UpdateDataChange>(
    ::UpdateDataChange,
    UpdateDataChange::class.java,
    LkUpdate::catalogName - UpdateDataChange::setCatalogName,
    LkUpdate::schemaName - UpdateDataChange::setSchemaName,
    LkUpdate::tableName - UpdateDataChange::setTableName,
    LkUpdate::where - UpdateDataChange::setWhere
)