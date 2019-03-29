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
    open val name by nullableWS(String::class, ColumnConfig::setName)
    open val type by nullableWS(String::class, ColumnConfig::setType)
    open val value by nullableWS(String::class, ColumnConfig::setValue)
    open val computed by nullableWS(Boolean::class, ColumnConfig::setComputed)
    open val valueNumeric by nullableWS(Number::class, ColumnConfig::setValueNumeric)
    open val valueBoolean by nullableWS(Boolean::class, ColumnConfig::setValueBoolean)
    open val valueDate by nullableWS(Date::class, ColumnConfig::setValueDate)
    open val valueComputed by nullableWS(Boolean::class, ColumnConfig::setComputed)
    open val valueBlobFile by nullableWS(String::class, ColumnConfig::setValueBlobFile)
    open val valueClobFile by nullableWS(String::class, ColumnConfig::setValueClobFile)
    open val encoding by nullableWS(String::class, ColumnConfig::setEncoding)
    open val defaultValue by nullableWS(String::class, ColumnConfig::setDefaultValue)
    open val defaultValueNumeric by nullableWS(Number::class, ColumnConfig::setDefaultValueNumeric)
    open val defaultValueBoolean by nullableWS(Boolean::class, ColumnConfig::setDefaultValueBoolean)
    open val defaultValueDate by nullableWS(Date::class, ColumnConfig::setDefaultValueDate)
    open val defaultValueComputed by nullableWS(
        String::class,
        { config, value -> config.setDefaultValueComputed(DatabaseFunction(value)) })
    open val autoIncrement by nullableWS(Boolean::class, ColumnConfig::setAutoIncrement)
    open val remarks by nullableWS(String::class, ColumnConfig::setRemarks)

    open val constraints by child(::LkConstraints)
}

abstract class LkBaseAddColumnConfig<SelfT : LkBaseAddColumnConfig<SelfT, ParentT>, ParentT>(
    selfClass: KClass<SelfT>,
    linkedSetter: (ParentT, AddColumnConfig, LkBaseAddColumnConfig<SelfT, ParentT>) -> Unit
) : LkBaseColumnConfig<SelfT, AddColumnConfig, ParentT>(
    selfClass,
    ::AddColumnConfig,
    linkedSetter
) {
    open val beforeColumn by nullableWS(String::class, AddColumnConfig::setBeforeColumn)
    open val afterColumn by nullableWS(String::class, AddColumnConfig::setAfterColumn)
    open val position by nullableWS(Int::class, AddColumnConfig::setPosition)
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
    open val catalogName by nullableWS(String::class, AddAutoIncrementChange::setCatalogName)
    open val columnDataType by nullableWS(String::class, AddAutoIncrementChange::setColumnDataType)
    open val columnName by nonNullableWS(String::class, AddAutoIncrementChange::setColumnName)
    open val incrementBy by nullableWS(BigInteger::class, AddAutoIncrementChange::setIncrementBy)
    open val schemaName by nullableWS(String::class, AddAutoIncrementChange::setSchemaName)
    open val startWith by nullableWS(BigInteger::class, AddAutoIncrementChange::setStartWith)
    open val tableName by nonNullableWS(String::class, AddAutoIncrementChange::setTableName)
}

open class LkAddColumn : LkChange<LkAddColumn, AddColumnChange>(
    LkAddColumn::class,
    ::AddColumnChange
) {
    open val catalogName by nullableWS(String::class, AddColumnChange::setCatalogName)
    open val schemaName by nullableWS(String::class, AddColumnChange::setSchemaName)
    open val tableName by nonNullableWS(String::class, AddColumnChange::setTableName)

    open val column by child(::LkAddColumnConfig)
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
    open val nullable by nullableWS(Boolean::class, ConstraintsConfig::setNullable)
    open val primaryKey by nullableWS(Boolean::class, ConstraintsConfig::setPrimaryKey)
    open val primaryKeyName by nullableWS(String::class, ConstraintsConfig::setPrimaryKeyName)
    open val unique by nullableWS(Boolean::class, ConstraintsConfig::setUnique)
    open val uniqueConstraintName by nullableWS(String::class, ConstraintsConfig::setUniqueConstraintName)
    open val references by nullableWS(String::class, ConstraintsConfig::setReferences)
    open val foreignKeyName by nullableWS(String::class, ConstraintsConfig::setForeignKeyName)
    open val deleteCascade by nullableWS(Boolean::class, ConstraintsConfig::setDeleteCascade)
    open val deferrable by nullableWS(Boolean::class, ConstraintsConfig::setDeferrable)
    open val initiallyDeferred by nullableWS(Boolean::class, ConstraintsConfig::setInitiallyDeferred)
}

open class LkAddDefaultValue : LkChange<LkAddDefaultValue, AddDefaultValueChange>(
    LkAddDefaultValue::class,
    ::AddDefaultValueChange
) {
    open val catalogName by nullableWS(String::class, AddDefaultValueChange::setCatalogName)
    open val columnDataType by nullableWS(String::class, AddDefaultValueChange::setColumnDataType)
    open val columnName by nullableWS(String::class, AddDefaultValueChange::setColumnName)
    open val defaultValue by nullableWS(String::class, AddDefaultValueChange::setDefaultValue)
    open val defaultValueBoolean by nullableWS(Boolean::class, AddDefaultValueChange::setDefaultValueBoolean)
    open val defaultValueComputed by nullableWS(
        String::class,
        { change, value -> change.setDefaultValueComputed(DatabaseFunction(value)) })
    open val defaultValueDate by nullableWS(String::class, AddDefaultValueChange::setDefaultValueDate)
    open val defaultValueNumeric by nullableWS(String::class, AddDefaultValueChange::setDefaultValueNumeric)
    open val defaultValueSequenceNext by nullableWS(
        String::class,
        { change, value -> change.setDefaultValueSequenceNext(SequenceNextValueFunction(value)) })
    open val schemaName by nullableWS(String::class, AddDefaultValueChange::setSchemaName)
    open val tableName by nullableWS(String::class, AddDefaultValueChange::setTableName)
}

open class LkAddForeignKeyConstraint : LkChange<LkAddForeignKeyConstraint, AddForeignKeyConstraintChange>(
    LkAddForeignKeyConstraint::class,
    ::AddForeignKeyConstraintChange
) {
    open val baseColumnNames by nullableWS(String::class, AddForeignKeyConstraintChange::setBaseColumnNames)
    open val baseTableCatalogName by nullableWS(String::class, AddForeignKeyConstraintChange::setBaseTableCatalogName)
    open val baseTableName by nullableWS(String::class, AddForeignKeyConstraintChange::setBaseTableName)
    open val baseTableSchemaName by nullableWS(String::class, AddForeignKeyConstraintChange::setBaseTableSchemaName)
    open val constraintName by nullableWS(String::class, AddForeignKeyConstraintChange::setConstraintName)
    open val deferrable by nullableWS(Boolean::class, AddForeignKeyConstraintChange::setDeferrable)
    open val initiallyDeferred by nullableWS(Boolean::class, AddForeignKeyConstraintChange::setInitiallyDeferred)
    open val onDelete by nullableWS(String::class, AddForeignKeyConstraintChange::setOnDelete)
    open val onUpdate by nullableWS(String::class, AddForeignKeyConstraintChange::setOnUpdate)
    open val referencedColumnNames by nullableWS(String::class, AddForeignKeyConstraintChange::setReferencedColumnNames)
    open val referencedTableCatalogName by nullableWS(
        String::class,
        AddForeignKeyConstraintChange::setReferencedTableCatalogName
    )
    open val referencedTableName by nullableWS(String::class, AddForeignKeyConstraintChange::setReferencedTableName)
    open val referencedTableSchemaName by nullableWS(
        String::class,
        AddForeignKeyConstraintChange::setReferencedTableSchemaName
    )
    open val referencesUniqueColumn by nullableWS(Boolean::class, AddForeignKeyConstraintChange::setReferencesUniqueColumn)
}

open class LkAddLookupTable : LkChange<LkAddLookupTable, AddLookupTableChange>(
    LkAddLookupTable::class,
    ::AddLookupTableChange
) {
    open val constraintName by nullableWS(String::class, AddLookupTableChange::setConstraintName)
    open val existingColumnName by nullableWS(String::class, AddLookupTableChange::setExistingColumnName)
    open val existingTableCatalogName by nullableWS(String::class, AddLookupTableChange::setExistingTableCatalogName)
    open val existingTableName by nullableWS(String::class, AddLookupTableChange::setExistingTableName)
    open val existingTableSchemaName by nullableWS(String::class, AddLookupTableChange::setExistingTableSchemaName)
    open val newColumnDataType by nullableWS(String::class, AddLookupTableChange::setNewColumnDataType)
    open val newColumnName by nullableWS(String::class, AddLookupTableChange::setNewColumnName)
    open val newTableCatalogName by nullableWS(String::class, AddLookupTableChange::setNewTableCatalogName)
    open val newTableName by nullableWS(String::class, AddLookupTableChange::setNewTableName)
    open val newTableSchemaName by nullableWS(String::class, AddLookupTableChange::setNewTableSchemaName)
}

open class LkAddNotNullConstraint : LkChange<LkAddNotNullConstraint, AddNotNullConstraintChange>(
    LkAddNotNullConstraint::class,
    ::AddNotNullConstraintChange
) {
    open val catalogName by nullableWS(String::class, AddNotNullConstraintChange::setCatalogName)
    open val columnDataType by nullableWS(String::class, AddNotNullConstraintChange::setColumnDataType)
    open val columnName by nullableWS(String::class, AddNotNullConstraintChange::setColumnName)
    open val defaultNullValue by nullableWS(String::class, AddNotNullConstraintChange::setDefaultNullValue)
    open val schemaName by nullableWS(String::class, AddNotNullConstraintChange::setSchemaName)
    open val tableName by nullableWS(String::class, AddNotNullConstraintChange::setTableName)
}

open class LkAddPrimaryKey : LkChange<LkAddPrimaryKey, AddPrimaryKeyChange>(
    LkAddPrimaryKey::class,
    ::AddPrimaryKeyChange
) {
    open val catalogName by nullableWS(String::class, AddPrimaryKeyChange::setCatalogName)
    open val columnNames by nullableWS(String::class, AddPrimaryKeyChange::setColumnNames)
    open val constraintName by nullableWS(String::class, AddPrimaryKeyChange::setConstraintName)
    open val schemaName by nullableWS(String::class, AddPrimaryKeyChange::setSchemaName)
    open val tableName by nullableWS(String::class, AddPrimaryKeyChange::setTableName)
    open val tablespace by nullableWS(String::class, AddPrimaryKeyChange::setTablespace)
}

open class LkAddUniqueConstraint : LkChange<LkAddUniqueConstraint, AddUniqueConstraintChange>(
    LkAddUniqueConstraint::class,
    ::AddUniqueConstraintChange
) {
    open val catalogName by nullableWS(String::class, AddUniqueConstraintChange::setCatalogName)
    open val columnNames by nullableWS(String::class, AddUniqueConstraintChange::setColumnNames)
    open val constraintName by nullableWS(String::class, AddUniqueConstraintChange::setConstraintName)
    open val deferrable by nullableWS(Boolean::class, AddUniqueConstraintChange::setDeferrable)
    open val disabled by nullableWS(Boolean::class, AddUniqueConstraintChange::setDisabled)
    open val initiallyDeferred by nullableWS(Boolean::class, AddUniqueConstraintChange::setInitiallyDeferred)
    open val schemaName by nullableWS(String::class, AddUniqueConstraintChange::setSchemaName)
    open val tableName by nullableWS(String::class, AddUniqueConstraintChange::setTableName)
    open val tablespace by nullableWS(String::class, AddUniqueConstraintChange::setTablespace)
}

open class LkCreateIndex : LkChange<LkCreateIndex, CreateIndexChange>(
    LkCreateIndex::class,
    ::CreateIndexChange
) {
    open val catalogName by nullableWS(String::class, CreateIndexChange::setCatalogName)
    open val indexName by nullableWS(String::class, CreateIndexChange::setIndexName)
    open val schemaName by nullableWS(String::class, CreateIndexChange::setSchemaName)
    open val tableName by nullableWS(String::class, CreateIndexChange::setTableName)
    open val tablespace by nullableWS(String::class, CreateIndexChange::setTablespace)
    open val unique by nullableWS(Boolean::class, CreateIndexChange::setUnique)

    open val column by child(::LbAddIndexColumnConfig)

    class LbAddIndexColumnConfig : LkBaseAddColumnConfig<LbAddIndexColumnConfig, CreateIndexChange>(
        LbAddIndexColumnConfig::class,
        { change, it, _ -> change.addColumn(it) }
    )
}

open class LkCreateProcedure : LkChange<LkCreateProcedure, CreateProcedureChange>(
    LkCreateProcedure::class,
    ::CreateProcedureChange
) {
    open val catalogName by nullableWS(String::class, CreateProcedureChange::setCatalogName)
    open val comments by nullableWS(String::class, CreateProcedureChange::setComments)
    open val dbms by nullableWS(String::class, CreateProcedureChange::setDbms)
    open val encoding by nullableWS(String::class, CreateProcedureChange::setEncoding)
    open val path by nullableWS(String::class, CreateProcedureChange::setPath)
    open val procedureName by nullableWS(String::class, CreateProcedureChange::setProcedureName)
    open val procedureText by nullableWS(String::class, CreateProcedureChange::setProcedureText)
    open val relativeToChangelogFile by nullableWS(Boolean::class, CreateProcedureChange::setRelativeToChangelogFile)
    open val schemaName by nullableWS(String::class, CreateProcedureChange::setSchemaName)
}

open class LkCreateSequence : LkChange<LkCreateSequence, CreateSequenceChange>(
    LkCreateSequence::class,
    ::CreateSequenceChange
) {
    open val catalogName by nullableWS(String::class, CreateSequenceChange::setCatalogName)
    open val cycle by nullableWS(Boolean::class, CreateSequenceChange::setCycle)
    open val incrementBy by nullableWS(BigInteger::class, CreateSequenceChange::setIncrementBy)
    open val maxValue by nullableWS(BigInteger::class, CreateSequenceChange::setMinValue)
    open val minValue by nullableWS(BigInteger::class, CreateSequenceChange::setMaxValue)
    open val ordered by nullableWS(Boolean::class, CreateSequenceChange::setOrdered)
    open val schemaName by nullableWS(String::class, CreateSequenceChange::setSchemaName)
    open val sequenceName by nullableWS(String::class, CreateSequenceChange::setSequenceName)
    open val startValue by nullableWS(BigInteger::class, CreateSequenceChange::setStartValue)
}

open class LkCreateTable : LkChange<LkCreateTable, CreateTableChange>(
    LkCreateTable::class,
    ::CreateTableChange
) {
    open val catalogName by nullableWS(String::class, CreateTableChange::setCatalogName)
    open val remarks by nullableWS(String::class, CreateTableChange::setRemarks)
    open val schemaName by nullableWS(String::class, CreateTableChange::setSchemaName)
    open val tableName by nullableWS(String::class, CreateTableChange::setTableName)
    open val tablespace by nullableWS(String::class, CreateTableChange::setTablespace)

    open val column by child(::CreateTableColumnConfig)

    class CreateTableColumnConfig : LkCommonColumnConfig<CreateTableColumnConfig, CreateTableChange>(
        CreateTableColumnConfig::class,
        { parent, column, _ -> parent.addColumn(column) })
}

open class LkCreateView : LkChange<LkCreateView, CreateViewChange>(
    LkCreateView::class,
    ::CreateViewChange
) {
    open val catalogName by nullableWS(String::class, CreateViewChange::setCatalogName)
    open val replaceIfExists by nullableWS(Boolean::class, CreateViewChange::setReplaceIfExists)
    open val schemaName by nullableWS(String::class, CreateViewChange::setSchemaName)
    open val selectQuery by nullableWS(String::class, CreateViewChange::setSelectQuery)
    open val viewName by nullableWS(String::class, CreateViewChange::setViewName)
}