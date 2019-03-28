package com.tsarev.liquikotlin.bundled

import com.tsarev.liquikotlin.infrastructure.LbDslNode
import liquibase.change.AddColumnConfig
import liquibase.change.ColumnConfig
import liquibase.change.ConstraintsConfig
import liquibase.change.core.*
import liquibase.statement.DatabaseFunction
import liquibase.statement.SequenceNextValueFunction
import java.lang.IllegalArgumentException
import java.math.BigInteger
import java.util.*
import kotlin.reflect.KClass

// --- Generalized classes ---

abstract class LkBaseColumnConfig<SelfT : LkBaseColumnConfig<SelfT, ColumnT, ParentT>, ColumnT : ColumnConfig, ParentT>(
    thisClass: KClass<SelfT>,
    linkedConstructor: () -> ColumnT,
    parentSetter: (ParentT, ColumnT, SelfT) -> Unit
) : LbDslNode<SelfT, ColumnT, ParentT>(
    thisClass,
    linkedConstructor,
    { parent, linked, self, _ -> parentSetter(parent, linked, self) }
) {
    val name by nullableWS(String::class, ColumnConfig::setName)
    val type by nullableWS(String::class, ColumnConfig::setType)
    val value by nullableWS(String::class, ColumnConfig::setValue)
    val computed by nullableWS(Boolean::class, ColumnConfig::setComputed)
    val valueNumeric by nullableWS(Number::class, ColumnConfig::setValueNumeric)
    val valueBoolean by nullableWS(Boolean::class, ColumnConfig::setValueBoolean)
    val valueDate by nullableWS(Date::class, ColumnConfig::setValueDate)
    val valueComputed by nullableWS(Boolean::class, ColumnConfig::setComputed)
    val valueBlobFile by nullableWS(String::class, ColumnConfig::setValueBlobFile)
    val valueClobFile by nullableWS(String::class, ColumnConfig::setValueClobFile)
    val encoding by nullableWS(String::class, ColumnConfig::setEncoding)
    val defaultValue by nullableWS(String::class, ColumnConfig::setDefaultValue)
    val defaultValueNumeric by nullableWS(Number::class, ColumnConfig::setDefaultValueNumeric)
    val defaultValueBoolean by nullableWS(Boolean::class, ColumnConfig::setDefaultValueBoolean)
    val defaultValueDate by nullableWS(Date::class, ColumnConfig::setDefaultValueDate)
    val defaultValueComputed by nullableWS(
        String::class,
        { config, value -> config.setDefaultValueComputed(DatabaseFunction(value)) })
    val autoIncrement by nullableWS(Boolean::class, ColumnConfig::setAutoIncrement)
    val remarks by nullableWS(String::class, ColumnConfig::setRemarks)

    val constraints by child(::LkConstraints)

    operator fun invoke(name: String) = name(name)
}

abstract class LkBaseAddColumnConfig<SelfT : LkBaseAddColumnConfig<SelfT, ParentT>, ParentT>(
    selfClass: KClass<SelfT>,
    linkedSetter: (ParentT, AddColumnConfig, LkBaseAddColumnConfig<SelfT, ParentT>) -> Unit
) : LkBaseColumnConfig<SelfT, AddColumnConfig, ParentT>(
    selfClass,
    ::AddColumnConfig,
    linkedSetter
) {
    val beforeColumn by nullableWS(String::class, AddColumnConfig::setBeforeColumn)
    val afterColumn by nullableWS(String::class, AddColumnConfig::setAfterColumn)
    val position by nullableWS(Int::class, AddColumnConfig::setPosition)
}

abstract class LkCommonColumnConfig<SelfT : LkCommonColumnConfig<SelfT, ParentT>, ParentT>(
    selfClass: KClass<SelfT>,
    linkedSetter: (ParentT, ColumnConfig, LkCommonColumnConfig<SelfT, ParentT>) -> Unit
) : LkBaseColumnConfig<SelfT, ColumnConfig, ParentT>(
    selfClass,
    ::ColumnConfig,
    linkedSetter
)

// --- Definitions ---

open class LkAddAutoIncrement :
    LkChange<LkAddAutoIncrement, AddAutoIncrementChange>(
        LkAddAutoIncrement::class,
        ::AddAutoIncrementChange
    ) {
    val catalogName by nullableWS(String::class, AddAutoIncrementChange::setCatalogName)
    val columnDataType by nullableWS(String::class, AddAutoIncrementChange::setColumnDataType)
    val columnName by nonNullableWS(String::class, AddAutoIncrementChange::setColumnName)
    val incrementBy by nullableWS(BigInteger::class, AddAutoIncrementChange::setIncrementBy)
    val schemaName by nullableWS(String::class, AddAutoIncrementChange::setSchemaName)
    val startWith by nullableWS(BigInteger::class, AddAutoIncrementChange::setStartWith)
    val tableName by nonNullableWS(String::class, AddAutoIncrementChange::setTableName)

    fun at(tableName: String, columnName: String) = columnName(columnName).tableName(tableName)
}

open class LkAddColumn : LkChange<LkAddColumn, AddColumnChange>(
    LkAddColumn::class,
    ::AddColumnChange
) {
    val catalogName by nullableWS(String::class, AddColumnChange::setCatalogName)
    val schemaName by nullableWS(String::class, AddColumnChange::setSchemaName)
    val tableName by nonNullableWS(String::class, AddColumnChange::setTableName)

    val column by child(::LkAddColumnConfig)

    operator fun invoke(tableName: String) = tableName(tableName)
}

class LkAddColumnConfig : LkBaseAddColumnConfig<LkAddColumnConfig, AddColumnChange>(
    LkAddColumnConfig::class,
    { change, it, _ -> change.addColumn(it) }
)

open class LkConstraints : LbDslNode<LkConstraints, ConstraintsConfig, ColumnConfig>(
    LkConstraints::class,
    ::ConstraintsConfig,
    { columnConfig, it, _, _ -> columnConfig.constraints = it }
) {
    val nullable by nullableWS(Boolean::class, ConstraintsConfig::setNullable)
    val primaryKey by nullableWS(Boolean::class, ConstraintsConfig::setPrimaryKey)
    val primaryKeyName by nullableWS(String::class, ConstraintsConfig::setPrimaryKeyName)
    val unique by nullableWS(Boolean::class, ConstraintsConfig::setUnique)
    val uniqueConstraintName by nullableWS(String::class, ConstraintsConfig::setUniqueConstraintName)
    val references by nullableWS(String::class, ConstraintsConfig::setReferences)
    val foreignKeyName by nullableWS(String::class, ConstraintsConfig::setForeignKeyName)
    val deleteCascade by nullableWS(Boolean::class, ConstraintsConfig::setDeleteCascade)
    val deferrable by nullableWS(Boolean::class, ConstraintsConfig::setDeferrable)
    val initiallyDeferred by nullableWS(Boolean::class, ConstraintsConfig::setInitiallyDeferred)
}

open class LkAddDefaultValue : LkChange<LkAddDefaultValue, AddDefaultValueChange>(
    LkAddDefaultValue::class,
    ::AddDefaultValueChange
) {
    val catalogName by nullableWS(String::class, AddDefaultValueChange::setCatalogName)
    val columnDataType by nullableWS(String::class, AddDefaultValueChange::setColumnDataType)
    val columnName by nullableWS(String::class, AddDefaultValueChange::setColumnName)
    val defaultValue by nullableWS(String::class, AddDefaultValueChange::setDefaultValue)
    val defaultValueBoolean by nullableWS(Boolean::class, AddDefaultValueChange::setDefaultValueBoolean)
    val defaultValueComputed by nullableWS(
        String::class,
        { change, value -> change.setDefaultValueComputed(DatabaseFunction(value)) })
    val defaultValueDate by nullableWS(String::class, AddDefaultValueChange::setDefaultValueDate)
    val defaultValueNumeric by nullableWS(String::class, AddDefaultValueChange::setDefaultValueNumeric)
    val defaultValueSequenceNext by nullableWS(
        String::class,
        { change, value -> change.setDefaultValueSequenceNext(SequenceNextValueFunction(value)) })
    val schemaName by nullableWS(String::class, AddDefaultValueChange::setSchemaName)
    val tableName by nullableWS(String::class, AddDefaultValueChange::setTableName)

    fun at(tableName: String, columnName: String) = tableName(tableName).columnName(columnName)
}

open class LkAddForeignKeyConstraint : LkChange<LkAddForeignKeyConstraint, AddForeignKeyConstraintChange>(
    LkAddForeignKeyConstraint::class,
    ::AddForeignKeyConstraintChange
) {
    val baseColumnNames by nullableWS(String::class, AddForeignKeyConstraintChange::setBaseColumnNames)
    val baseTableCatalogName by nullableWS(String::class, AddForeignKeyConstraintChange::setBaseTableCatalogName)
    val baseTableName by nullableWS(String::class, AddForeignKeyConstraintChange::setBaseTableName)
    val baseTableSchemaName by nullableWS(String::class, AddForeignKeyConstraintChange::setBaseTableSchemaName)
    val constraintName by nullableWS(String::class, AddForeignKeyConstraintChange::setConstraintName)
    val deferrable by nullableWS(Boolean::class, AddForeignKeyConstraintChange::setDeferrable)
    val initiallyDeferred by nullableWS(Boolean::class, AddForeignKeyConstraintChange::setInitiallyDeferred)
    val onDelete by nullableWS(String::class, AddForeignKeyConstraintChange::setOnDelete)
    val onUpdate by nullableWS(String::class, AddForeignKeyConstraintChange::setOnUpdate)
    val referencedColumnNames by nullableWS(String::class, AddForeignKeyConstraintChange::setReferencedColumnNames)
    val referencedTableCatalogName by nullableWS(
        String::class,
        AddForeignKeyConstraintChange::setReferencedTableCatalogName
    )
    val referencedTableName by nullableWS(String::class, AddForeignKeyConstraintChange::setReferencedTableName)
    val referencedTableSchemaName by nullableWS(
        String::class,
        AddForeignKeyConstraintChange::setReferencedTableSchemaName
    )
    val referencesUniqueColumn by nullableWS(Boolean::class, AddForeignKeyConstraintChange::setReferencesUniqueColumn)

    operator fun invoke(constraintName: String) = constraintName(constraintName)
    fun at(baseTableName: String, vararg baseColumnNames: String) =
        if (baseColumnNames.isNotEmpty())
            baseTableName(baseTableName).baseColumnNames(baseColumnNames.joinToString())
        else
            throw IllegalArgumentException("Referenced columns cannot be empty.")
}

open class LkAddLookupTable : LkChange<LkAddLookupTable, AddLookupTableChange>(
    LkAddLookupTable::class,
    ::AddLookupTableChange
) {
    val constraintName by nullableWS(String::class, AddLookupTableChange::setConstraintName)
    val existingColumnName by nullableWS(String::class, AddLookupTableChange::setExistingColumnName)
    val existingTableCatalogName by nullableWS(String::class, AddLookupTableChange::setExistingTableCatalogName)
    val existingTableName by nullableWS(String::class, AddLookupTableChange::setExistingTableName)
    val existingTableSchemaName by nullableWS(String::class, AddLookupTableChange::setExistingTableSchemaName)
    val newColumnDataType by nullableWS(String::class, AddLookupTableChange::setNewColumnDataType)
    val newColumnName by nullableWS(String::class, AddLookupTableChange::setNewColumnName)
    val newTableCatalogName by nullableWS(String::class, AddLookupTableChange::setNewTableCatalogName)
    val newTableName by nullableWS(String::class, AddLookupTableChange::setNewTableName)
    val newTableSchemaName by nullableWS(String::class, AddLookupTableChange::setNewTableSchemaName)
}

open class LkAddNotNullConstraint : LkChange<LkAddNotNullConstraint, AddNotNullConstraintChange>(
    LkAddNotNullConstraint::class,
    ::AddNotNullConstraintChange
) {
    val catalogName by nullableWS(String::class, AddNotNullConstraintChange::setCatalogName)
    val columnDataType by nullableWS(String::class, AddNotNullConstraintChange::setColumnDataType)
    val columnName by nullableWS(String::class, AddNotNullConstraintChange::setColumnName)
    val defaultNullValue by nullableWS(String::class, AddNotNullConstraintChange::setDefaultNullValue)
    val schemaName by nullableWS(String::class, AddNotNullConstraintChange::setSchemaName)
    val tableName by nullableWS(String::class, AddNotNullConstraintChange::setTableName)

    fun at(tableName: String, columnName: String) = tableName(tableName).columnName(columnName)
}

open class LkAddPrimaryKey : LkChange<LkAddPrimaryKey, AddPrimaryKeyChange>(
    LkAddPrimaryKey::class,
    ::AddPrimaryKeyChange
) {
    val catalogName by nullableWS(String::class, AddPrimaryKeyChange::setCatalogName)
    val columnNames by nullableWS(String::class, AddPrimaryKeyChange::setColumnNames)
    val constraintName by nullableWS(String::class, AddPrimaryKeyChange::setConstraintName)
    val schemaName by nullableWS(String::class, AddPrimaryKeyChange::setSchemaName)
    val tableName by nullableWS(String::class, AddPrimaryKeyChange::setTableName)
    val tablespace by nullableWS(String::class, AddPrimaryKeyChange::setTablespace)

    operator fun invoke(constraintName: String) = constraintName(constraintName)
}

open class LkAddUniqueConstraint : LkChange<LkAddUniqueConstraint, AddUniqueConstraintChange>(
    LkAddUniqueConstraint::class,
    ::AddUniqueConstraintChange
) {
    val catalogName by nullableWS(String::class, AddUniqueConstraintChange::setCatalogName)
    val columnNames by nullableWS(String::class, AddUniqueConstraintChange::setColumnNames)
    val constraintName by nullableWS(String::class, AddUniqueConstraintChange::setConstraintName)
    val deferrable by nullableWS(Boolean::class, AddUniqueConstraintChange::setDeferrable)
    val disabled by nullableWS(Boolean::class, AddUniqueConstraintChange::setDisabled)
    val initiallyDeferred by nullableWS(Boolean::class, AddUniqueConstraintChange::setInitiallyDeferred)
    val schemaName by nullableWS(String::class, AddUniqueConstraintChange::setSchemaName)
    val tableName by nullableWS(String::class, AddUniqueConstraintChange::setTableName)
    val tablespace by nullableWS(String::class, AddUniqueConstraintChange::setTablespace)

    operator fun invoke(constraintName: String) = constraintName(constraintName)
    fun at(tableName: String, vararg columnNames: String) =
        if (columnNames.isNotEmpty())
            tableName(tableName).columnNames(columnNames.joinToString())
        else
            throw IllegalArgumentException("Referenced columns cannot be empty.")
}

open class LkCreateIndex : LkChange<LkCreateIndex, CreateIndexChange>(
    LkCreateIndex::class,
    ::CreateIndexChange
) {
    val catalogName by nullableWS(String::class, CreateIndexChange::setCatalogName)
    val indexName by nullableWS(String::class, CreateIndexChange::setIndexName)
    val schemaName by nullableWS(String::class, CreateIndexChange::setSchemaName)
    val tableName by nullableWS(String::class, CreateIndexChange::setTableName)
    val tablespace by nullableWS(String::class, CreateIndexChange::setTablespace)
    val unique by nullableWS(Boolean::class, CreateIndexChange::setUnique)

    val column by child(::LbAddIndexColumnConfig)

    operator fun invoke(indexName: String) = indexName(indexName)

    class LbAddIndexColumnConfig : LkBaseAddColumnConfig<LbAddIndexColumnConfig, CreateIndexChange>(
        LbAddIndexColumnConfig::class,
        { change, it, _ -> change.addColumn(it) }
    )
}

open class LkCreateProcedure : LkChange<LkCreateProcedure, CreateProcedureChange>(
    LkCreateProcedure::class,
    ::CreateProcedureChange
) {
    val catalogName by nullableWS(String::class, CreateProcedureChange::setCatalogName)
    val comments by nullableWS(String::class, CreateProcedureChange::setComments)
    val dbms by nullableWS(String::class, CreateProcedureChange::setDbms)
    val encoding by nullableWS(String::class, CreateProcedureChange::setEncoding)
    val path by nullableWS(String::class, CreateProcedureChange::setPath)
    val procedureName by nullableWS(String::class, CreateProcedureChange::setProcedureName)
    val procedureText by nullableWS(String::class, CreateProcedureChange::setProcedureText)
    val relativeToChangelogFile by nullableWS(Boolean::class, CreateProcedureChange::setRelativeToChangelogFile)
    val schemaName by nullableWS(String::class, CreateProcedureChange::setSchemaName)

    operator fun invoke(procedureName: String) = procedureName(procedureName)
    operator fun minus(procedureText: String): Any = procedureText(procedureText)
}

open class LkCreateSequence : LkChange<LkCreateSequence, CreateSequenceChange>(
    LkCreateSequence::class,
    ::CreateSequenceChange
) {
    val catalogName by nullableWS(String::class, CreateSequenceChange::setCatalogName)
    val cycle by nullableWS(Boolean::class, CreateSequenceChange::setCycle)
    val incrementBy by nullableWS(BigInteger::class, CreateSequenceChange::setIncrementBy)
    val maxValue by nullableWS(BigInteger::class, CreateSequenceChange::setMinValue)
    val minValue by nullableWS(BigInteger::class, CreateSequenceChange::setMaxValue)
    val ordered by nullableWS(Boolean::class, CreateSequenceChange::setOrdered)
    val schemaName by nullableWS(String::class, CreateSequenceChange::setSchemaName)
    val sequenceName by nullableWS(String::class, CreateSequenceChange::setSequenceName)
    val startValue by nullableWS(BigInteger::class, CreateSequenceChange::setStartValue)

    operator fun invoke(sequenceName: String) = sequenceName(sequenceName)
}

open class LkCreateTable : LkChange<LkCreateTable, CreateTableChange>(
    LkCreateTable::class,
    ::CreateTableChange
) {
    val catalogName by nullableWS(String::class, CreateTableChange::setCatalogName)
    val remarks by nullableWS(String::class, CreateTableChange::setRemarks)
    val schemaName by nullableWS(String::class, CreateTableChange::setSchemaName)
    val tableName by nullableWS(String::class, CreateTableChange::setTableName)
    val tablespace by nullableWS(String::class, CreateTableChange::setTablespace)

    val column by child(::CreateTableColumnConfig)

    class CreateTableColumnConfig : LkCommonColumnConfig<CreateTableColumnConfig, CreateTableChange>(
        CreateTableColumnConfig::class,
        { parent, column, _ -> parent.addColumn(column) })

    operator fun invoke(tableName: String) = tableName(tableName)
    operator fun invoke(tableName: String, vararg columns: Pair<String, String>) =
        tableName(tableName).apply {
            columns.forEach { (name, type) -> this.column(name).type(type) }
        }
}

open class LkCreateView : LkChange<LkCreateView, CreateViewChange>(
    LkCreateView::class,
    ::CreateViewChange
) {
    val catalogName by nullableWS(String::class, CreateViewChange::setCatalogName)
    val replaceIfExists by nullableWS(Boolean::class, CreateViewChange::setReplaceIfExists)
    val schemaName by nullableWS(String::class, CreateViewChange::setSchemaName)
    val selectQuery by nullableWS(String::class, CreateViewChange::setSelectQuery)
    val viewName by nullableWS(String::class, CreateViewChange::setViewName)

    operator fun invoke(viewName: String) = viewName(viewName)
    operator fun minus(selectQuery: String): Any = selectQuery(selectQuery)
}