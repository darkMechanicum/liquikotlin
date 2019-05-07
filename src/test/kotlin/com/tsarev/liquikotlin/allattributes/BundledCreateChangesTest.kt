package com.tsarev.liquikotlin.allattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.util.*
import liquibase.change.AddColumnConfig
import liquibase.change.ColumnConfig
import liquibase.change.ConstraintsConfig
import liquibase.change.core.*
import liquibase.statement.DatabaseFunction
import liquibase.statement.SequenceCurrentValueFunction
import liquibase.statement.SequenceNextValueFunction
import org.junit.Test

/**
 * Testing that basic evaluations process all attributes
 * correctly without modifications.
 */
open class BundledCreateChangesTest : BaseLiquikotlinUnitTest() {

    private val columnConfigFields = arrayOf(
        ColumnConfig::getName to testColumnName,
        ColumnConfig::getComputed to testComputed,
        ColumnConfig::getType to testColumnType,
        ColumnConfig::getValue to testValue,
        ColumnConfig::getValueNumeric to testValueNumeric,
        ColumnConfig::getValueDate to testValueDate,
        ColumnConfig::getValueBoolean to testValueBoolean,
        ColumnConfig::getValueBlobFile to testValueBlobFile,
        ColumnConfig::getValueClobFile to testValueClobFile,
        ColumnConfig::getEncoding to testEncoding,
        ColumnConfig::getValueComputed to DatabaseFunction(testValueComputed),
        ColumnConfig::getValueSequenceNext to SequenceNextValueFunction(testValueSequenceNext),
        ColumnConfig::getValueSequenceCurrent to SequenceCurrentValueFunction(testValueSequenceCurrent),
        ColumnConfig::getDefaultValue to testDefaultValue,
        ColumnConfig::getDefaultValueNumeric to testDefaultValueNumeric,
        ColumnConfig::getDefaultValueDate to testDefaultValueDate,
        ColumnConfig::getDefaultValueBoolean to testDefaultValueBoolean,
        ColumnConfig::getDefaultValueComputed to DatabaseFunction(testDefaultValueComputed),
        ColumnConfig::getDefaultValueSequenceNext to SequenceNextValueFunction(testDefaultValueSequenceNext),
        ColumnConfig::isAutoIncrement to testAutoIncrement,
        ColumnConfig::getStartWith to testStartWith,
        ColumnConfig::getIncrementBy to testIncrementBy,
        ColumnConfig::getRemarks to testRemarks,
        ColumnConfig::getDescending to testDescending
    )

    private fun LkBaseColumnConfig<*>.setBaseColumnAttributes() = this
        .name(testColumnName)
        .type(testColumnType)
        .value(testValue)
        .computed(testComputed)
        .valueNumeric(testValueNumeric)
        .valueBoolean(testValueBoolean)
        .valueDate(testValueDate)
        .valueComputed(testValueComputed)
        .valueBlobFile(testValueBlobFile)
        .valueClobFile(testValueClobFile)
        .encoding(testEncoding)
        .defaultValue(testDefaultValue)
        .defaultValueNumeric(testDefaultValueNumeric)
        .defaultValueBoolean(testDefaultValueBoolean)
        .defaultValueDate(testDefaultValueDate)
        .defaultValueComputed(testDefaultValueComputed)
        .autoIncrement(testAutoIncrement)
        .remarks(testRemarks)
        .valueSequenceNext(testValueSequenceNext)
        .valueSequenceCurrent(testValueSequenceCurrent)
        .defaultValueSequenceNext(testDefaultValueSequenceNext)
        .startWith(testStartWith)
        .incrementBy(testIncrementBy)
        .descending(testDescending)

    @Test
    fun addColumnConfigTest() = testEvaluation(
        LkAddColumnConfig()
            .afterColumn(testAfterColumn)
            .beforeColumn(testBeforeColumn)
            .position(testPosition)
            .setBaseColumnAttributes(),
        AddColumnConfig::class,
        AddColumnConfig::getAfterColumn to testAfterColumn,
        AddColumnConfig::getBeforeColumn to testBeforeColumn,
        AddColumnConfig::getPosition to testPosition,
        *columnConfigFields
    )

    @Test
    fun loadColumnConfigTest() = testEvaluation(
        LkLoadColumnConfig()
            .index(testIndex)
            .header(testHeader)
            .setBaseColumnAttributes(),
        LoadDataColumnConfig::class,
        LoadDataColumnConfig::getIndex to testIndex,
        LoadDataColumnConfig::getHeader to testHeader,
        *columnConfigFields
    )

    @Test
    fun commonColumnConfigTest() = testEvaluation(
        LkCommonColumnConfig()
            .setBaseColumnAttributes(),
        ColumnConfig::class,
        * columnConfigFields
    )

    @Test
    fun addAutoIncrementTest() = testEvaluation(
        LkAddAutoIncrement()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .columnName(testColumnName)
            .columnDataType(testColumnType)
            .tableName(testTableName)
            .startWith(testStartWith)
            .incrementBy(testIncrementBy),
        AddAutoIncrementChange::class,
        AddAutoIncrementChange::getCatalogName to testCatalogName,
        AddAutoIncrementChange::getSchemaName to testSchemaName,
        AddAutoIncrementChange::getTableName to testTableName,
        AddAutoIncrementChange::getColumnName to testColumnName,
        AddAutoIncrementChange::getColumnDataType to testColumnType,
        AddAutoIncrementChange::getStartWith to testStartWith,
        AddAutoIncrementChange::getIncrementBy to testIncrementBy
    )

    @Test
    fun addColumnTest() = testEvaluation(
        LkAddColumn()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName),
        AddColumnChange::class,
        AddColumnChange::getCatalogName to testCatalogName,
        AddColumnChange::getSchemaName to testSchemaName,
        AddColumnChange::getTableName to testTableName,
        AddColumnChange::getColumns to emptyList<AddColumnConfig>()
    )

    @Test
    fun constraintsTest() = testEvaluation(
        LkConstraints()
            .nullable(testNullable)
            .primaryKey(testPrimaryKey)
            .primaryKeyName(testPrimaryKeyName)
            .primaryKeyTablespace(testPrimaryKeyTablespace)
            .references(testReferences)
            .referencedTableName(testReferencedTableName)
            .referencedColumnNames(testReferencedColumnNames)
            .unique(testUnique)
            .uniqueConstraintName(testUniqueConstraintName)
            .checkConstraint(testCheckConstraint)
            .deleteCascade(testDeleteCascade)
            .foreignKeyName(testForeignKeyName)
            .initiallyDeferred(testInitiallyDeferred)
            .deferrable(testDeferrable),
        ConstraintsConfig::class,
        ConstraintsConfig::isNullable to testNullable,
        ConstraintsConfig::isNullable to testNullable,
        ConstraintsConfig::isPrimaryKey to testPrimaryKey,
        ConstraintsConfig::getPrimaryKeyName to testPrimaryKeyName,
        ConstraintsConfig::getPrimaryKeyTablespace to testPrimaryKeyTablespace,
        ConstraintsConfig::getReferences to testReferences,
        ConstraintsConfig::getReferencedTableName to testReferencedTableName,
        ConstraintsConfig::getReferencedColumnNames to testReferencedColumnNames,
        ConstraintsConfig::isUnique to testUnique,
        ConstraintsConfig::getUniqueConstraintName to testUniqueConstraintName,
        ConstraintsConfig::getCheckConstraint to testCheckConstraint,
        ConstraintsConfig::isDeleteCascade to testDeleteCascade,
        ConstraintsConfig::getForeignKeyName to testForeignKeyName,
        ConstraintsConfig::isInitiallyDeferred to testInitiallyDeferred,
        ConstraintsConfig::isDeferrable to testDeferrable
    )

    @Test
    fun addDefaultValueTest() = testEvaluation(
        LkAddDefaultValue()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .columnName(testColumnName)
            .columnDataType(testColumnType)
            .defaultValue(testDefaultValue)
            .defaultValueNumeric(testDefaultValueNumeric)
            .defaultValueDate(testDefaultValueDate)
            .defaultValueBoolean(testDefaultValueBoolean)
            .defaultValueComputed(testDefaultValueComputed)
            .defaultValueSequenceNext(testDefaultValueSequenceNext),
        AddDefaultValueChange::class,
        AddDefaultValueChange::getCatalogName to testCatalogName,
        AddDefaultValueChange::getSchemaName to testSchemaName,
        AddDefaultValueChange::getTableName to testTableName,
        AddDefaultValueChange::getColumnName to testColumnName,
        AddDefaultValueChange::getColumnDataType to testColumnType,
        AddDefaultValueChange::getDefaultValue to testDefaultValue,
        AddDefaultValueChange::getDefaultValueNumeric to "$testDefaultValueNumeric",
        AddDefaultValueChange::getDefaultValueDate to "$testDefaultValueDate",
        AddDefaultValueChange::getDefaultValueBoolean to testDefaultValueBoolean,
        AddDefaultValueChange::getDefaultValueComputed to DatabaseFunction(testDefaultValueComputed),
        AddDefaultValueChange::getDefaultValueSequenceNext to SequenceNextValueFunction(testDefaultValueSequenceNext)
    )

    @Test
    fun addForeignKeyConstraintTest() = testEvaluation(
        LkAddForeignKeyConstraint()
            .baseTableCatalogName(testBaseTableCatalogName)
            .baseTableSchemaName(testBaseTableSchemaName)
            .baseTableName(testBaseTableName)
            .baseColumnNames(testBaseColumnNames)
            .referencedTableCatalogName(testReferencedTableCatalogName)
            .referencedTableSchemaName(testReferencedTableSchemaName)
            .referencedTableName(testReferencedTableName)
            .referencedColumnNames(testReferencedColumnNames)
            .constraintName(testConstraintName)
            .deferrable(testDeferrable)
            .initiallyDeferred(testInitiallyDeferred)
            .onUpdate(testOnUpdate)
            .onDelete(testOnDelete),
        AddForeignKeyConstraintChange::class,
        AddForeignKeyConstraintChange::getBaseTableCatalogName to testBaseTableCatalogName,
        AddForeignKeyConstraintChange::getBaseTableSchemaName to testBaseTableSchemaName,
        AddForeignKeyConstraintChange::getBaseTableName to testBaseTableName,
        AddForeignKeyConstraintChange::getBaseColumnNames to testBaseColumnNames,
        AddForeignKeyConstraintChange::getReferencedTableCatalogName to testReferencedTableCatalogName,
        AddForeignKeyConstraintChange::getReferencedTableSchemaName to testReferencedTableSchemaName,
        AddForeignKeyConstraintChange::getReferencedTableName to testReferencedTableName,
        AddForeignKeyConstraintChange::getReferencedColumnNames to testReferencedColumnNames,
        AddForeignKeyConstraintChange::getConstraintName to testConstraintName,
        AddForeignKeyConstraintChange::getDeferrable to testDeferrable,
        AddForeignKeyConstraintChange::getInitiallyDeferred to testInitiallyDeferred,
        AddForeignKeyConstraintChange::getOnUpdate to testOnUpdate,
        AddForeignKeyConstraintChange::getOnDelete to testOnDelete
    )


    @Test
    fun addLookupTableTest() = testEvaluation(
        LkAddLookupTable()
            .existingTableCatalogName(testExistingTableCatalogName)
            .existingTableSchemaName(testExistingTableSchemaName)
            .existingTableName(testExistingTableName)
            .existingColumnName(testExistingColumnName)
            .newTableCatalogName(testNewTableCatalogName)
            .newTableSchemaName(testNewTableSchemaName)
            .newTableName(testNewTableName)
            .newColumnName(testNewColumnName)
            .newColumnDataType(testNewColumnDataType)
            .constraintName(testConstraintName),
        AddLookupTableChange::class,
        AddLookupTableChange::getExistingTableCatalogName to testExistingTableCatalogName,
        AddLookupTableChange::getExistingTableSchemaName to testExistingTableSchemaName,
        AddLookupTableChange::getExistingTableName to testExistingTableName,
        AddLookupTableChange::getExistingColumnName to testExistingColumnName,
        AddLookupTableChange::getNewTableCatalogName to testNewTableCatalogName,
        AddLookupTableChange::getNewTableSchemaName to testNewTableSchemaName,
        AddLookupTableChange::getNewTableName to testNewTableName,
        AddLookupTableChange::getNewColumnName to testNewColumnName,
        AddLookupTableChange::getNewColumnDataType to testNewColumnDataType,
        AddLookupTableChange::getConstraintName to testConstraintName
    )

    @Test
    fun addNotNullConstraintTest() = testEvaluation(
        LkAddNotNullConstraint()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .columnName(testColumnName)
            .defaultNullValue(testDefaultNullValue)
            .columnDataType(testColumnType)
            .constraintName(testConstraintName),
        AddNotNullConstraintChange::class,
        AddNotNullConstraintChange::getCatalogName to testCatalogName,
        AddNotNullConstraintChange::getSchemaName to testSchemaName,
        AddNotNullConstraintChange::getTableName to testTableName,
        AddNotNullConstraintChange::getColumnName to testColumnName,
        AddNotNullConstraintChange::getDefaultNullValue to testDefaultNullValue,
        AddNotNullConstraintChange::getColumnDataType to testColumnType,
        AddNotNullConstraintChange::getConstraintName to testConstraintName
    )

    @Test
    fun addPrimaryKeyTest() = testEvaluation(
        LkAddPrimaryKey()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .tablespace(testTablespace)
            .columnNames(testColumnNames)
            .constraintName(testConstraintName)
            .clustered(testClustered)
            .forIndexName(testIndexName)
            .forIndexSchemaName(testSchemaName)
            .forIndexCatalogName(testCatalogName),
        AddPrimaryKeyChange::class,
        AddPrimaryKeyChange::getCatalogName to testCatalogName,
        AddPrimaryKeyChange::getSchemaName to testSchemaName,
        AddPrimaryKeyChange::getTableName to testTableName,
        AddPrimaryKeyChange::getTablespace to testTablespace,
        AddPrimaryKeyChange::getColumnNames to testColumnNames,
        AddPrimaryKeyChange::getConstraintName to testConstraintName,
        AddPrimaryKeyChange::getClustered to testClustered,
        AddPrimaryKeyChange::getForIndexName to testIndexName,
        AddPrimaryKeyChange::getForIndexSchemaName to testSchemaName,
        AddPrimaryKeyChange::getForIndexCatalogName to testCatalogName
    )

    @Test
    fun addUniqueConstraintTest() = testEvaluation(
        LkAddUniqueConstraint()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .columnNames(testColumnNames)
            .constraintName(testConstraintName)
            .tablespace(testTablespace)
            .deferrable(testDeferrable)
            .initiallyDeferred(testInitiallyDeferred)
            .disabled(testDisabled)
            .forIndexName(testIndexName)
            .forIndexSchemaName(testSchemaName)
            .forIndexCatalogName(testCatalogName),
        AddUniqueConstraintChange::class,
        AddUniqueConstraintChange::getCatalogName to testCatalogName,
        AddUniqueConstraintChange::getSchemaName to testSchemaName,
        AddUniqueConstraintChange::getTableName to testTableName,
        AddUniqueConstraintChange::getColumnNames to testColumnNames,
        AddUniqueConstraintChange::getConstraintName to testConstraintName,
        AddUniqueConstraintChange::getTablespace to testTablespace,
        AddUniqueConstraintChange::getDeferrable to testDeferrable,
        AddUniqueConstraintChange::getInitiallyDeferred to testInitiallyDeferred,
        AddUniqueConstraintChange::getDisabled to testDisabled,
        AddUniqueConstraintChange::getForIndexName to testIndexName,
        AddUniqueConstraintChange::getForIndexSchemaName to testSchemaName,
        AddUniqueConstraintChange::getForIndexCatalogName to testCatalogName
    )

    @Test
    fun createIndexTest() = testEvaluation(
        LkCreateIndex()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .indexName(testIndexName)
            .unique(testUnique)
            .tablespace(testTablespace),
        CreateIndexChange::class,
        CreateIndexChange::getCatalogName to testCatalogName,
        CreateIndexChange::getSchemaName to testSchemaName,
        CreateIndexChange::getTableName to testTableName,
        CreateIndexChange::getIndexName to testIndexName,
        CreateIndexChange::isUnique to testUnique,
        CreateIndexChange::getTablespace to testTablespace,
        CreateIndexChange::getColumns to emptyList<CreateIndexChange>()
    )

    @Test
    fun createProcedureTest() = testEvaluation(
        LkCreateProcedure()
            .comments(testComments)
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .procedureName(testProcedureName)
            .procedureText(testProcedureText)
            .dbms(testDbms)
            .path(testPath)
            .relativeToChangelogFile(testRelativeToChangelogFile)
            .encoding(testEncoding)
            .replaceIfExists(testReplaceIfExists),
        CreateProcedureChange::class,
        CreateProcedureChange::getComments to testComments,
        CreateProcedureChange::getCatalogName to testCatalogName,
        CreateProcedureChange::getSchemaName to testSchemaName,
        CreateProcedureChange::getProcedureName to testProcedureName,
        CreateProcedureChange::getProcedureText to testProcedureText,
        CreateProcedureChange::getDbms to testDbms,
        CreateProcedureChange::getPath to testPath,
        CreateProcedureChange::isRelativeToChangelogFile to testRelativeToChangelogFile,
        CreateProcedureChange::getEncoding to testEncoding,
        CreateProcedureChange::getReplaceIfExists to testReplaceIfExists
    )

    @Test
    fun createSequenceTest() = testEvaluation(
        LkCreateSequence()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .sequenceName(testSequenceName)
            .startValue(testStartValue)
            .incrementBy(testIncrementBy)
            .maxValue(testMaxValue)
            .minValue(testMinValue)
            .ordered(testOrdered)
            .cycle(testCycle)
            .cacheSize(testCacheSize),
        CreateSequenceChange::class,
        CreateSequenceChange::getCatalogName to testCatalogName,
        CreateSequenceChange::getSchemaName to testSchemaName,
        CreateSequenceChange::getSequenceName to testSequenceName,
        CreateSequenceChange::getStartValue to testStartValue,
        CreateSequenceChange::getIncrementBy to testIncrementBy,
        CreateSequenceChange::getMaxValue to testMaxValue,
        CreateSequenceChange::getMinValue to testMinValue,
        CreateSequenceChange::isOrdered to testOrdered,
        CreateSequenceChange::getCycle to testCycle,
        CreateSequenceChange::getCacheSize to testCacheSize
    )

    @Test
    fun createTableTest() = testEvaluation(
        LkCreateTable()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .tableName(testTableName)
            .tablespace(testTablespace)
            .remarks(testRemarks),
        CreateTableChange::class,
        CreateTableChange::getColumns to emptyList<ColumnConfig>(),
        CreateTableChange::getCatalogName to testCatalogName,
        CreateTableChange::getSchemaName to testSchemaName,
        CreateTableChange::getTableName to testTableName,
        CreateTableChange::getTablespace to testTablespace,
        CreateTableChange::getRemarks to testRemarks
    )

    @Test
    fun createViewTest() = testEvaluation(
        LkCreateView()
            .catalogName(testCatalogName)
            .schemaName(testSchemaName)
            .viewName(testViewName)
            .selectQuery(testSelectQuery)
            .replaceIfExists(testReplaceIfExists)
            .fullDefinition(testFullDefinition),
        CreateViewChange::class,
        CreateViewChange::getCatalogName to testCatalogName,
        CreateViewChange::getSchemaName to testSchemaName,
        CreateViewChange::getViewName to testViewName,
        CreateViewChange::getSelectQuery to testSelectQuery,
        CreateViewChange::getReplaceIfExists to testReplaceIfExists,
        CreateViewChange::getFullDefinition to testFullDefinition
    )

}