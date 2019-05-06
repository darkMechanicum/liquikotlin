package com.tsarev.liquikotlin.noattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.util.primaryKeyName
import liquibase.precondition.CustomPreconditionWrapper
import liquibase.precondition.core.*
import org.junit.Ignore
import org.junit.Test

class PreconditionsTest : BaseLiquikotlinUnitTest() {

    @Test
    fun preconditionTest() = testEvaluation(
        LkPrecondition(),
        PreconditionContainer::class,
        PreconditionContainer::getOnFail to PreconditionContainer.FailOption.HALT,
        PreconditionContainer::getOnError to PreconditionContainer.ErrorOption.HALT,
        PreconditionContainer::getOnSqlOutput to PreconditionContainer.OnSqlOutputOption.IGNORE,
        PreconditionContainer::getOnFailMessage,
        PreconditionContainer::getOnErrorMessage
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
        LkDbmsPrecondition(),
        DBMSPrecondition::class,
        DBMSPrecondition::getType
    )

    @Test
    fun runningAsPreconditionTest() = testEvaluation(
        LkRunningAsPrecondition(),
        RunningAsPrecondition::class,
        RunningAsPrecondition::getUsername to RunningAsPrecondition().username
    )

    @Test
    fun columnExistsPreconditionTest() = testEvaluation(
        LkColumnExistsPrecondition(),
        ColumnExistsPrecondition::class,
        ColumnExistsPrecondition::getCatalogName,
        ColumnExistsPrecondition::getSchemaName,
        ColumnExistsPrecondition::getTableName,
        ColumnExistsPrecondition::getColumnName
    )

    @Test
    fun tableExistsPreconditionTest() = testEvaluation(
        LkTableExistsPrecondition(),
        TableExistsPrecondition::class,
        TableExistsPrecondition::getCatalogName,
        TableExistsPrecondition::getSchemaName,
        TableExistsPrecondition::getTableName
    )

    @Test
    fun viewExistsPreconditionTest() = testEvaluation(
        LkViewExistsPrecondition(),
        ViewExistsPrecondition::class,
        ViewExistsPrecondition::getCatalogName,
        ViewExistsPrecondition::getSchemaName,
        ViewExistsPrecondition::getViewName
    )

    @Test
    fun foreignKeyConstraintExistsPreconditionTest() = testEvaluation(
        LkForeignKeyConstraintExistsPrecondition(),
        ForeignKeyExistsPrecondition::class,
        ForeignKeyExistsPrecondition::getCatalogName,
        ForeignKeyExistsPrecondition::getSchemaName,
        ForeignKeyExistsPrecondition::getForeignKeyTableName,
        ForeignKeyExistsPrecondition::getForeignKeyName
    )

    @Test
    fun indexExistsPreconditionTest() = testEvaluation(
        LkIndexExistsPrecondition(),
        IndexExistsPrecondition::class,
        IndexExistsPrecondition::getCatalogName,
        IndexExistsPrecondition::getSchemaName,
        IndexExistsPrecondition::getTableName,
        IndexExistsPrecondition::getIndexName,
        IndexExistsPrecondition::getColumnNames
    )

    @Test
    fun sequenceExistsPreconditionTest() = testEvaluation(
        LkSequenceExistsPrecondition(),
        SequenceExistsPrecondition::class,
        SequenceExistsPrecondition::getCatalogName,
        SequenceExistsPrecondition::getSchemaName,
        SequenceExistsPrecondition::getSequenceName
    )

    @Test
    fun primaryKeyExistsPreconditionTest() = testEvaluation(
        LkPrimaryKeyExistsPrecondition().primaryKeyName(primaryKeyName),
        PrimaryKeyExistsPrecondition::class,
        PrimaryKeyExistsPrecondition::getCatalogName,
        PrimaryKeyExistsPrecondition::getSchemaName,
        PrimaryKeyExistsPrecondition::getPrimaryKeyName to primaryKeyName,
        PrimaryKeyExistsPrecondition::getTableName
    )

    @Test
    fun sqlCheckPreconditionTest() = testEvaluation(
        LkSqlCheckPrecondition(),
        SqlPrecondition::class,
        SqlPrecondition::getExpectedResult,
        SqlPrecondition::getSql
    )

    @Test
    fun changeLogPropertyDefinedPreconditionTest() = testEvaluation(
        LkChangeLogPropertyDefinedPrecondition(),
        ChangeLogPropertyDefinedPrecondition::class,
        ChangeLogPropertyDefinedPrecondition::getProperty,
        ChangeLogPropertyDefinedPrecondition::getValue
    )

    @Test
    fun customPreconditionTest() = testEvaluation(
        LkCustomPrecondition(),
        CustomPreconditionWrapper::class,
        CustomPreconditionWrapper::getClassName,
        CustomPreconditionWrapper::getClassLoader
    )

}