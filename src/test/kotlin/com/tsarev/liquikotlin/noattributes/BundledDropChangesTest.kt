package com.tsarev.liquikotlin.noattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.*
import liquibase.change.ColumnConfig
import liquibase.change.core.*
import org.junit.Test

/**
 * Testing that basic evaluations do not add any
 * values except required ones.
 */
open class BundledDropChangesTest : BaseLiquikotlinUnitTest() {

    companion object {
        val abstractModifyFields = arrayOf(
            AbstractModifyDataChange::getCatalogName,
            AbstractModifyDataChange::getSchemaName,
            AbstractModifyDataChange::getTableName,
            AbstractModifyDataChange::getWhereParams to emptyList<ColumnConfig>(),
            AbstractModifyDataChange::getWhere
        )
    }

    @Test
    open fun deleteTest() = testEvaluation(
        LkDelete(),
        DeleteDataChange::class,
        *abstractModifyFields
    )

    @Test
    open fun dropAllForeignKeyConstraintsTest() = testEvaluation(
        LkDropAllForeignKeyConstraints(),
        DropAllForeignKeyConstraintsChange::class,
        DropAllForeignKeyConstraintsChange::getBaseTableCatalogName,
        DropAllForeignKeyConstraintsChange::getBaseTableSchemaName,
        DropAllForeignKeyConstraintsChange::getBaseTableName
    )

    @Test
    open fun dropColumnTest() = testEvaluation(
        LkDropColumn(),
        DropColumnChange::class,
        DropColumnChange::getCatalogName,
        DropColumnChange::getSchemaName,
        DropColumnChange::getTableName,
        DropColumnChange::getColumnName,
        DropColumnChange::getColumns to emptyList<ColumnConfig>()
    )

    @Test
    open fun dropDefaultValueTest() = testEvaluation(
        LkDropDefaultValue(),
        DropDefaultValueChange::class,
        DropDefaultValueChange::getCatalogName,
        DropDefaultValueChange::getSchemaName,
        DropDefaultValueChange::getTableName,
        DropDefaultValueChange::getColumnName,
        DropDefaultValueChange::getColumnDataType
    )

    @Test
    open fun dropForeignKeyConstraintTest() = testEvaluation(
        LkDropForeignKeyConstraint(),
        DropForeignKeyConstraintChange::class,
        DropForeignKeyConstraintChange::getBaseTableCatalogName,
        DropForeignKeyConstraintChange::getBaseTableSchemaName,
        DropForeignKeyConstraintChange::getBaseTableName,
        DropForeignKeyConstraintChange::getConstraintName
    )

    @Test
    open fun dropIndexTest() = testEvaluation(
        LkDropIndex(),
        DropIndexChange::class,
        DropIndexChange::getSchemaName,
        DropIndexChange::getIndexName,
        DropIndexChange::getTableName,
        DropIndexChange::getAssociatedWith,
        DropIndexChange::getCatalogName
    )

    @Test
    open fun dropNotNullConstraintTest() = testEvaluation(
        LkDropNotNullConstraint(),
        DropNotNullConstraintChange::class,
        DropNotNullConstraintChange::getCatalogName,
        DropNotNullConstraintChange::getSchemaName,
        DropNotNullConstraintChange::getTableName,
        DropNotNullConstraintChange::getColumnName,
        DropNotNullConstraintChange::getColumnDataType
    )

    @Test
    open fun dropPrimaryKeyTest() = testEvaluation(
        LkDropPrimaryKey(),
        DropPrimaryKeyChange::class,
        DropPrimaryKeyChange::getCatalogName,
        DropPrimaryKeyChange::getSchemaName,
        DropPrimaryKeyChange::getTableName,
        DropPrimaryKeyChange::getConstraintName
    )

    @Test
    open fun dropProcedureTest() = testEvaluation(
        LkDropProcedure(),
        DropProcedureChange::class,
        DropProcedureChange::getCatalogName,
        DropProcedureChange::getSchemaName,
        DropProcedureChange::getProcedureName
    )

    @Test
    open fun dropSequenceTest() = testEvaluation(
        LkDropSequence(),
        DropSequenceChange::class,
        DropSequenceChange::getCatalogName,
        DropSequenceChange::getSchemaName,
        DropSequenceChange::getSequenceName
    )

    @Test
    open fun dropTableTest() = testEvaluation(
        LkDropTable(),
        DropTableChange::class,
        DropTableChange::getCatalogName,
        DropTableChange::getSchemaName,
        DropTableChange::getTableName,
        DropTableChange::isCascadeConstraints
    )

    @Test
    open fun dropUniqueConstraintTest() = testEvaluation(
        LkDropUniqueConstraint(),
        DropUniqueConstraintChange::class,
        DropUniqueConstraintChange::getCatalogName,
        DropUniqueConstraintChange::getSchemaName,
        DropUniqueConstraintChange::getTableName,
        DropUniqueConstraintChange::getConstraintName
    )

    @Test
    open fun dropViewTest() = testEvaluation(
        LkDropView(),
        DropViewChange::class,
        DropViewChange::getCatalogName,
        DropViewChange::getSchemaName,
        DropViewChange::getViewName
    )
}