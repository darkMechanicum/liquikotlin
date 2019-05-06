package com.tsarev.liquikotlin.allattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.util.*
import liquibase.change.AbstractSQLChange
import liquibase.change.ColumnConfig
import liquibase.change.core.*
import org.junit.Test

/**
 * Testing that basic evaluations process all attributes
 * correctly without modifications.
 */
class BundledOtherChangesTest : BaseLiquikotlinUnitTest() {

    companion object {
        val abstractSql = arrayOf(
            AbstractSQLChange::isStripComments to stripComments,
            AbstractSQLChange::isSplitStatements to splitStatements,
            AbstractSQLChange::getEndDelimiter to endDelimiter,
            AbstractSQLChange::getDbms to dbms
        )
    }

    @Test
    fun alterSequenceTest() = testEvaluation(
        LkAlterSequence()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .sequenceName(sequenceName)
            .incrementBy(incrementBy)
            .maxValue(maxValue)
            .minValue(minValue)
            .ordered(ordered),
//            .cacheSize(cacheSize) TODO Implement missed attribute
//            .willCycle(willCycle), TODO Implement missed attribute
        AlterSequenceChange::class,
        AlterSequenceChange::getCatalogName to catalogName,
        AlterSequenceChange::getSchemaName to schemaName,
        AlterSequenceChange::getSequenceName to sequenceName,
        AlterSequenceChange::getIncrementBy to incrementBy,
        AlterSequenceChange::getMaxValue to maxValue,
        AlterSequenceChange::getMinValue to minValue,
        AlterSequenceChange::isOrdered to ordered
//        AlterSequenceChange::getCacheSize, TODO Implement missed attribute
//        AlterSequenceChange::getWillCycle TODO Implement missed attribute
    )

    // TODO Add args testing
    @Test
    fun executeCommandTest() = testEvaluation(
        LkExecuteCommand()
            .executable(executable),
//            .os(os), TODO Implement missed attribute
        ExecuteShellCommandChange::class,
        ExecuteShellCommandChange::getExecutable to executable,
//        ExecuteShellCommandChange::getOs, TODO Implement missed attribute
        ExecuteShellCommandChange::getArgs to emptyList<String>()
    )

    @Test
    fun insertTest() = testEvaluation(
        LkInsert()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .dbms(dbms),
        InsertDataChange::class,
        InsertDataChange::getCatalogName to catalogName,
        InsertDataChange::getSchemaName to schemaName,
        InsertDataChange::getTableName to tableName,
        InsertDataChange::getColumns to emptyList<ColumnConfig>(),
        InsertDataChange::getDbms to dbms
    )

    @Test
    fun loadDataTest() = testEvaluation(
        LkLoadData()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .file(file)
//            .relativeToChangelogFile(relativeToChangelogFile) TODO Implement missed attribute
            .encoding(encoding),
        LoadDataChange::class,
        LoadDataChange::getCatalogName to catalogName,
        LoadDataChange::getSchemaName to schemaName,
        LoadDataChange::getTableName to tableName,
        LoadDataChange::getFile to file,
//        LoadDataChange::isRelativeToChangelogFile, TODO Implement missed attribute
        LoadDataChange::getEncoding to encoding,
        LoadDataChange::getSeparator to liquibase.util.csv.opencsv.CSVReader.DEFAULT_SEPARATOR + "",
        LoadDataChange::getQuotchar to liquibase.util.csv.opencsv.CSVReader.DEFAULT_QUOTE_CHARACTER + "",
        LoadDataChange::getColumns to emptyList<LoadDataColumnConfig>()
    )

    @Test
    fun loadUpdateDataTest() = testEvaluation(
        LkUpdate()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .where(where),
        UpdateDataChange::class,
        *BundledDropChangesTest.abstractModifyFields,
        UpdateDataChange::getColumns to emptyList<ColumnConfig>()
    )

    @Test
    fun mergeColumnsTest() = testEvaluation(
        LkMergeColumns()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .column1Name(column1Name)
            .joinString(joinString)
            .column2Name(column2Name)
            .finalColumnName(finalColumnName)
            .finalColumnType(finalColumnType),
        MergeColumnChange::class,
        MergeColumnChange::getCatalogName to catalogName,
        MergeColumnChange::getSchemaName to schemaName,
        MergeColumnChange::getTableName to tableName,
        MergeColumnChange::getColumn1Name to column1Name,
        MergeColumnChange::getJoinString to joinString,
        MergeColumnChange::getColumn2Name to column2Name,
        MergeColumnChange::getFinalColumnName to finalColumnName,
        MergeColumnChange::getFinalColumnType to finalColumnType
    )

    @Test
    fun modifyDataTypeTest() = testEvaluation(
        LkModifyDataType()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .columnName(columnName)
            .newDataType(newDataType),
        ModifyDataTypeChange::class,
        ModifyDataTypeChange::getCatalogName to catalogName,
        ModifyDataTypeChange::getSchemaName to schemaName,
        ModifyDataTypeChange::getTableName to tableName,
        ModifyDataTypeChange::getColumnName to columnName,
        ModifyDataTypeChange::getNewDataType to newDataType
    )

    @Test
    fun renameColumnTest() = testEvaluation(
        LkRenameColumn()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .oldColumnName(oldColumnName)
            .newColumnName(newColumnName)
            .columnDataType(columnType)
            .remarks(remarks),
        RenameColumnChange::class,
        RenameColumnChange::getCatalogName to catalogName,
        RenameColumnChange::getSchemaName to schemaName,
        RenameColumnChange::getTableName to tableName,
        RenameColumnChange::getOldColumnName to oldColumnName,
        RenameColumnChange::getNewColumnName to newColumnName,
        RenameColumnChange::getColumnDataType to columnType,
        RenameColumnChange::getRemarks to remarks
    )

    @Test
    fun renameTableTest() = testEvaluation(
        LkRenameTable()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .oldTableName(oldTableName)
            .newTableName(newTableName),
        RenameTableChange::class,
        RenameTableChange::getCatalogName to catalogName,
        RenameTableChange::getSchemaName to schemaName,
        RenameTableChange::getOldTableName to oldTableName,
        RenameTableChange::getNewTableName to newTableName
    )

    @Test
    fun renameViewTest() = testEvaluation(
        LkRenameView()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .oldViewName(oldViewName)
            .newViewName(newViewName),
        RenameViewChange::class,
        RenameViewChange::getCatalogName to catalogName,
        RenameViewChange::getSchemaName to schemaName,
        RenameViewChange::getOldViewName to oldViewName,
        RenameViewChange::getNewViewName to newViewName
    )

    @Test
    fun sqlTest() = testEvaluation(
        LkSql()
            .sql(sql)
            .comment(comment)
            .stripComments(stripComments)
            .splitStatements(splitStatements)
            .endDelimiter(endDelimiter)
            .dbms(dbms),
        RawSQLChange::class,
        *abstractSql,
        RawSQLChange::getSql to sql,
        RawSQLChange::getComment to comment
    )

    @Test
    fun sqlFileTest() = testEvaluation(
        LkSqlFile()
            .stripComments(stripComments)
            .splitStatements(splitStatements)
            .endDelimiter(endDelimiter)
            .dbms(dbms)
            .encoding(encoding)
            .path(path)
            .relativeToChangelogFile(relativeToChangelogFile),
        SQLFileChange::class,
        *abstractSql,
        SQLFileChange::getEncoding to encoding,
        SQLFileChange::getPath to path,
        SQLFileChange::isRelativeToChangelogFile to relativeToChangelogFile
    )

    @Test
    fun stopTest() = testEvaluation(
        LkStop().message(message),
        StopChange::class,
        StopChange::getMessage to message
    )

    @Test
    fun tagDatabaseTest() = testEvaluation(
        LkTagDatabase().tag(tag),
        TagDatabaseChange::class,
        TagDatabaseChange::getTag to tag
    )

    @Test
    fun updateTest() = testEvaluation(
        LkUpdate()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .where(where),
//            .abstractModifyFields(), TODO Implement missed attribute
        UpdateDataChange::class,
//        *BundledDropChangesTest.abstractModifyFields, TODO Implement missed attribute
        UpdateDataChange::getColumns to emptyList<ColumnConfig>()
    )

}