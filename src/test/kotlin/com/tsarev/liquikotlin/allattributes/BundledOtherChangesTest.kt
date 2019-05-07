package com.tsarev.liquikotlin.allattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.util.*
import liquibase.change.ColumnConfig
import liquibase.change.core.*
import liquibase.util.StringUtils
import org.junit.Test

/**
 * Testing that basic evaluations process all attributes
 * correctly without modifications.
 */
class BundledOtherChangesTest : BaseLiquikotlinUnitTest() {

    @Test
    fun alterSequenceTest() = testEvaluation(
        LkAlterSequence()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .sequenceName(testSequenceName)
            .incrementBy(testIncrementBy)
            .maxValue(testMaxValue)
            .minValue(testMinValue)
            .ordered(testOrdered)
            .cacheSize(testCacheSize)
            .willCycle(testWillCycle),
        AlterSequenceChange::class,
        AlterSequenceChange::getCatalogName to testCatalogName,
        AlterSequenceChange::getSchemaName to testSchemaName,
        AlterSequenceChange::getSequenceName to testSequenceName,
        AlterSequenceChange::getIncrementBy to testIncrementBy,
        AlterSequenceChange::getMaxValue to testMaxValue,
        AlterSequenceChange::getMinValue to testMinValue,
        AlterSequenceChange::isOrdered to testOrdered,
        AlterSequenceChange::getCacheSize to testCacheSize,
        AlterSequenceChange::getWillCycle to testWillCycle
    )

    // TODO Add args testing
    @Test
    fun executeCommandTest() = testEvaluation(
        LkExecuteCommand()
            .executable(testExecutable)
            .os(testOs),
        ExecuteShellCommandChange::class,
        ExecuteShellCommandChange::getExecutable to testExecutable,
        ExecuteShellCommandChange::getOs to StringUtils.splitAndTrim(testOs, ","),
        ExecuteShellCommandChange::getArgs to emptyList<String>()
    )

    @Test
    fun insertTest() = testEvaluation(
        LkInsert()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .dbms(testDbms),
        InsertDataChange::class,
        InsertDataChange::getCatalogName to testCatalogName,
        InsertDataChange::getSchemaName to testSchemaName,
        InsertDataChange::getTableName to testTableName,
        InsertDataChange::getColumns to emptyList<ColumnConfig>(),
        InsertDataChange::getDbms to testDbms
    )

    @Test
    fun loadDataTest() = testEvaluation(
        LkLoadData()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .file(testFile)
            .relativeToChangelogFile(testRelativeToChangelogFile)
            .encoding(testEncoding),
        LoadDataChange::class,
        LoadDataChange::getCatalogName to testCatalogName,
        LoadDataChange::getSchemaName to testSchemaName,
        LoadDataChange::getTableName to testTableName,
        LoadDataChange::getFile to testFile,
        LoadDataChange::isRelativeToChangelogFile to testRelativeToChangelogFile,
        LoadDataChange::getEncoding to testEncoding,
        LoadDataChange::getSeparator to "${liquibase.util.csv.opencsv.CSVReader.DEFAULT_SEPARATOR}",
        LoadDataChange::getQuotchar to "${liquibase.util.csv.opencsv.CSVReader.DEFAULT_QUOTE_CHARACTER}",
        LoadDataChange::getColumns to emptyList<LoadDataColumnConfig>()
    )

    @Test
    fun loadUpdateDataTest() = testEvaluation(
        LkLoadUpdateData()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .file(testFile)
            .relativeToChangelogFile(testRelativeToChangelogFile)
            .primaryKey(testPrimaryKeyName)
            .encoding(testEncoding),
        LoadUpdateDataChange::class,
        LoadDataChange::getCatalogName to testCatalogName,
        LoadUpdateDataChange::getSchemaName to testSchemaName,
        LoadUpdateDataChange::getTableName to testTableName,
        LoadUpdateDataChange::getFile to testFile,
        LoadUpdateDataChange::isRelativeToChangelogFile to testRelativeToChangelogFile,
        LoadUpdateDataChange::getEncoding to testEncoding,
        LoadUpdateDataChange::getSeparator to "${liquibase.util.csv.opencsv.CSVReader.DEFAULT_SEPARATOR}",
        LoadUpdateDataChange::getQuotchar to "${liquibase.util.csv.opencsv.CSVReader.DEFAULT_QUOTE_CHARACTER}",
        LoadUpdateDataChange::getColumns to emptyList<LoadDataColumnConfig>(),
        LoadUpdateDataChange::getPrimaryKey to testPrimaryKeyName
    )

    @Test
    fun mergeColumnsTest() = testEvaluation(
        LkMergeColumns()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .column1Name(testColumn1Name)
            .joinString(testJoinString)
            .column2Name(testColumn2Name)
            .finalColumnName(testFinalColumnName)
            .finalColumnType(testFinalColumnType),
        MergeColumnChange::class,
        MergeColumnChange::getCatalogName to testCatalogName,
        MergeColumnChange::getSchemaName to testSchemaName,
        MergeColumnChange::getTableName to testTableName,
        MergeColumnChange::getColumn1Name to testColumn1Name,
        MergeColumnChange::getJoinString to testJoinString,
        MergeColumnChange::getColumn2Name to testColumn2Name,
        MergeColumnChange::getFinalColumnName to testFinalColumnName,
        MergeColumnChange::getFinalColumnType to testFinalColumnType
    )

    @Test
    fun modifyDataTypeTest() = testEvaluation(
        LkModifyDataType()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .columnName(testColumnName)
            .newDataType(testNewDataType),
        ModifyDataTypeChange::class,
        ModifyDataTypeChange::getCatalogName to testCatalogName,
        ModifyDataTypeChange::getSchemaName to testSchemaName,
        ModifyDataTypeChange::getTableName to testTableName,
        ModifyDataTypeChange::getColumnName to testColumnName,
        ModifyDataTypeChange::getNewDataType to testNewDataType
    )

    @Test
    fun renameColumnTest() = testEvaluation(
        LkRenameColumn()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .oldColumnName(testOldColumnName)
            .newColumnName(testNewColumnName)
            .columnDataType(testColumnType)
            .remarks(testRemarks),
        RenameColumnChange::class,
        RenameColumnChange::getCatalogName to testCatalogName,
        RenameColumnChange::getSchemaName to testSchemaName,
        RenameColumnChange::getTableName to testTableName,
        RenameColumnChange::getOldColumnName to testOldColumnName,
        RenameColumnChange::getNewColumnName to testNewColumnName,
        RenameColumnChange::getColumnDataType to testColumnType,
        RenameColumnChange::getRemarks to testRemarks
    )

    @Test
    fun renameTableTest() = testEvaluation(
        LkRenameTable()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .oldTableName(testOldTableName)
            .newTableName(testNewTableName),
        RenameTableChange::class,
        RenameTableChange::getCatalogName to testCatalogName,
        RenameTableChange::getSchemaName to testSchemaName,
        RenameTableChange::getOldTableName to testOldTableName,
        RenameTableChange::getNewTableName to testNewTableName
    )

    @Test
    fun renameViewTest() = testEvaluation(
        LkRenameView()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .oldViewName(testOldViewName)
            .newViewName(testNewViewName),
        RenameViewChange::class,
        RenameViewChange::getCatalogName to testCatalogName,
        RenameViewChange::getSchemaName to testSchemaName,
        RenameViewChange::getOldViewName to testOldViewName,
        RenameViewChange::getNewViewName to testNewViewName
    )

    @Test
    fun sqlTest() = testEvaluation(
        LkSql()
            .sql(testSql)
            .comment(testComment)
            .stripComments(testStripComments)
            .splitStatements(testSplitStatements)
            .endDelimiter(testEndDelimiter)
            .dbms(testDbms),
        RawSQLChange::class,
        *notNullSql,
        RawSQLChange::getSql to testSql,
        RawSQLChange::getComment to testComment
    )

    @Test
    fun sqlFileTest() = testEvaluation(
        LkSqlFile()
            .stripComments(testStripComments)
            .splitStatements(testSplitStatements)
            .endDelimiter(testEndDelimiter)
            .dbms(testDbms)
            .encoding(testEncoding)
            .path(testPath)
            .relativeToChangelogFile(testRelativeToChangelogFile),
        SQLFileChange::class,
        *notNullSql,
        SQLFileChange::getEncoding to testEncoding,
        SQLFileChange::getPath to testPath,
        SQLFileChange::isRelativeToChangelogFile to testRelativeToChangelogFile
    )

    @Test
    fun stopTest() = testEvaluation(
        LkStop().message(testMessage),
        StopChange::class,
        StopChange::getMessage to testMessage
    )

    @Test
    fun tagDatabaseTest() = testEvaluation(
        LkTagDatabase().tag(testTag),
        TagDatabaseChange::class,
        TagDatabaseChange::getTag to testTag
    )

    @Test
    fun updateTest() = testEvaluation(
        LkUpdate()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .where(testWhere)
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .where(testWhere),
        UpdateDataChange::class,
        *notNullModifyFields,
        UpdateDataChange::getColumns to emptyList<ColumnConfig>()
    )

}