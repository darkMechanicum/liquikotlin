package com.tsarev.liquikotlin.noattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.util.testColumnName
import com.tsarev.liquikotlin.util.testTableName
import liquibase.change.AddColumnConfig
import liquibase.change.ColumnConfig
import liquibase.change.ConstraintsConfig
import liquibase.change.core.*
import org.junit.Test

/**
 * Testing that basic evaluations do not add any
 * values except required ones.
 */
open class BundledCreateChangesTest : BaseLiquikotlinUnitTest() {

    companion object {
        private val columnConfigFields = arrayOf(
            ColumnConfig::getName,
            ColumnConfig::getComputed,
            ColumnConfig::getType,
            ColumnConfig::getValue,
            ColumnConfig::getValueNumeric,
            ColumnConfig::getValueDate,
            ColumnConfig::getValueBoolean,
            ColumnConfig::getValueBlobFile,
            ColumnConfig::getValueClobFile,
            ColumnConfig::getEncoding,
            ColumnConfig::getValueComputed,
            ColumnConfig::getValueSequenceNext,
            ColumnConfig::getValueSequenceCurrent,
            ColumnConfig::getDefaultValue,
            ColumnConfig::getDefaultValueNumeric,
            ColumnConfig::getDefaultValueDate,
            ColumnConfig::getDefaultValueBoolean,
            ColumnConfig::getDefaultValueComputed,
            ColumnConfig::getDefaultValueSequenceNext,
            ColumnConfig::getConstraints,
            ColumnConfig::isAutoIncrement,
            ColumnConfig::getStartWith,
            ColumnConfig::getIncrementBy,
            ColumnConfig::getRemarks,
            ColumnConfig::getDescending
        )
    }

    @Test
    fun addColumnConfigTest() = testEvaluation(
        LkAddColumnConfig(),
        AddColumnConfig::class,
        AddColumnConfig::getAfterColumn,
        AddColumnConfig::getBeforeColumn,
        AddColumnConfig::getPosition,
        *columnConfigFields
    )

    @Test
    fun loadColumnConfigTest() = testEvaluation(
        LkLoadColumnConfig(),
        LoadDataColumnConfig::class,
        LoadDataColumnConfig::getIndex,
        LoadDataColumnConfig::getHeader,
        *columnConfigFields
    )

    @Test
    fun commonColumnConfigTest() = testEvaluation(
        LkCommonColumnConfig(),
        ColumnConfig::class,
        * columnConfigFields
    )

    @Test
    fun addAutoIncrementTest() = testEvaluation(
        LkAddAutoIncrement()
            .columnName(testColumnName)
            .tableName(testTableName),
        AddAutoIncrementChange::class,
        AddAutoIncrementChange::getCatalogName,
        AddAutoIncrementChange::getSchemaName,
        AddAutoIncrementChange::getTableName to testTableName,
        AddAutoIncrementChange::getColumnName to testColumnName,
        AddAutoIncrementChange::getColumnDataType,
        AddAutoIncrementChange::getStartWith,
        AddAutoIncrementChange::getIncrementBy
    )

    @Test
    fun addColumnTest() = testEvaluation(
        LkAddColumn().tableName(testTableName),
        AddColumnChange::class,
        AddColumnChange::getCatalogName,
        AddColumnChange::getSchemaName,
        AddColumnChange::getTableName to testTableName,
        AddColumnChange::getColumns to emptyList<AddColumnConfig>()
    )

    @Test
    fun constraintsTest() = testEvaluation(
        LkConstraints(),
        ConstraintsConfig::class,
        ConstraintsConfig::isNullable,
        ConstraintsConfig::isPrimaryKey,
        ConstraintsConfig::getPrimaryKeyName,
        ConstraintsConfig::getPrimaryKeyTablespace,
        ConstraintsConfig::getReferences,
        ConstraintsConfig::getReferencedTableName,
        ConstraintsConfig::getReferencedColumnNames,
        ConstraintsConfig::isUnique,
        ConstraintsConfig::getUniqueConstraintName,
        ConstraintsConfig::getCheckConstraint,
        ConstraintsConfig::isDeleteCascade,
        ConstraintsConfig::getForeignKeyName,
        ConstraintsConfig::isInitiallyDeferred,
        ConstraintsConfig::isDeferrable
    )

    @Test
    fun addDefaultValueTest() = testEvaluation(
        LkAddDefaultValue(),
        AddDefaultValueChange::class,
        AddDefaultValueChange::getCatalogName,
        AddDefaultValueChange::getSchemaName,
        AddDefaultValueChange::getTableName,
        AddDefaultValueChange::getColumnName,
        AddDefaultValueChange::getColumnDataType,
        AddDefaultValueChange::getDefaultValue,
        AddDefaultValueChange::getDefaultValueNumeric,
        AddDefaultValueChange::getDefaultValueDate,
        AddDefaultValueChange::getDefaultValueBoolean,
        AddDefaultValueChange::getDefaultValueComputed,
        AddDefaultValueChange::getDefaultValueSequenceNext
    )

    @Test
    fun addForeignKeyConstraintTest() = testEvaluation(
        LkAddForeignKeyConstraint(),
        AddForeignKeyConstraintChange::class,
        AddForeignKeyConstraintChange::getBaseTableCatalogName,
        AddForeignKeyConstraintChange::getBaseTableSchemaName,
        AddForeignKeyConstraintChange::getBaseTableName,
        AddForeignKeyConstraintChange::getBaseColumnNames,
        AddForeignKeyConstraintChange::getReferencedTableCatalogName,
        AddForeignKeyConstraintChange::getReferencedTableSchemaName,
        AddForeignKeyConstraintChange::getReferencedTableName,
        AddForeignKeyConstraintChange::getReferencedColumnNames,
        AddForeignKeyConstraintChange::getConstraintName,
        AddForeignKeyConstraintChange::getDeferrable,
        AddForeignKeyConstraintChange::getInitiallyDeferred,
        AddForeignKeyConstraintChange::getOnUpdate,
        AddForeignKeyConstraintChange::getOnDelete
    )


    @Test
    fun addLookupTableTest() = testEvaluation(
        LkAddLookupTable(),
        AddLookupTableChange::class,
        AddLookupTableChange::getExistingTableCatalogName,
        AddLookupTableChange::getExistingTableSchemaName,
        AddLookupTableChange::getExistingTableName,
        AddLookupTableChange::getExistingColumnName,
        AddLookupTableChange::getNewTableCatalogName,
        AddLookupTableChange::getNewTableSchemaName,
        AddLookupTableChange::getNewTableName,
        AddLookupTableChange::getNewColumnName,
        AddLookupTableChange::getNewColumnDataType,
        AddLookupTableChange::getConstraintName
    )

    @Test
    fun addNotNullConstraintTest() = testEvaluation(
        LkAddNotNullConstraint(),
        AddNotNullConstraintChange::class,
        AddNotNullConstraintChange::getCatalogName,
        AddNotNullConstraintChange::getSchemaName,
        AddNotNullConstraintChange::getTableName,
        AddNotNullConstraintChange::getColumnName,
        AddNotNullConstraintChange::getDefaultNullValue,
        AddNotNullConstraintChange::getColumnDataType,
        AddNotNullConstraintChange::getConstraintName
    )

    @Test
    fun addPrimaryKeyTest() = testEvaluation(
        LkAddPrimaryKey(),
        AddPrimaryKeyChange::class,
        AddPrimaryKeyChange::getCatalogName,
        AddPrimaryKeyChange::getSchemaName,
        AddPrimaryKeyChange::getTableName,
        AddPrimaryKeyChange::getTablespace,
        AddPrimaryKeyChange::getColumnNames,
        AddPrimaryKeyChange::getConstraintName,
        AddPrimaryKeyChange::getClustered,
        AddPrimaryKeyChange::getForIndexName,
        AddPrimaryKeyChange::getForIndexSchemaName,
        AddPrimaryKeyChange::getForIndexCatalogName
    )

    @Test
    fun addUniqueConstraintTest() = testEvaluation(
        LkAddUniqueConstraint(),
        AddUniqueConstraintChange::class,
        AddUniqueConstraintChange::getCatalogName,
        AddUniqueConstraintChange::getSchemaName,
        AddUniqueConstraintChange::getTableName,
        AddUniqueConstraintChange::getColumnNames,
        AddUniqueConstraintChange::getConstraintName,
        AddUniqueConstraintChange::getTablespace,
        AddUniqueConstraintChange::getDeferrable,
        AddUniqueConstraintChange::getInitiallyDeferred,
        AddUniqueConstraintChange::getDisabled,
        AddUniqueConstraintChange::getForIndexName,
        AddUniqueConstraintChange::getForIndexSchemaName,
        AddUniqueConstraintChange::getForIndexCatalogName
    )

    @Test
    fun createIndexTest() = testEvaluation(
        LkCreateIndex(),
        CreateIndexChange::class,
        CreateIndexChange::getCatalogName,
        CreateIndexChange::getSchemaName,
        CreateIndexChange::getTableName,
        CreateIndexChange::getIndexName,
        CreateIndexChange::isUnique,
        CreateIndexChange::getTablespace,
        CreateIndexChange::getColumns to emptyList<CreateIndexChange>()
    )

    @Test
    fun createProcedureTest() = testEvaluation(
        LkCreateProcedure(),
        CreateProcedureChange::class,
        CreateProcedureChange::getComments,
        CreateProcedureChange::getCatalogName,
        CreateProcedureChange::getSchemaName,
        CreateProcedureChange::getProcedureName,
        CreateProcedureChange::getProcedureText,
        CreateProcedureChange::getDbms,
        CreateProcedureChange::getPath,
        CreateProcedureChange::isRelativeToChangelogFile,
        CreateProcedureChange::getEncoding,
        CreateProcedureChange::getReplaceIfExists
    )

    @Test
    fun createSequenceTest() = testEvaluation(
        LkCreateSequence(),
        CreateSequenceChange::class,
        CreateSequenceChange::getCatalogName,
        CreateSequenceChange::getSchemaName,
        CreateSequenceChange::getSequenceName,
        CreateSequenceChange::getStartValue,
        CreateSequenceChange::getIncrementBy,
        CreateSequenceChange::getMaxValue,
        CreateSequenceChange::getMinValue,
        CreateSequenceChange::isOrdered,
        CreateSequenceChange::getCycle,
        CreateSequenceChange::getCacheSize
    )

    @Test
    fun createTableTest() = testEvaluation(
        LkCreateTable(),
        CreateTableChange::class,
        CreateTableChange::getColumns to emptyList<ColumnConfig>(),
        CreateTableChange::getCatalogName,
        CreateTableChange::getSchemaName,
        CreateTableChange::getTableName,
        CreateTableChange::getTablespace,
        CreateTableChange::getRemarks
    )

    @Test
    fun createViewTest() = testEvaluation(
        LkCreateView(),
        CreateViewChange::class,
        CreateViewChange::getCatalogName,
        CreateViewChange::getSchemaName,
        CreateViewChange::getViewName,
        CreateViewChange::getSelectQuery,
        CreateViewChange::getReplaceIfExists,
        CreateViewChange::getFullDefinition
    )

}