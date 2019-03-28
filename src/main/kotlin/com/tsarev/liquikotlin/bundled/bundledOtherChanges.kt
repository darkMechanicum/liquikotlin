package com.tsarev.liquikotlin.bundled

import liquibase.change.core.*
import java.math.BigInteger

open class LkAlterSequence : LkChange<LkAlterSequence, AlterSequenceChange>(
    LkAlterSequence::class,
    ::AlterSequenceChange
) {
    val catalogName by nullableWS(String::class, AlterSequenceChange::setCatalogName)
    val incrementBy by nullableWS(BigInteger::class, AlterSequenceChange::setIncrementBy)
    val maxValue by nullableWS(BigInteger::class, AlterSequenceChange::setMaxValue)
    val minValue by nullableWS(BigInteger::class, AlterSequenceChange::setMinValue)
    val ordered by nullableWS(Boolean::class, AlterSequenceChange::setOrdered)
    val schemaName by nullableWS(String::class, AlterSequenceChange::setSchemaName)
    val sequenceName by nullableWS(String::class, AlterSequenceChange::setSequenceName)

    operator fun invoke(sequenceName: String) = sequenceName(sequenceName)
}

open class LkEmpty : LkChange<LkEmpty, EmptyChange>(LkEmpty::class, ::EmptyChange)

open class LkExecuteCommand : LkChange<LkExecuteCommand, ExecuteShellCommandChange>(
    LkExecuteCommand::class,
    ::ExecuteShellCommandChange
) {
    val executable by nullableWS(String::class, ExecuteShellCommandChange::setExecutable)

    operator fun invoke(executable: String) = executable(executable)
}

open class LkInsert : LkChange<LkInsert, InsertDataChange>(
    LkInsert::class,
    ::InsertDataChange
) {
    val catalogName by nullableWS(String::class, InsertDataChange::setCatalogName)
    val dbms by nullableWS(String::class, InsertDataChange::setDbms)
    val schemaName by nullableWS(String::class, InsertDataChange::setSchemaName)
    val tableName by nullableWS(String::class, InsertDataChange::setTableName)

    val column by child(::AddDataColumnConfig)

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
    val catalogName by nullableWS(String::class, LoadDataChange::setCatalogName)
    val encoding by nullableWS(String::class, LoadDataChange::setEncoding)
    val file by nullableWS(String::class, LoadDataChange::setFile)
    val quotchar by nullableWS(String::class, LoadDataChange::setQuotchar)
    val schemaName by nullableWS(String::class, LoadDataChange::setSchemaName)
    val separator by nullableWS(String::class, LoadDataChange::setSeparator)
    val tableName by nullableWS(String::class, LoadDataChange::setTableName)

    val column by child(::LkLoadDataColumnConfig)

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
    val catalogName by nullableWS(String::class, LoadUpdateDataChange::setCatalogName)
    val encoding by nullableWS(String::class, LoadUpdateDataChange::setEncoding)
    val file by nullableWS(String::class, LoadUpdateDataChange::setFile)
    val primaryKey by nullableWS(String::class, LoadUpdateDataChange::setPrimaryKey)
    val quotchar by nullableWS(String::class, LoadUpdateDataChange::setQuotchar)
    val schemaName by nullableWS(String::class, LoadUpdateDataChange::setSchemaName)
    val separator by nullableWS(String::class, LoadUpdateDataChange::setSeparator)
    val tableName by nullableWS(String::class, LoadUpdateDataChange::setTableName)

    val column by child(::LkLoadUpdateDataColumnConfig)

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
    val catalogName by nullableWS(String::class, MergeColumnChange::setCatalogName)
    val column1Name by nullableWS(String::class, MergeColumnChange::setColumn1Name)
    val column2Name by nullableWS(String::class, MergeColumnChange::setColumn2Name)
    val finalColumnName by nullableWS(String::class, MergeColumnChange::setFinalColumnName)
    val finalColumnType by nullableWS(String::class, MergeColumnChange::setFinalColumnType)
    val joinString by nullableWS(String::class, MergeColumnChange::setJoinString)
    val schemaName by nullableWS(String::class, MergeColumnChange::setSchemaName)
    val tableName by nullableWS(String::class, MergeColumnChange::setTableName)

    operator fun invoke(tableName: String) = tableName(tableName)
}

open class LkModifyDataType : LkChange<LkModifyDataType, ModifyDataTypeChange>(
    LkModifyDataType::class,
    ::ModifyDataTypeChange
) {
    val catalogName by nullableWS(String::class, ModifyDataTypeChange::setCatalogName)
    val columnName by nullableWS(String::class, ModifyDataTypeChange::setColumnName)
    val newDataType by nullableWS(String::class, ModifyDataTypeChange::setNewDataType)
    val schemaName by nullableWS(String::class, ModifyDataTypeChange::setSchemaName)
    val tableName by nullableWS(String::class, ModifyDataTypeChange::setTableName)

    operator fun invoke(tableName: String, columnName: String, newDataType: String) =
        tableName(tableName).columnName(columnName).newDataType(newDataType)
}

open class LkRenameColumn : LkChange<LkRenameColumn, RenameColumnChange>(
    LkRenameColumn::class,
    ::RenameColumnChange
) {
    val catalogName by nullableWS(String::class, RenameColumnChange::setCatalogName)
    val columnDataType by nullableWS(String::class, RenameColumnChange::setColumnDataType)
    val newColumnName by nullableWS(String::class, RenameColumnChange::setNewColumnName)
    val oldColumnName by nullableWS(String::class, RenameColumnChange::setOldColumnName)
    val remarks by nullableWS(String::class, RenameColumnChange::setRemarks)
    val schemaName by nullableWS(String::class, RenameColumnChange::setSchemaName)
    val tableName by nullableWS(String::class, RenameColumnChange::setTableName)

    operator fun invoke(tableName: String, oldColumnName: String, newColumnName: String) =
        tableName(tableName).oldColumnName(oldColumnName).newColumnName(newColumnName)
}

open class LkRenameTable : LkChange<LkRenameTable, RenameTableChange>(
    LkRenameTable::class,
    ::RenameTableChange
) {
    val catalogName by nullableWS(String::class, RenameTableChange::setCatalogName)
    val newTableName by nullableWS(String::class, RenameTableChange::setNewTableName)
    val oldTableName by nullableWS(String::class, RenameTableChange::setOldTableName)
    val schemaName by nullableWS(String::class, RenameTableChange::setSchemaName)

    operator fun invoke(oldTableName: String, newTableName: String) =
        oldTableName(oldTableName).newTableName(newTableName)
}

open class LkRenameView : LkChange<LkRenameView, RenameViewChange>(
    LkRenameView::class,
    ::RenameViewChange
) {
    val catalogName by nullableWS(String::class, RenameViewChange::setCatalogName)
    val newViewName by nullableWS(String::class, RenameViewChange::setNewViewName)
    val oldViewName by nullableWS(String::class, RenameViewChange::setOldViewName)
    val schemaName by nullableWS(String::class, RenameViewChange::setSchemaName)

    operator fun invoke(oldViewName: String, newViewName: String) =
            oldViewName(oldViewName).newViewName(newViewName)
}

open class LkSql : LkChange<LkSql, RawSQLChange>(
    LkSql::class,
    ::RawSQLChange
) {
    val comment by nullableWS(String::class, RawSQLChange::setComment)
    val dbms by nullableWS(String::class, RawSQLChange::setDbms)
    val endDelimiter by nullableWS(String::class, RawSQLChange::setEndDelimiter)
    val splitStatements by nullableWS(Boolean::class, RawSQLChange::setSplitStatements)
    val sql by nonNullableWS(String::class, RawSQLChange::setSql)
    val stripComments by nullableWS(Boolean::class, RawSQLChange::setStripComments)

    operator fun minus(sql: String) {
        sql(sql)
    }
}

open class LkSqlFile : LkChange<LkSqlFile, SQLFileChange>(
    LkSqlFile::class,
    ::SQLFileChange
) {
    val dbms by nullableWS(String::class, SQLFileChange::setDbms)
    val encoding by nullableWS(String::class, SQLFileChange::setEncoding)
    val endDelimiter by nullableWS(String::class, SQLFileChange::setEndDelimiter)
    val path by nullableWS(String::class, SQLFileChange::setPath)
    val relativeToChangelogFile by nullableWS(Boolean::class, SQLFileChange::setRelativeToChangelogFile)
    val splitStatements by nullableWS(Boolean::class, SQLFileChange::setSplitStatements)
    val stripComments by nullableWS(Boolean::class, SQLFileChange::setStripComments)

    operator fun invoke(path: String) = path(path)
}

open class LkStop : LkChange<LkStop, StopChange>(LkStop::class, ::StopChange) {
    val message by nullableWS(String::class, StopChange::setMessage)

    operator fun invoke(message: String) = message(message)
}

open class LkTagDatabase : LkChange<LkTagDatabase, TagDatabaseChange>(LkTagDatabase::class, ::TagDatabaseChange) {
    val tag by nullableWS(String::class, TagDatabaseChange::setTag)

    operator fun invoke(tag: String) = tag(tag)
}

open class LkUpdate : LkChange<LkUpdate, UpdateDataChange>(LkUpdate::class, ::UpdateDataChange) {
    val catalogName by nullableWS(String::class, UpdateDataChange::setCatalogName)
    val schemaName by nullableWS(String::class, UpdateDataChange::setSchemaName)
    val tableName by nullableWS(String::class, UpdateDataChange::setTableName)
    val where by nullableWS(String::class, UpdateDataChange::setWhere)

    val column by child(::LkLoadUpdateDataColumnConfig)

    class LkLoadUpdateDataColumnConfig : LkCommonColumnConfig<LkLoadUpdateDataColumnConfig, UpdateDataChange>(
        LkLoadUpdateDataColumnConfig::class, { change, it, _ -> change.addColumn(it) })

    operator fun invoke(tableName: String) = tableName(tableName)
}

