package com.tsarev.liquikotlin.noattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.util.sql
import liquibase.change.AbstractSQLChange
import liquibase.change.ColumnConfig
import liquibase.change.core.*
import org.junit.Test

/**
 * Testing that basic evaluations do not add any
 * values except required ones.
 */
class BundledOtherChangesTest : BaseLiquikotlinUnitTest() {

    companion object {
        val abstractSql = arrayOf(
            AbstractSQLChange::isStripComments to false,
            AbstractSQLChange::isSplitStatements to true,
            AbstractSQLChange::getEndDelimiter,
            AbstractSQLChange::getDbms
        )
    }

    @Test
    fun alterSequenceTest() = testEvaluation(
        LkAlterSequence(),
        AlterSequenceChange::class,
        AlterSequenceChange::getCatalogName,
        AlterSequenceChange::getSchemaName,
        AlterSequenceChange::getSequenceName,
        AlterSequenceChange::getIncrementBy,
        AlterSequenceChange::getMaxValue,
        AlterSequenceChange::getMinValue,
        AlterSequenceChange::isOrdered,
        AlterSequenceChange::getCacheSize,
        AlterSequenceChange::getWillCycle
    )

    @Test
    fun emptyTest() = testEvaluation(
        LkEmpty(),
        EmptyChange::class
    )

    @Test
    fun executeCommandTest() = testEvaluation(
        LkExecuteCommand(),
        ExecuteShellCommandChange::class,
        ExecuteShellCommandChange::getExecutable,
        ExecuteShellCommandChange::getOs,
        ExecuteShellCommandChange::getArgs to emptyList<String>()
    )

    @Test
    fun insertTest() = testEvaluation(
        LkInsert(),
        InsertDataChange::class,
        InsertDataChange::getCatalogName,
        InsertDataChange::getSchemaName,
        InsertDataChange::getTableName,
        InsertDataChange::getColumns to emptyList<ColumnConfig>(),
        InsertDataChange::getDbms
    )

    @Test
    fun loadDataTest() = testEvaluation(
        LkLoadData(),
        LoadDataChange::class,
        LoadDataChange::getCatalogName,
        LoadDataChange::getSchemaName,
        LoadDataChange::getTableName,
        LoadDataChange::getFile,
        LoadDataChange::isRelativeToChangelogFile,
        LoadDataChange::getEncoding,
        LoadDataChange::getSeparator to liquibase.util.csv.opencsv.CSVReader.DEFAULT_SEPARATOR + "",
        LoadDataChange::getQuotchar to liquibase.util.csv.opencsv.CSVReader.DEFAULT_QUOTE_CHARACTER + "",
        LoadDataChange::getColumns to emptyList<LoadDataColumnConfig>()
    )

    @Test
    fun loadUpdateDataTest() = testEvaluation(
        LkUpdate(),
        UpdateDataChange::class,
        *BundledDropChangesTest.abstractModifyFields,
        UpdateDataChange::getColumns to emptyList<ColumnConfig>()
    )

    @Test
    fun mergeColumnsTest() = testEvaluation(
        LkMergeColumns(),
        MergeColumnChange::class,
        MergeColumnChange::getCatalogName,
        MergeColumnChange::getSchemaName,
        MergeColumnChange::getTableName,
        MergeColumnChange::getColumn1Name,
        MergeColumnChange::getJoinString,
        MergeColumnChange::getColumn2Name,
        MergeColumnChange::getFinalColumnName,
        MergeColumnChange::getFinalColumnType
    )

    @Test
    fun modifyDataTypeTest() = testEvaluation(
        LkModifyDataType(),
        ModifyDataTypeChange::class,
        ModifyDataTypeChange::getCatalogName,
        ModifyDataTypeChange::getSchemaName,
        ModifyDataTypeChange::getTableName,
        ModifyDataTypeChange::getColumnName,
        ModifyDataTypeChange::getNewDataType
    )

    @Test
    fun renameColumnTest() = testEvaluation(
        LkRenameColumn(),
        RenameColumnChange::class,
        RenameColumnChange::getCatalogName,
        RenameColumnChange::getSchemaName,
        RenameColumnChange::getTableName,
        RenameColumnChange::getOldColumnName,
        RenameColumnChange::getNewColumnName,
        RenameColumnChange::getColumnDataType,
        RenameColumnChange::getRemarks
    )

    @Test
    fun renameTableTest() = testEvaluation(
        LkRenameTable(),
        RenameTableChange::class,
        RenameTableChange::getCatalogName,
        RenameTableChange::getSchemaName,
        RenameTableChange::getOldTableName,
        RenameTableChange::getNewTableName
    )

    @Test
    fun renameViewTest() = testEvaluation(
        LkRenameView(),
        RenameViewChange::class,
        RenameViewChange::getCatalogName,
        RenameViewChange::getSchemaName,
        RenameViewChange::getOldViewName,
        RenameViewChange::getNewViewName
    )

    @Test
    fun sqlTest() = testEvaluation(
        LkSql().sql(sql),
        RawSQLChange::class,
        *abstractSql,
        RawSQLChange::getSql to sql,
        RawSQLChange::getComment
    )

    @Test
    fun sqlFileTest() = testEvaluation(
        LkSqlFile(),
        SQLFileChange::class,
        *abstractSql,
        SQLFileChange::getEncoding,
        SQLFileChange::getPath,
        SQLFileChange::isRelativeToChangelogFile
    )

    @Test
    fun stopTest() = testEvaluation(
        LkStop(),
        StopChange::class,
        StopChange::getMessage to StopChange().message
    )

    @Test
    fun tagDatabaseTest() = testEvaluation(
        LkTagDatabase(),
        TagDatabaseChange::class,
        TagDatabaseChange::getTag
    )

    @Test
    fun updateTest() = testEvaluation(
        LkUpdate(),
        UpdateDataChange::class,
        *BundledDropChangesTest.abstractModifyFields,
        UpdateDataChange::getColumns to emptyList<ColumnConfig>()
    )

}