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

    companion object {
        val abstractModifyFields = arrayOf(
            AbstractModifyDataChange::getCatalogName to catalogName,
            AbstractModifyDataChange::getSchemaName to schemaName,
            AbstractModifyDataChange::getTableName to tableName,
            AbstractModifyDataChange::getWhere to where,
            AbstractModifyDataChange::getWhereParams to emptyList<ColumnConfig>()
        )
    }

    @Test
    open fun deleteTest() = testEvaluation(
        LkDelete()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .where(where),
        DeleteDataChange::class,
        *abstractModifyFields
    )

    @Test
    open fun dropAllForeignKeyConstraintsTest() = testEvaluation(
        LkDropAllForeignKeyConstraints()
            .baseTableCatalogName(baseTableSchemaName)
            .baseTableSchemaName(baseTableSchemaName)
            .baseTableName(baseTableSchemaName),
        DropAllForeignKeyConstraintsChange::class,
        DropAllForeignKeyConstraintsChange::getBaseTableCatalogName to baseTableSchemaName,
        DropAllForeignKeyConstraintsChange::getBaseTableSchemaName to baseTableSchemaName,
        DropAllForeignKeyConstraintsChange::getBaseTableName to baseTableSchemaName
    )

    @Test
    open fun dropColumnTest() = testEvaluation(
        LkDropColumn()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .columnName(columnName),
        DropColumnChange::class,
        DropColumnChange::getCatalogName to catalogName,
        DropColumnChange::getSchemaName to schemaName,
        DropColumnChange::getTableName to tableName,
        DropColumnChange::getColumnName to columnName,
        DropColumnChange::getColumns to emptyList<ColumnConfig>()
    )

    @Test
    open fun dropDefaultValueTest() = testEvaluation(
        LkDropDefaultValue()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .columnName(columnName),
        DropDefaultValueChange::class,
        DropDefaultValueChange::getCatalogName to catalogName,
        DropDefaultValueChange::getSchemaName to schemaName,
        DropDefaultValueChange::getTableName to tableName,
        DropDefaultValueChange::getColumnName to columnName,
        DropDefaultValueChange::getColumnDataType
    )

    @Test
    open fun dropForeignKeyConstraintTest() = testEvaluation(
        LkDropForeignKeyConstraint()
            .baseTableCatalogName(baseTableCatalogName)
            .baseTableSchemaName(baseTableSchemaName)
            .baseTableName(baseTableName)
            .constraintName(constraintName),
        DropForeignKeyConstraintChange::class,
        DropForeignKeyConstraintChange::getBaseTableCatalogName to baseTableCatalogName,
        DropForeignKeyConstraintChange::getBaseTableSchemaName to baseTableSchemaName,
        DropForeignKeyConstraintChange::getBaseTableName to baseTableName,
        DropForeignKeyConstraintChange::getConstraintName to constraintName
    )

    @Test
    open fun dropIndexTest() = testEvaluation(
        LkDropIndex()
            .schemaName(schemaName)
            .indexName(indexName)
            .tableName(tableName)
//            .associatedWith(associatedWith) TODO Implement missed attribute
            .catalogName(catalogName),
        DropIndexChange::class,
        DropIndexChange::getSchemaName to schemaName,
        DropIndexChange::getIndexName to indexName,
        DropIndexChange::getTableName to tableName,
//        DropIndexChange::getAssociatedWith, TODO Implement missed attribute
        DropIndexChange::getCatalogName to catalogName
    )

    @Test
    open fun dropNotNullConstraintTest() = testEvaluation(
        LkDropNotNullConstraint()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .columnName(columnName)
            .columnDataType(columnType),
        DropNotNullConstraintChange::class,
        DropNotNullConstraintChange::getCatalogName to catalogName,
        DropNotNullConstraintChange::getSchemaName to schemaName,
        DropNotNullConstraintChange::getTableName to tableName,
        DropNotNullConstraintChange::getColumnName to columnName,
        DropNotNullConstraintChange::getColumnDataType to columnType
    )

    @Test
    open fun dropPrimaryKeyTest() = testEvaluation(
        LkDropPrimaryKey()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .constraintName(constraintName),
        DropPrimaryKeyChange::class,
        DropPrimaryKeyChange::getCatalogName to catalogName,
        DropPrimaryKeyChange::getSchemaName to schemaName,
        DropPrimaryKeyChange::getTableName to tableName,
        DropPrimaryKeyChange::getConstraintName to constraintName
    )

    @Test
    open fun dropProcedureTest() = testEvaluation(
        LkDropProcedure()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .procedureName(procedureName),
        DropProcedureChange::class,
        DropProcedureChange::getCatalogName to catalogName,
        DropProcedureChange::getSchemaName to schemaName,
        DropProcedureChange::getProcedureName to procedureName
    )

    @Test
    open fun dropSequenceTest() = testEvaluation(
        LkDropSequence()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .sequenceName(sequenceName),
        DropSequenceChange::class,
        DropSequenceChange::getCatalogName to catalogName,
        DropSequenceChange::getSchemaName to schemaName,
        DropSequenceChange::getSequenceName to sequenceName
    )

    @Test
    open fun dropTableTest() = testEvaluation(
        LkDropTable()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .cascadeConstraints(cascadeConstraints),
        DropTableChange::class,
        DropTableChange::getCatalogName to catalogName,
        DropTableChange::getSchemaName to schemaName,
        DropTableChange::getTableName to tableName,
        DropTableChange::isCascadeConstraints to cascadeConstraints
    )

    @Test
    open fun dropUniqueConstraintTest() = testEvaluation(
        LkDropUniqueConstraint()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .constraintName(constraintName),
        DropUniqueConstraintChange::class,
        DropUniqueConstraintChange::getCatalogName to catalogName,
        DropUniqueConstraintChange::getSchemaName to schemaName,
        DropUniqueConstraintChange::getTableName to tableName,
        DropUniqueConstraintChange::getConstraintName to constraintName
    )

    @Test
    open fun dropViewTest() = testEvaluation(
        LkDropView()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .viewName(viewName),
        DropViewChange::class,
        DropViewChange::getCatalogName to catalogName,
        DropViewChange::getSchemaName to schemaName,
        DropViewChange::getViewName to viewName
    )
}