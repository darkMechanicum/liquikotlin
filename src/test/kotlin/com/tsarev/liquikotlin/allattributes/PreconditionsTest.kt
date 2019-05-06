package com.tsarev.liquikotlin.allattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.util.*
import liquibase.precondition.CustomPreconditionWrapper
import liquibase.precondition.core.*
import org.junit.Ignore
import org.junit.Test

/**
 * Testing that basic evaluations process all attributes
 * correctly without modifications.
 */
class PreconditionsTest : BaseLiquikotlinUnitTest() {

    @Test
    fun preconditionTest() = testEvaluation(
        LkPrecondition()
            .onFail("$onFail")
            .onError("$onError")
            .onSqlOutput("$onSqlOutput")
            .onFailMessage(onFailMessage)
            .onErrorMessage(onErrorMessage),
        PreconditionContainer::class,
        PreconditionContainer::getOnFail to onFail,
        PreconditionContainer::getOnError to onError,
        PreconditionContainer::getOnSqlOutput to onSqlOutput,
        PreconditionContainer::getOnFailMessage to onFailMessage,
        PreconditionContainer::getOnErrorMessage to onErrorMessage
    )

    // TODO Implement
    @Test
    @Ignore
    fun andPreconditionTest() { }

    // TODO Implement
    @Test
    @Ignore
    fun orPreconditionTest() { }

    @Test
    fun dbmsPreconditionTest() = testEvaluation(
        LkDbmsPrecondition().type(dbms),
        DBMSPrecondition::class,
        DBMSPrecondition::getType to dbms.toLowerCase()
    )

    @Test
    fun runningAsPreconditionTest() = testEvaluation(
        LkRunningAsPrecondition().username(username),
        RunningAsPrecondition::class,
        RunningAsPrecondition::getUsername to username
    )

    @Test
    fun columnExistsPreconditionTest() = testEvaluation(
        LkColumnExistsPrecondition()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .columnName(columnName),
        ColumnExistsPrecondition::class,
        ColumnExistsPrecondition::getCatalogName to catalogName,
        ColumnExistsPrecondition::getSchemaName to schemaName,
        ColumnExistsPrecondition::getTableName to tableName,
        ColumnExistsPrecondition::getColumnName to columnName
    )

    @Test
    fun tableExistsPreconditionTest() = testEvaluation(
        LkTableExistsPrecondition()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName),
        TableExistsPrecondition::class,
        TableExistsPrecondition::getCatalogName to catalogName,
        TableExistsPrecondition::getSchemaName to schemaName,
        TableExistsPrecondition::getTableName to tableName
    )

    @Test
    fun viewExistsPreconditionTest() = testEvaluation(
        LkViewExistsPrecondition()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .viewName(viewName),
        ViewExistsPrecondition::class,
        ViewExistsPrecondition::getCatalogName to catalogName,
        ViewExistsPrecondition::getSchemaName to schemaName,
        ViewExistsPrecondition::getViewName to viewName
    )

    @Test
    fun foreignKeyConstraintExistsPreconditionTest() = testEvaluation(
        LkForeignKeyConstraintExistsPrecondition()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .foreignKeyTableName(foreignKeyTableName)
            .foreignKeyName(foreignKeyName),
        ForeignKeyExistsPrecondition::class,
        ForeignKeyExistsPrecondition::getCatalogName to catalogName,
        ForeignKeyExistsPrecondition::getSchemaName to schemaName,
        ForeignKeyExistsPrecondition::getForeignKeyTableName to foreignKeyTableName,
        ForeignKeyExistsPrecondition::getForeignKeyName to foreignKeyName
    )

    @Test
    fun indexExistsPreconditionTest() = testEvaluation(
        LkIndexExistsPrecondition()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .indexName(indexName),
//            .columnNames(columnNames), TODO Implement missed attribute
        IndexExistsPrecondition::class,
        IndexExistsPrecondition::getCatalogName to catalogName,
        IndexExistsPrecondition::getSchemaName to schemaName,
        IndexExistsPrecondition::getTableName to tableName,
        IndexExistsPrecondition::getIndexName to indexName
//        IndexExistsPrecondition::getColumnNames TODO Implement missed attribute
    )

    @Test
    fun sequenceExistsPreconditionTest() = testEvaluation(
        LkSequenceExistsPrecondition()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .sequenceName(sequenceName),
        SequenceExistsPrecondition::class,
        SequenceExistsPrecondition::getCatalogName to catalogName,
        SequenceExistsPrecondition::getSchemaName to schemaName,
        SequenceExistsPrecondition::getSequenceName to sequenceName
    )

    @Test
    fun primaryKeyExistsPreconditionTest() = testEvaluation(
        LkPrimaryKeyExistsPrecondition()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .primaryKeyName(primaryKeyName)
            .tableName(tableName),
        PrimaryKeyExistsPrecondition::class,
        PrimaryKeyExistsPrecondition::getCatalogName to catalogName,
        PrimaryKeyExistsPrecondition::getSchemaName to schemaName,
        PrimaryKeyExistsPrecondition::getPrimaryKeyName to primaryKeyName,
        PrimaryKeyExistsPrecondition::getTableName to tableName
    )

    @Test
    fun sqlCheckPreconditionTest() = testEvaluation(
        LkSqlCheckPrecondition()
            .expectedResult(expectedResult)
            .sql(sql),
        SqlPrecondition::class,
        SqlPrecondition::getExpectedResult to expectedResult,
        SqlPrecondition::getSql to sql
    )

    @Test
    fun changeLogPropertyDefinedPreconditionTest() = testEvaluation(
        LkChangeLogPropertyDefinedPrecondition()
            .property(property)
            .value(value),
        ChangeLogPropertyDefinedPrecondition::class,
        ChangeLogPropertyDefinedPrecondition::getProperty to property,
        ChangeLogPropertyDefinedPrecondition::getValue to value
    )

    @Test
    fun customPreconditionTest() = testEvaluation(
        LkCustomPrecondition().className(className),
        CustomPreconditionWrapper::class,
        CustomPreconditionWrapper::getClassName to className
        // TODO Add child check
    )

}