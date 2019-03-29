package com.tsarev.liquikotlin.bundled

import liquibase.change.core.*
import java.math.BigInteger

open class LkAlterSequence : LkChange<LkAlterSequence, AlterSequenceChange>(
    LkAlterSequence::class,
    ::AlterSequenceChange
) {
    open val catalogName by nullableWS(String::class, AlterSequenceChange::setCatalogName)
    open val incrementBy by nullableWS(BigInteger::class, AlterSequenceChange::setIncrementBy)
    open val maxValue by nullableWS(BigInteger::class, AlterSequenceChange::setMaxValue)
    open val minValue by nullableWS(BigInteger::class, AlterSequenceChange::setMinValue)
    open val ordered by nullableWS(Boolean::class, AlterSequenceChange::setOrdered)
    open val schemaName by nullableWS(String::class, AlterSequenceChange::setSchemaName)
    open val sequenceName by nullableWS(String::class, AlterSequenceChange::setSequenceName)

    operator fun invoke(sequenceName: String) = sequenceName(sequenceName)
}

open class LkEmpty : LkChange<LkEmpty, EmptyChange>(LkEmpty::class, ::EmptyChange)

open class LkExecuteCommand : LkChange<LkExecuteCommand, ExecuteShellCommandChange>(
    LkExecuteCommand::class,
    ::ExecuteShellCommandChange
) {
    open val executable by nullableWS(String::class, ExecuteShellCommandChange::setExecutable)

    operator fun invoke(executable: String) = executable(executable)
}

open class LkInsert : LkChange<LkInsert, InsertDataChange>(
    LkInsert::class,
    ::InsertDataChange
) {
    open val catalogName by nullableWS(String::class, InsertDataChange::setCatalogName)
    open val dbms by nullableWS(String::class, InsertDataChange::setDbms)
    open val schemaName by nullableWS(String::class, InsertDataChange::setSchemaName)
    open val tableName by nullableWS(String::class, InsertDataChange::setTableName)

    open val column by child(::AddDataColumnConfig)

    class AddDataColumnConfig : LkCommonColumnConfig<AddDataColumnConfig, InsertDataChange>(
        AddDataColumnConfig::class,
        { change, it, _ -> change.addColumn(it) }
    )

    operator fun invoke(tableName: String) = tableName(tableName)
}

open class LkLoadData : LkChange<LkLoadData, LoadDataChange>(
    LkLoadData::class,
    ::LoadDataChange
) {
    open val catalogName by nullableWS(String::class, LoadDataChange::setCatalogName)
    open val encoding by nullableWS(String::class, LoadDataChange::setEncoding)
    open val file by nullableWS(String::class, LoadDataChange::setFile)
    open val quotchar by nullableWS(String::class, LoadDataChange::setQuotchar)
    open val schemaName by nullableWS(String::class, LoadDataChange::setSchemaName)
    open val separator by nullableWS(String::class, LoadDataChange::setSeparator)
    open val tableName by nullableWS(String::class, LoadDataChange::setTableName)

    open val column by child(::LkLoadDataColumnConfig)

    class LkLoadDataColumnConfig : LkBaseColumnConfig<LkLoadDataColumnConfig, LoadDataColumnConfig, LoadDataChange>(
        LkLoadDataColumnConfig::class,
        ::LoadDataColumnConfig,
        { change, it, _ -> change.addColumn(it) }
    )

    operator fun invoke(tableName: String) = tableName(tableName)
}

open class LkLoadUpdateData : LkChange<LkLoadUpdateData, LoadUpdateDataChange>(
    LkLoadUpdateData::class,
    ::LoadUpdateDataChange
) {
    open val catalogName by nullableWS(String::class, LoadUpdateDataChange::setCatalogName)
    open val encoding by nullableWS(String::class, LoadUpdateDataChange::setEncoding)
    open val file by nullableWS(String::class, LoadUpdateDataChange::setFile)
    open val primaryKey by nullableWS(String::class, LoadUpdateDataChange::setPrimaryKey)
    open val quotchar by nullableWS(String::class, LoadUpdateDataChange::setQuotchar)
    open val schemaName by nullableWS(String::class, LoadUpdateDataChange::setSchemaName)
    open val separator by nullableWS(String::class, LoadUpdateDataChange::setSeparator)
    open val tableName by nullableWS(String::class, LoadUpdateDataChange::setTableName)

    open val column by child(::LkLoadUpdateDataColumnConfig)

    class LkLoadUpdateDataColumnConfig :
        LkBaseColumnConfig<LkLoadUpdateDataColumnConfig, LoadDataColumnConfig, LoadUpdateDataChange>(
            LkLoadUpdateDataColumnConfig::class,
            ::LoadDataColumnConfig,
            { change, it, _ -> change.addColumn(it) }
        )

    operator fun invoke(tableName: String) = tableName(tableName)
}

open class LkMergeColumns : LkChange<LkMergeColumns, MergeColumnChange>(
    LkMergeColumns::class,
    ::MergeColumnChange
) {
    open val catalogName by nullableWS(String::class, MergeColumnChange::setCatalogName)
    open val column1Name by nullableWS(String::class, MergeColumnChange::setColumn1Name)
    open val column2Name by nullableWS(String::class, MergeColumnChange::setColumn2Name)
    open val finalColumnName by nullableWS(String::class, MergeColumnChange::setFinalColumnName)
    open val finalColumnType by nullableWS(String::class, MergeColumnChange::setFinalColumnType)
    open val joinString by nullableWS(String::class, MergeColumnChange::setJoinString)
    open val schemaName by nullableWS(String::class, MergeColumnChange::setSchemaName)
    open val tableName by nullableWS(String::class, MergeColumnChange::setTableName)

    operator fun invoke(tableName: String) = tableName(tableName)
}

open class LkModifyDataType : LkChange<LkModifyDataType, ModifyDataTypeChange>(
    LkModifyDataType::class,
    ::ModifyDataTypeChange
) {
    open val catalogName by nullableWS(String::class, ModifyDataTypeChange::setCatalogName)
    open val columnName by nullableWS(String::class, ModifyDataTypeChange::setColumnName)
    open val newDataType by nullableWS(String::class, ModifyDataTypeChange::setNewDataType)
    open val schemaName by nullableWS(String::class, ModifyDataTypeChange::setSchemaName)
    open val tableName by nullableWS(String::class, ModifyDataTypeChange::setTableName)

    operator fun invoke(tableName: String, columnName: String, newDataType: String) =
        tableName(tableName).columnName(columnName).newDataType(newDataType)
}

open class LkRenameColumn : LkChange<LkRenameColumn, RenameColumnChange>(
    LkRenameColumn::class,
    ::RenameColumnChange
) {
    open val catalogName by nullableWS(String::class, RenameColumnChange::setCatalogName)
    open val columnDataType by nullableWS(String::class, RenameColumnChange::setColumnDataType)
    open val newColumnName by nullableWS(String::class, RenameColumnChange::setNewColumnName)
    open val oldColumnName by nullableWS(String::class, RenameColumnChange::setOldColumnName)
    open val remarks by nullableWS(String::class, RenameColumnChange::setRemarks)
    open val schemaName by nullableWS(String::class, RenameColumnChange::setSchemaName)
    open val tableName by nullableWS(String::class, RenameColumnChange::setTableName)

    operator fun invoke(tableName: String, oldColumnName: String, newColumnName: String) =
        tableName(tableName).oldColumnName(oldColumnName).newColumnName(newColumnName)
}

open class LkRenameTable : LkChange<LkRenameTable, RenameTableChange>(
    LkRenameTable::class,
    ::RenameTableChange
) {
    open val catalogName by nullableWS(String::class, RenameTableChange::setCatalogName)
    open val newTableName by nullableWS(String::class, RenameTableChange::setNewTableName)
    open val oldTableName by nullableWS(String::class, RenameTableChange::setOldTableName)
    open val schemaName by nullableWS(String::class, RenameTableChange::setSchemaName)

    operator fun invoke(oldTableName: String, newTableName: String) =
        oldTableName(oldTableName).newTableName(newTableName)
}

open class LkRenameView : LkChange<LkRenameView, RenameViewChange>(
    LkRenameView::class,
    ::RenameViewChange
) {
    open val catalogName by nullableWS(String::class, RenameViewChange::setCatalogName)
    open val newViewName by nullableWS(String::class, RenameViewChange::setNewViewName)
    open val oldViewName by nullableWS(String::class, RenameViewChange::setOldViewName)
    open val schemaName by nullableWS(String::class, RenameViewChange::setSchemaName)

    operator fun invoke(oldViewName: String, newViewName: String) =
            oldViewName(oldViewName).newViewName(newViewName)
}

open class LkSql : LkChange<LkSql, RawSQLChange>(
    LkSql::class,
    ::RawSQLChange
) {
    open val comment by nullableWS(String::class, RawSQLChange::setComment)
    open val dbms by nullableWS(String::class, RawSQLChange::setDbms)
    open val endDelimiter by nullableWS(String::class, RawSQLChange::setEndDelimiter)
    open val splitStatements by nullableWS(Boolean::class, RawSQLChange::setSplitStatements)
    open val sql by nonNullableWS(String::class, RawSQLChange::setSql)
    open val stripComments by nullableWS(Boolean::class, RawSQLChange::setStripComments)

    operator fun minus(sql: String) = sql(sql)
}

open class LkSqlFile : LkChange<LkSqlFile, SQLFileChange>(
    LkSqlFile::class,
    ::SQLFileChange
) {
    open val dbms by nullableWS(String::class, SQLFileChange::setDbms)
    open val encoding by nullableWS(String::class, SQLFileChange::setEncoding)
    open val endDelimiter by nullableWS(String::class, SQLFileChange::setEndDelimiter)
    open val path by nullableWS(String::class, SQLFileChange::setPath)
    open val relativeToChangelogFile by nullableWS(Boolean::class, SQLFileChange::setRelativeToChangelogFile)
    open val splitStatements by nullableWS(Boolean::class, SQLFileChange::setSplitStatements)
    open val stripComments by nullableWS(Boolean::class, SQLFileChange::setStripComments)

    operator fun invoke(path: String) = path(path)
}

open class LkStop : LkChange<LkStop, StopChange>(LkStop::class, ::StopChange) {
    open val message by nullableWS(String::class, StopChange::setMessage)

    operator fun invoke(message: String) = message(message)
}

open class LkTagDatabase : LkChange<LkTagDatabase, TagDatabaseChange>(LkTagDatabase::class, ::TagDatabaseChange) {
    open val tag by nullableWS(String::class, TagDatabaseChange::setTag)

    operator fun invoke(tag: String) = tag(tag)
}

open class LkUpdate : LkChange<LkUpdate, UpdateDataChange>(LkUpdate::class, ::UpdateDataChange) {
    open val catalogName by nullableWS(String::class, UpdateDataChange::setCatalogName)
    open val schemaName by nullableWS(String::class, UpdateDataChange::setSchemaName)
    open val tableName by nullableWS(String::class, UpdateDataChange::setTableName)
    open val where by nullableWS(String::class, UpdateDataChange::setWhere)

    open val column by child(::LkLoadUpdateDataColumnConfig)

    class LkLoadUpdateDataColumnConfig : LkCommonColumnConfig<LkLoadUpdateDataColumnConfig, UpdateDataChange>(
        LkLoadUpdateDataColumnConfig::class, { change, it, _ -> change.addColumn(it) })

    operator fun invoke(tableName: String) = tableName(tableName)
}

