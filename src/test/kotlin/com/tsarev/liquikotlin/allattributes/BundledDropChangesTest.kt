package com.tsarev.liquikotlin.allattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.util.*
import liquibase.change.ColumnConfig
import liquibase.change.core.*
import org.junit.Test

/**
 * Testing that basic evaluations process all attributes
 * correctly without modifications.
 */
open class BundledDropChangesTest : BaseLiquikotlinUnitTest() {

    @Test
    open fun deleteTest() = testEvaluation(
        LkDelete()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .where(testWhere),
        DeleteDataChange::class,
        *notNullModifyFields
    )

    @Test
    open fun dropAllForeignKeyConstraintsTest() = testEvaluation(
        LkDropAllForeignKeyConstraints()
            .baseTableCatalogName(testBaseTableSchemaName)
            .baseTableSchemaName(testBaseTableSchemaName)
            .baseTableName(testBaseTableSchemaName),
        DropAllForeignKeyConstraintsChange::class,
        DropAllForeignKeyConstraintsChange::getBaseTableCatalogName to testBaseTableSchemaName,
        DropAllForeignKeyConstraintsChange::getBaseTableSchemaName to testBaseTableSchemaName,
        DropAllForeignKeyConstraintsChange::getBaseTableName to testBaseTableSchemaName
    )

    @Test
    open fun dropColumnTest() = testEvaluation(
        LkDropColumn()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .columnName(testColumnName),
        DropColumnChange::class,
        DropColumnChange::getCatalogName to testCatalogName,
        DropColumnChange::getSchemaName to testSchemaName,
        DropColumnChange::getTableName to testTableName,
        DropColumnChange::getColumnName to testColumnName,
        DropColumnChange::getColumns to emptyList<ColumnConfig>()
    )

    @Test
    open fun dropDefaultValueTest() = testEvaluation(
        LkDropDefaultValue()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .columnName(testColumnName),
        DropDefaultValueChange::class,
        DropDefaultValueChange::getCatalogName to testCatalogName,
        DropDefaultValueChange::getSchemaName to testSchemaName,
        DropDefaultValueChange::getTableName to testTableName,
        DropDefaultValueChange::getColumnName to testColumnName,
        DropDefaultValueChange::getColumnDataType
    )

    @Test
    open fun dropForeignKeyConstraintTest() = testEvaluation(
        LkDropForeignKeyConstraint()
            .baseTableCatalogName(testBaseTableCatalogName)
            .baseTableSchemaName(testBaseTableSchemaName)
            .baseTableName(testBaseTableName)
            .constraintName(testConstraintName),
        DropForeignKeyConstraintChange::class,
        DropForeignKeyConstraintChange::getBaseTableCatalogName to testBaseTableCatalogName,
        DropForeignKeyConstraintChange::getBaseTableSchemaName to testBaseTableSchemaName,
        DropForeignKeyConstraintChange::getBaseTableName to testBaseTableName,
        DropForeignKeyConstraintChange::getConstraintName to testConstraintName
    )

    @Test
    open fun dropIndexTest() = testEvaluation(
        LkDropIndex()
            .schemaName(testSchemaName)
            .indexName(testIndexName)
            .tableName(testTableName)
            .associatedWith(testAssociatedWith)
            .catalogName(testCatalogName),
        DropIndexChange::class,
        DropIndexChange::getSchemaName to testSchemaName,
        DropIndexChange::getIndexName to testIndexName,
        DropIndexChange::getTableName to testTableName,
        DropIndexChange::getAssociatedWith to testAssociatedWith,
        DropIndexChange::getCatalogName to testCatalogName
    )

    @Test
    open fun dropNotNullConstraintTest() = testEvaluation(
        LkDropNotNullConstraint()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .columnName(testColumnName)
            .columnDataType(testColumnType),
        DropNotNullConstraintChange::class,
        DropNotNullConstraintChange::getCatalogName to testCatalogName,
        DropNotNullConstraintChange::getSchemaName to testSchemaName,
        DropNotNullConstraintChange::getTableName to testTableName,
        DropNotNullConstraintChange::getColumnName to testColumnName,
        DropNotNullConstraintChange::getColumnDataType to testColumnType
    )

    @Test
    open fun dropPrimaryKeyTest() = testEvaluation(
        LkDropPrimaryKey()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .constraintName(testConstraintName),
        DropPrimaryKeyChange::class,
        DropPrimaryKeyChange::getCatalogName to testCatalogName,
        DropPrimaryKeyChange::getSchemaName to testSchemaName,
        DropPrimaryKeyChange::getTableName to testTableName,
        DropPrimaryKeyChange::getConstraintName to testConstraintName
    )

    @Test
    open fun dropProcedureTest() = testEvaluation(
        LkDropProcedure()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .procedureName(testProcedureName),
        DropProcedureChange::class,
        DropProcedureChange::getCatalogName to testCatalogName,
        DropProcedureChange::getSchemaName to testSchemaName,
        DropProcedureChange::getProcedureName to testProcedureName
    )

    @Test
    open fun dropSequenceTest() = testEvaluation(
        LkDropSequence()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .sequenceName(testSequenceName),
        DropSequenceChange::class,
        DropSequenceChange::getCatalogName to testCatalogName,
        DropSequenceChange::getSchemaName to testSchemaName,
        DropSequenceChange::getSequenceName to testSequenceName
    )

    @Test
    open fun dropTableTest() = testEvaluation(
        LkDropTable()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .cascadeConstraints(testCascadeConstraints),
        DropTableChange::class,
        DropTableChange::getCatalogName to testCatalogName,
        DropTableChange::getSchemaName to testSchemaName,
        DropTableChange::getTableName to testTableName,
        DropTableChange::isCascadeConstraints to testCascadeConstraints
    )

    @Test
    open fun dropUniqueConstraintTest() = testEvaluation(
        LkDropUniqueConstraint()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .constraintName(testConstraintName),
        DropUniqueConstraintChange::class,
        DropUniqueConstraintChange::getCatalogName to testCatalogName,
        DropUniqueConstraintChange::getSchemaName to testSchemaName,
        DropUniqueConstraintChange::getTableName to testTableName,
        DropUniqueConstraintChange::getConstraintName to testConstraintName
    )

    @Test
    open fun dropViewTest() = testEvaluation(
        LkDropView()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .viewName(testViewName),
        DropViewChange::class,
        DropViewChange::getCatalogName to testCatalogName,
        DropViewChange::getSchemaName to testSchemaName,
        DropViewChange::getViewName to testViewName
    )
}