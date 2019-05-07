package com.tsarev.liquikotlin.allattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.util.*
import liquibase.precondition.CustomPreconditionWrapper
import liquibase.precondition.core.*
import org.junit.Test

/**
 * Testing that basic evaluations process all attributes
 * correctly without modifications.
 */
class PreconditionsTest : BaseLiquikotlinUnitTest() {

    @Test
    fun preconditionTest() = testEvaluation(
        LkPrecondition()
            .onFail("$testOnFail")
            .onError("$testOnError")
            .onSqlOutput("$testOnSqlOutput")
            .onFailMessage(testOnFailMessage)
            .onErrorMessage(testOnErrorMessage),
        PreconditionContainer::class,
        PreconditionContainer::getOnFail to testOnFail,
        PreconditionContainer::getOnError to testOnError,
        PreconditionContainer::getOnSqlOutput to testOnSqlOutput,
        PreconditionContainer::getOnFailMessage to testOnFailMessage,
        PreconditionContainer::getOnErrorMessage to testOnErrorMessage
    )

    @Test
    fun dbmsPreconditionTest() = testEvaluation(
        LkDbmsPrecondition().type(testDbms),
        DBMSPrecondition::class,
        DBMSPrecondition::getType to testDbms.toLowerCase()
    )

    @Test
    fun runningAsPreconditionTest() = testEvaluation(
        LkRunningAsPrecondition().username(testUsername),
        RunningAsPrecondition::class,
        RunningAsPrecondition::getUsername to testUsername
    )

    @Test
    fun columnExistsPreconditionTest() = testEvaluation(
        LkColumnExistsPrecondition()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .columnName(testColumnName),
        ColumnExistsPrecondition::class,
        ColumnExistsPrecondition::getCatalogName to testCatalogName,
        ColumnExistsPrecondition::getSchemaName to testSchemaName,
        ColumnExistsPrecondition::getTableName to testTableName,
        ColumnExistsPrecondition::getColumnName to testColumnName
    )

    @Test
    fun tableExistsPreconditionTest() = testEvaluation(
        LkTableExistsPrecondition()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName),
        TableExistsPrecondition::class,
        TableExistsPrecondition::getCatalogName to testCatalogName,
        TableExistsPrecondition::getSchemaName to testSchemaName,
        TableExistsPrecondition::getTableName to testTableName
    )

    @Test
    fun viewExistsPreconditionTest() = testEvaluation(
        LkViewExistsPrecondition()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .viewName(testViewName),
        ViewExistsPrecondition::class,
        ViewExistsPrecondition::getCatalogName to testCatalogName,
        ViewExistsPrecondition::getSchemaName to testSchemaName,
        ViewExistsPrecondition::getViewName to testViewName
    )

    @Test
    fun foreignKeyConstraintExistsPreconditionTest() = testEvaluation(
        LkForeignKeyConstraintExistsPrecondition()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .foreignKeyTableName(testForeignKeyTableName)
            .foreignKeyName(testForeignKeyName),
        ForeignKeyExistsPrecondition::class,
        ForeignKeyExistsPrecondition::getCatalogName to testCatalogName,
        ForeignKeyExistsPrecondition::getSchemaName to testSchemaName,
        ForeignKeyExistsPrecondition::getForeignKeyTableName to testForeignKeyTableName,
        ForeignKeyExistsPrecondition::getForeignKeyName to testForeignKeyName
    )

    @Test
    fun indexExistsPreconditionTest() = testEvaluation(
        LkIndexExistsPrecondition()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .indexName(testIndexName)
            .columnNames(testColumnNames),
        IndexExistsPrecondition::class,
        IndexExistsPrecondition::getCatalogName to testCatalogName,
        IndexExistsPrecondition::getSchemaName to testSchemaName,
        IndexExistsPrecondition::getTableName to testTableName,
        IndexExistsPrecondition::getIndexName to testIndexName,
        IndexExistsPrecondition::getColumnNames to testColumnNames
    )

    @Test
    fun sequenceExistsPreconditionTest() = testEvaluation(
        LkSequenceExistsPrecondition()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .sequenceName(testSequenceName),
        SequenceExistsPrecondition::class,
        SequenceExistsPrecondition::getCatalogName to testCatalogName,
        SequenceExistsPrecondition::getSchemaName to testSchemaName,
        SequenceExistsPrecondition::getSequenceName to testSequenceName
    )

    @Test
    fun primaryKeyExistsPreconditionTest() = testEvaluation(
        LkPrimaryKeyExistsPrecondition()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .primaryKeyName(testPrimaryKeyName)
            .tableName(testTableName),
        PrimaryKeyExistsPrecondition::class,
        PrimaryKeyExistsPrecondition::getCatalogName to testCatalogName,
        PrimaryKeyExistsPrecondition::getSchemaName to testSchemaName,
        PrimaryKeyExistsPrecondition::getPrimaryKeyName to testPrimaryKeyName,
        PrimaryKeyExistsPrecondition::getTableName to testTableName
    )

    @Test
    fun sqlCheckPreconditionTest() = testEvaluation(
        LkSqlCheckPrecondition()
            .expectedResult(testExpectedResult)
            .sql(testSql),
        SqlPrecondition::class,
        SqlPrecondition::getExpectedResult to testExpectedResult,
        SqlPrecondition::getSql to testSql
    )

    @Test
    fun changeLogPropertyDefinedPreconditionTest() = testEvaluation(
        LkChangeLogPropertyDefinedPrecondition()
            .property(testProperty)
            .value(testValue),
        ChangeLogPropertyDefinedPrecondition::class,
        ChangeLogPropertyDefinedPrecondition::getProperty to testProperty,
        ChangeLogPropertyDefinedPrecondition::getValue to testValue
    )

    @Test
    fun customPreconditionTest() = testEvaluation(
        LkCustomPrecondition()(testClassName, testProperty to testValue),
        CustomPreconditionWrapper::class,
        CustomPreconditionWrapper::getClassName to testClassName,
        { wrapper: CustomPreconditionWrapper -> wrapper.getParamValue(testProperty) } to testValue
    )

}