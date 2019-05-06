package com.tsarev.liquikotlin.allattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.util.*
import liquibase.change.AddColumnConfig
import liquibase.change.ColumnConfig
import liquibase.change.ConstraintsConfig
import liquibase.change.core.*
import liquibase.statement.DatabaseFunction
import org.junit.Test

/**
 * Testing that basic evaluations process all attributes
 * correctly without modifications.
 */
open class BundledCreateChangesTest : BaseLiquikotlinUnitTest() {

    private val columnConfigFields = arrayOf(
        ColumnConfig::getName to columnName,
        ColumnConfig::getComputed to columnComputed,
        ColumnConfig::getType to columnType,
        ColumnConfig::getValue to columnValue,
        ColumnConfig::getValueNumeric to columnValueNumeric,
        ColumnConfig::getValueDate to columnValueDate,
        ColumnConfig::getValueBoolean to columnValueBoolean,
        ColumnConfig::getValueBlobFile to columnValueBlobFile,
        ColumnConfig::getValueClobFile to columnValueClobFile,
        ColumnConfig::getEncoding to columnEncoding,
        ColumnConfig::getValueComputed to DatabaseFunction(columnValueComputed),
//        ColumnConfig::getValueSequenceNext to columnValueSequenceNext, TODO Implement missed attribute
//        ColumnConfig::getValueSequenceCurrent to columnValueSequenceCurrent, TODO Implement missed attribute
        ColumnConfig::getDefaultValue to columnDefaultValue,
        ColumnConfig::getDefaultValueNumeric to columnDefaultValueNumeric,
        ColumnConfig::getDefaultValueDate to columnDefaultValueDate,
        ColumnConfig::getDefaultValueBoolean to columnDefaultValueBoolean,
        ColumnConfig::getDefaultValueComputed to DatabaseFunction(columnDefaultValueComputed),
//        ColumnConfig::getDefaultValueSequenceNext to columnDefaultValueSequenceNext, TODO Implement missed attribute

        ColumnConfig::getConstraints,
//        ColumnConfig::isAutoIncrement to columnAutoIncrement, TODO Implement missed attribute
//        ColumnConfig::getStartWith to columnStartWith, TODO Implement missed attribute
//        ColumnConfig::getIncrementBy to columnIncrementBy, TODO Implement missed attribute
        ColumnConfig::getRemarks to columnRemarks
//        ColumnConfig::getDescending to columnDescending TODO Implement missed attribute
    )

    private fun LkBaseColumnConfig<*>.setBaseColumnAttributes() = this
        .name(columnName)
        .type(columnType)
        .value(columnValue)
        .computed(columnComputed)
        .valueNumeric(columnValueNumeric)
        .valueBoolean(columnValueBoolean)
        .valueDate(columnValueDate)
        .valueComputed(columnValueComputed)
        .valueBlobFile(columnValueBlobFile)
        .valueClobFile(columnValueClobFile)
        .encoding(columnEncoding)
        .defaultValue(columnDefaultValue)
        .defaultValueNumeric(columnDefaultValueNumeric)
        .defaultValueBoolean(columnDefaultValueBoolean)
        .defaultValueDate(columnDefaultValueDate)
        .defaultValueComputed(columnDefaultValueComputed)
        .autoIncrement(columnAutoIncrement)
        .remarks(columnRemarks)

    @Test
    fun addColumnConfigTest() = testEvaluation(
        LkAddColumnConfig()
            .afterColumn(afterColumn)
            .beforeColumn(beforeColumn)
            .position(position)
            .setBaseColumnAttributes(),
        AddColumnConfig::class,
        AddColumnConfig::getAfterColumn to afterColumn,
        AddColumnConfig::getBeforeColumn to beforeColumn,
        AddColumnConfig::getPosition to position,
        *columnConfigFields
    )

    @Test
    fun loadColumnConfigTest() = testEvaluation(
        LkLoadColumnConfig()
            .index(index)
            .header(header)
            .setBaseColumnAttributes(),
        LoadDataColumnConfig::class,
        LoadDataColumnConfig::getIndex to index,
        LoadDataColumnConfig::getHeader to header,
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
            .catalogName(catalogName)
            .schemaName(schemaName)
            .columnName(columnName)
            .columnDataType(columnType)
            .tableName(tableName)
            .startWith(startWith)
            .incrementBy(incrementBy),
        AddAutoIncrementChange::class,
        AddAutoIncrementChange::getCatalogName to catalogName,
        AddAutoIncrementChange::getSchemaName to schemaName,
        AddAutoIncrementChange::getTableName to tableName,
        AddAutoIncrementChange::getColumnName to columnName,
        AddAutoIncrementChange::getColumnDataType to columnType,
        AddAutoIncrementChange::getStartWith to startWith,
        AddAutoIncrementChange::getIncrementBy to incrementBy
    )

    @Test
    fun addColumnTest() = testEvaluation(
        LkAddColumn()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName),
        AddColumnChange::class,
        AddColumnChange::getCatalogName to catalogName,
        AddColumnChange::getSchemaName to schemaName,
        AddColumnChange::getTableName to tableName,
        AddColumnChange::getColumns to emptyList<AddColumnConfig>()
    )

    @Test
    fun constraintsTest() = testEvaluation(
        LkConstraints()
            .nullable(nullable)
            .primaryKey(primaryKey)
            .primaryKeyName(primaryKeyName)
//            .primaryKeyTablespace(primaryKeyTablespace) TODO Implement missed attribute
            .references(references)
//            .referencedTableName(referencedTableName) TODO Implement missed attribute
//            .referencedColumnNames(referencedColumnNames) TODO Implement missed attribute
            .unique(unique)
            .uniqueConstraintName(uniqueConstraintName)
//            .checkConstraint(checkConstraint) TODO Implement missed attribute
            .deleteCascade(deleteCascade)
            .foreignKeyName(foreignKeyName)
            .initiallyDeferred(initiallyDeferred)
            .deferrable(deferrable),
        ConstraintsConfig::class,
        ConstraintsConfig::isNullable to nullable,
        ConstraintsConfig::isPrimaryKey to primaryKey,
        ConstraintsConfig::getPrimaryKeyName to primaryKeyName,
//        ConstraintsConfig::getPrimaryKeyTablespace to primaryKeyTablespace, TODO Implement missed attribute
        ConstraintsConfig::getReferences to references,
//        ConstraintsConfig::getReferencedTableName to referencedTableName, TODO Implement missed attribute
//        ConstraintsConfig::getReferencedColumnNames to referencedColumnNames, TODO Implement missed attribute
        ConstraintsConfig::isUnique to unique,
        ConstraintsConfig::getUniqueConstraintName to uniqueConstraintName,
//        ConstraintsConfig::getCheckConstraint to checkConstraint, TODO Implement missed attribute
        ConstraintsConfig::isDeleteCascade to deleteCascade,
        ConstraintsConfig::getForeignKeyName to foreignKeyName,
        ConstraintsConfig::isInitiallyDeferred to initiallyDeferred,
        ConstraintsConfig::isDeferrable to deferrable
    )

    @Test
    fun addDefaultValueTest() = testEvaluation(
        LkAddDefaultValue()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .columnName(columnName)
            .columnDataType(columnType)
            .defaultValue(defaultValue)
            .defaultValueNumeric(defaultValueNumeric)
            .defaultValueDate(defaultValueDate)
            .defaultValueBoolean(defaultValueBoolean)
            .defaultValueComputed(defaultValueComputed),
//            .defaultValueSequenceNext(defaultValueSequenceNext), TODO Implement missed attribute
        AddDefaultValueChange::class,
        AddDefaultValueChange::getCatalogName to catalogName,
        AddDefaultValueChange::getSchemaName to schemaName,
        AddDefaultValueChange::getTableName to tableName,
        AddDefaultValueChange::getColumnName to columnName,
        AddDefaultValueChange::getColumnDataType to columnType,
        AddDefaultValueChange::getDefaultValue to defaultValue,
        AddDefaultValueChange::getDefaultValueNumeric to defaultValueNumeric,
        AddDefaultValueChange::getDefaultValueDate to defaultValueDate,
        AddDefaultValueChange::getDefaultValueBoolean to defaultValueBoolean,
        AddDefaultValueChange::getDefaultValueComputed to DatabaseFunction(defaultValueComputed)
//        AddDefaultValueChange::getDefaultValueSequenceNext to defaultValueSequenceNext TODO Implement missed attribute
    )

    @Test
    fun addForeignKeyConstraintTest() = testEvaluation(
        LkAddForeignKeyConstraint()
            .baseTableCatalogName(baseTableCatalogName)
            .baseTableSchemaName(baseTableSchemaName)
            .baseTableName(baseTableName)
            .baseColumnNames(baseColumnNames)
            .referencedTableCatalogName(referencedTableCatalogName)
            .referencedTableSchemaName(referencedTableSchemaName)
            .referencedTableName(referencedTableName)
            .referencedColumnNames(referencedColumnNames)
            .constraintName(constraintName)
            .deferrable(deferrable)
            .initiallyDeferred(initiallyDeferred)
            .onUpdate(onUpdate)
            .onDelete(onDelete),
        AddForeignKeyConstraintChange::class,
        AddForeignKeyConstraintChange::getBaseTableCatalogName to baseTableCatalogName,
        AddForeignKeyConstraintChange::getBaseTableSchemaName to baseTableSchemaName,
        AddForeignKeyConstraintChange::getBaseTableName to baseTableName,
        AddForeignKeyConstraintChange::getBaseColumnNames to baseColumnNames,
        AddForeignKeyConstraintChange::getReferencedTableCatalogName to referencedTableCatalogName,
        AddForeignKeyConstraintChange::getReferencedTableSchemaName to referencedTableSchemaName,
        AddForeignKeyConstraintChange::getReferencedTableName to referencedTableName,
        AddForeignKeyConstraintChange::getReferencedColumnNames to referencedColumnNames,
        AddForeignKeyConstraintChange::getConstraintName to constraintName,
        AddForeignKeyConstraintChange::getDeferrable to deferrable,
        AddForeignKeyConstraintChange::getInitiallyDeferred to initiallyDeferred,
        AddForeignKeyConstraintChange::getOnUpdate to onUpdate,
        AddForeignKeyConstraintChange::getOnDelete to onDelete
    )


    @Test
    fun addLookupTableTest() = testEvaluation(
        LkAddLookupTable()
            .existingTableCatalogName(existingTableCatalogName)
            .existingTableSchemaName(existingTableSchemaName)
            .existingTableName(existingTableName)
            .existingColumnName(existingColumnName)
            .newTableCatalogName(newTableCatalogName)
            .newTableSchemaName(newTableSchemaName)
            .newTableName(newTableName)
            .newColumnName(newColumnName)
            .newColumnDataType(newColumnDataType)
            .constraintName(constraintName),
        AddLookupTableChange::class,
        AddLookupTableChange::getExistingTableCatalogName to existingTableCatalogName,
        AddLookupTableChange::getExistingTableSchemaName to existingTableSchemaName,
        AddLookupTableChange::getExistingTableName to existingTableName,
        AddLookupTableChange::getExistingColumnName to existingColumnName,
        AddLookupTableChange::getNewTableCatalogName to newTableCatalogName,
        AddLookupTableChange::getNewTableSchemaName to newTableSchemaName,
        AddLookupTableChange::getNewTableName to newTableName,
        AddLookupTableChange::getNewColumnName to newColumnName,
        AddLookupTableChange::getNewColumnDataType to newColumnDataType,
        AddLookupTableChange::getConstraintName to constraintName
    )

    @Test
    fun addNotNullConstraintTest() = testEvaluation(
        LkAddNotNullConstraint()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .columnName(columnName)
            .defaultNullValue(defaultNullValue)
            .columnDataType(columnType),
//            .constraintName(constraintName), TODO Implement missed attribute
        AddNotNullConstraintChange::class,
        AddNotNullConstraintChange::getCatalogName to catalogName,
        AddNotNullConstraintChange::getSchemaName to schemaName,
        AddNotNullConstraintChange::getTableName to tableName,
        AddNotNullConstraintChange::getColumnName to columnName,
        AddNotNullConstraintChange::getDefaultNullValue to defaultNullValue,
        AddNotNullConstraintChange::getColumnDataType to columnType
//        AddNotNullConstraintChange::getConstraintName TODO Implement missed attribute
    )

    @Test
    fun addPrimaryKeyTest() = testEvaluation(
        LkAddPrimaryKey()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .tablespace(tablespace)
            .columnNames(columnNames)
            .constraintName(constraintName),
//            .clustered(clustered) TODO Implement missed attribute
//            .forIndexName(forIndexName) TODO Implement missed attribute
//            .forIndexSchemaName(forIndexSchemaName) TODO Implement missed attribute
//            .forIndexCatalogName(forIndexCatalogName), TODO Implement missed attribute
        AddPrimaryKeyChange::class,
        AddPrimaryKeyChange::getCatalogName to catalogName,
        AddPrimaryKeyChange::getSchemaName to schemaName,
        AddPrimaryKeyChange::getTableName to tableName,
        AddPrimaryKeyChange::getTablespace to tablespace,
        AddPrimaryKeyChange::getColumnNames to columnNames,
        AddPrimaryKeyChange::getConstraintName to constraintName
//        AddPrimaryKeyChange::getClustered, TODO Implement missed attribute
//        AddPrimaryKeyChange::getForIndexName, TODO Implement missed attribute
//        AddPrimaryKeyChange::getForIndexSchemaName, TODO Implement missed attribute
//        AddPrimaryKeyChange::getForIndexCatalogName TODO Implement missed attribute
    )

    @Test
    fun addUniqueConstraintTest() = testEvaluation(
        LkAddUniqueConstraint()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .columnNames(columnNames)
            .constraintName(constraintName)
            .tablespace(tablespace)
            .deferrable(deferrable)
            .initiallyDeferred(initiallyDeferred)
            .disabled(disabled),
//            .forIndexName(forIndexName) TODO Implement missed attribute
//            .forIndexSchemaName(forIndexSchemaName) TODO Implement missed attribute
//            .forIndexCatalogName(forIndexCatalogName), TODO Implement missed attribute
        AddUniqueConstraintChange::class,
        AddUniqueConstraintChange::getCatalogName to catalogName,
        AddUniqueConstraintChange::getSchemaName to schemaName,
        AddUniqueConstraintChange::getTableName to tableName,
        AddUniqueConstraintChange::getColumnNames to columnNames,
        AddUniqueConstraintChange::getConstraintName to constraintName,
        AddUniqueConstraintChange::getTablespace to tablespace,
        AddUniqueConstraintChange::getDeferrable to deferrable,
        AddUniqueConstraintChange::getInitiallyDeferred to initiallyDeferred,
        AddUniqueConstraintChange::getDisabled to disabled
//        AddUniqueConstraintChange::getForIndexName, TODO Implement missed attribute
//        AddUniqueConstraintChange::getForIndexSchemaName, TODO Implement missed attribute
//        AddUniqueConstraintChange::getForIndexCatalogName TODO Implement missed attribute
    )

    @Test
    fun createIndexTest() = testEvaluation(
        LkCreateIndex()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .indexName(indexName)
            .unique(unique)
            .tablespace(tablespace),
        CreateIndexChange::class,
        CreateIndexChange::getCatalogName to catalogName,
        CreateIndexChange::getSchemaName to schemaName,
        CreateIndexChange::getTableName to tableName,
        CreateIndexChange::getIndexName to indexName,
        CreateIndexChange::isUnique to unique,
        CreateIndexChange::getTablespace to tablespace,
        CreateIndexChange::getColumns to emptyList<CreateIndexChange>()
    )

    @Test
    fun createProcedureTest() = testEvaluation(
        LkCreateProcedure()
            .comments(comments)
            .catalogName(catalogName)
            .schemaName(schemaName)
            .procedureName(procedureName)
            .procedureText(procedureText)
            .dbms(dbms)
            .path(path)
            .relativeToChangelogFile(relativeToChangelogFile)
            .encoding(encoding),
//            .replaceIfExists(replaceIfExists), TODO Implement missed attribute
        CreateProcedureChange::class,
        CreateProcedureChange::getComments to comments,
        CreateProcedureChange::getCatalogName to catalogName,
        CreateProcedureChange::getSchemaName to schemaName,
        CreateProcedureChange::getProcedureName to procedureName,
        CreateProcedureChange::getProcedureText to procedureText,
        CreateProcedureChange::getDbms to dbms,
        CreateProcedureChange::getPath to path,
        CreateProcedureChange::isRelativeToChangelogFile to relativeToChangelogFile,
        CreateProcedureChange::getEncoding to encoding
//        CreateProcedureChange::getReplaceIfExists TODO Implement missed attribute
    )

    @Test
    fun createSequenceTest() = testEvaluation(
        LkCreateSequence()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .sequenceName(sequenceName)
            .startValue(startValue)
            .incrementBy(incrementBy)
            .maxValue(maxValue)
            .minValue(minValue)
            .ordered(ordered)
            .cycle(cycle),
//            .cacheSize(cacheSize), TODO Implement missed attribute
        CreateSequenceChange::class,
        CreateSequenceChange::getCatalogName to catalogName,
        CreateSequenceChange::getSchemaName to schemaName,
        CreateSequenceChange::getSequenceName to sequenceName,
        CreateSequenceChange::getStartValue to startValue,
        CreateSequenceChange::getIncrementBy to incrementBy,
        CreateSequenceChange::getMaxValue to maxValue,
        CreateSequenceChange::getMinValue to minValue,
        CreateSequenceChange::isOrdered to ordered,
        CreateSequenceChange::getCycle to cycle
//        CreateSequenceChange::getCacheSize TODO Implement missed attribute
    )

    @Test
    fun createTableTest() = testEvaluation(
        LkCreateTable()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .tableName(tableName)
            .tablespace(tablespace)
            .remarks(remarks),
        CreateTableChange::class,
        CreateTableChange::getColumns to emptyList<ColumnConfig>(),
        CreateTableChange::getCatalogName to catalogName,
        CreateTableChange::getSchemaName to schemaName,
        CreateTableChange::getTableName to tableName,
        CreateTableChange::getTablespace to tablespace,
        CreateTableChange::getRemarks to remarks
    )

    @Test
    fun createViewTest() = testEvaluation(
        LkCreateView()
            .catalogName(catalogName)
            .schemaName(schemaName)
            .viewName(viewName)
            .selectQuery(selectQuery)
            .replaceIfExists(replaceIfExists),
//            .fullDefinition(fullDefinition), TODO Implement missed attribute
        CreateViewChange::class,
        CreateViewChange::getCatalogName to catalogName,
        CreateViewChange::getSchemaName to schemaName,
        CreateViewChange::getViewName to viewName,
        CreateViewChange::getSelectQuery to selectQuery,
        CreateViewChange::getReplaceIfExists to replaceIfExists
//        CreateViewChange::getFullDefinition TODO Implement missed attribute
    )

}