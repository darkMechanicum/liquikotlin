package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.LbArg
import com.tsarev.liquikotlin.infrastructure.LiquibaseIntegrator
import com.tsarev.liquikotlin.infrastructure.PropertyMapping
import com.tsarev.liquikotlin.infrastructure.minus
import liquibase.change.AddColumnConfig
import liquibase.change.ColumnConfig
import liquibase.change.ConstraintsConfig
import liquibase.change.core.*
import liquibase.statement.DatabaseFunction
import liquibase.statement.SequenceNextValueFunction

open class BaseColumnConfigIntegration<NodeT : LkBaseColumnConfig<NodeT>, ColumnT : ColumnConfig, ParentT : Any>(
    linkedConstructor: () -> ColumnT,
    parentSetter: (ParentT, ColumnT, NodeT, LbArg?) -> Unit,
    vararg childMappings: PropertyMapping<NodeT, ColumnT, *>
) : LiquibaseIntegrator<NodeT, ColumnT, ParentT>(
    linkedConstructor,
    parentSetter,
    LkBaseColumnConfig<NodeT>::name - ColumnConfig::setName,
    LkBaseColumnConfig<NodeT>::type - ColumnConfig::setType,
    LkBaseColumnConfig<NodeT>::value - ColumnConfig::setValue,
    LkBaseColumnConfig<NodeT>::computed - ColumnConfig::setComputed,
    LkBaseColumnConfig<NodeT>::valueNumeric - ColumnConfig::setValueNumeric,
    LkBaseColumnConfig<NodeT>::valueBoolean - ColumnConfig::setValueBoolean,
    LkBaseColumnConfig<NodeT>::valueDate - ColumnConfig::setValueDate,
    LkBaseColumnConfig<NodeT>::valueComputed - { config: ColumnConfig, value ->
        config.setValueComputed(DatabaseFunction(value))
    },
    LkBaseColumnConfig<NodeT>::valueBlobFile - ColumnConfig::setValueBlobFile,
    LkBaseColumnConfig<NodeT>::valueClobFile - ColumnConfig::setValueClobFile,
    LkBaseColumnConfig<NodeT>::encoding - ColumnConfig::setEncoding,
    LkBaseColumnConfig<NodeT>::defaultValue - ColumnConfig::setDefaultValue,
    LkBaseColumnConfig<NodeT>::defaultValueNumeric - ColumnConfig::setDefaultValueNumeric,
    LkBaseColumnConfig<NodeT>::defaultValueBoolean - ColumnConfig::setDefaultValueBoolean,
    LkBaseColumnConfig<NodeT>::defaultValueDate - ColumnConfig::setDefaultValueDate,
    LkBaseColumnConfig<NodeT>::defaultValueComputed - { config: ColumnConfig, value ->
        config.setValueComputed(DatabaseFunction(value))
    },
    LkBaseColumnConfig<NodeT>::autoIncrement - ColumnConfig::setAutoIncrement,
    LkBaseColumnConfig<NodeT>::remarks - ColumnConfig::setRemarks
) { init {
    propertyMappings.addAll(childMappings)
}
}

open class AddColumnConfigIntegration<ParentT : Any>(
    parentSetter: (ParentT, AddColumnConfig, LkAddColumnConfig, LbArg?) -> Unit,
    vararg childMappings: PropertyMapping<LkAddColumnConfig, AddColumnConfig, *>
) : BaseColumnConfigIntegration<LkAddColumnConfig, AddColumnConfig, ParentT>(
    ::AddColumnConfig,
    parentSetter,
    LkAddColumnConfig::beforeColumn - AddColumnConfig::setBeforeColumn,
    LkAddColumnConfig::afterColumn - AddColumnConfig::setAfterColumn,
    LkAddColumnConfig::position - AddColumnConfig::setPosition
) { init {
    propertyMappings.addAll(childMappings)
}
}

open class LoadColumnConfigIntegration<ParentT : Any>(
    parentSetter: (ParentT, LoadDataColumnConfig, LkLoadColumnConfig, LbArg?) -> Unit,
    vararg childMappings: PropertyMapping<LkLoadColumnConfig, LoadDataColumnConfig, *>
) : BaseColumnConfigIntegration<LkLoadColumnConfig, LoadDataColumnConfig, ParentT>(
    ::LoadDataColumnConfig,
    parentSetter,
    LkLoadColumnConfig::header - LoadDataColumnConfig::setHeader,
    LkLoadColumnConfig::index - LoadDataColumnConfig::setIndex
) { init {
    propertyMappings.addAll(childMappings)
}
}

open class CommonColumnConfigIntegration<ParentT : Any>(
    parentSetter: (ParentT, ColumnConfig, LkCommonColumnConfig, LbArg?) -> Unit,
    vararg childMappings: PropertyMapping<LkCommonColumnConfig, ColumnConfig, *>
) : BaseColumnConfigIntegration<LkCommonColumnConfig, ColumnConfig, ParentT>(
    ::ColumnConfig,
    parentSetter
) { init {
    propertyMappings.addAll(childMappings)
}
}

open class AddAutoIncrementIntegration : ChangeIntegration<LkAddAutoIncrement, AddAutoIncrementChange>(
    ::AddAutoIncrementChange,
    LkAddAutoIncrement::catalogName - AddAutoIncrementChange::setCatalogName,
    LkAddAutoIncrement::columnDataType - AddAutoIncrementChange::setColumnDataType,
    LkAddAutoIncrement::columnName - AddAutoIncrementChange::setColumnName,
    LkAddAutoIncrement::incrementBy - AddAutoIncrementChange::setIncrementBy,
    LkAddAutoIncrement::schemaName - AddAutoIncrementChange::setSchemaName,
    LkAddAutoIncrement::startWith - AddAutoIncrementChange::setStartWith,
    LkAddAutoIncrement::tableName - AddAutoIncrementChange::setTableName
)

open class AddColumnIntegration : ChangeIntegration<LkAddColumn, AddColumnChange>(
    ::AddColumnChange,
    LkAddColumn::catalogName - AddColumnChange::setCatalogName,
    LkAddColumn::schemaName - AddColumnChange::setSchemaName,
    LkAddColumn::tableName - AddColumnChange::setTableName
)

open class ConstraintsIntegration : LiquibaseIntegrator<LkConstraints, ConstraintsConfig, ColumnConfig>(
    ::ConstraintsConfig,
    { columnConfig, constraintsConfig, _, _ -> columnConfig.constraints = constraintsConfig },
    LkConstraints::nullable - ConstraintsConfig::setNullable,
    LkConstraints::primaryKey - ConstraintsConfig::setPrimaryKey,
    LkConstraints::primaryKeyName - ConstraintsConfig::setPrimaryKeyName,
    LkConstraints::unique - ConstraintsConfig::setUnique,
    LkConstraints::uniqueConstraintName - ConstraintsConfig::setUniqueConstraintName,
    LkConstraints::references - ConstraintsConfig::setReferences,
    LkConstraints::foreignKeyName - ConstraintsConfig::setForeignKeyName,
    LkConstraints::deleteCascade - ConstraintsConfig::setDeleteCascade,
    LkConstraints::deferrable - ConstraintsConfig::setDeferrable,
    LkConstraints::initiallyDeferred - ConstraintsConfig::setInitiallyDeferred
)

open class AddDefaultValueIntegration : ChangeIntegration<LkAddDefaultValue, AddDefaultValueChange>(
    ::AddDefaultValueChange,
    LkAddDefaultValue::catalogName - AddDefaultValueChange::setCatalogName,
    LkAddDefaultValue::columnDataType - AddDefaultValueChange::setColumnDataType,
    LkAddDefaultValue::columnName - AddDefaultValueChange::setColumnName,
    LkAddDefaultValue::defaultValue - AddDefaultValueChange::setDefaultValue,
    LkAddDefaultValue::defaultValueBoolean - AddDefaultValueChange::setDefaultValueBoolean,
    LkAddDefaultValue::defaultValueComputed - { change: AddDefaultValueChange, text ->
        change.setDefaultValueComputed(DatabaseFunction(text))
    },
    LkAddDefaultValue::defaultValueDate - AddDefaultValueChange::setDefaultValueDate,
    LkAddDefaultValue::defaultValueNumeric - AddDefaultValueChange::setDefaultValueNumeric,
    LkAddDefaultValue::defaultValueSequenceNext - { change: AddDefaultValueChange, text ->
        change.setDefaultValueSequenceNext(SequenceNextValueFunction(text))
    },
    LkAddDefaultValue::schemaName - AddDefaultValueChange::setSchemaName,
    LkAddDefaultValue::tableName - AddDefaultValueChange::setTableName
)

open class AddForeignKeyConstraintIntegration :
    ChangeIntegration<LkAddForeignKeyConstraint, AddForeignKeyConstraintChange>(
        ::AddForeignKeyConstraintChange,
        LkAddForeignKeyConstraint::baseColumnNames - AddForeignKeyConstraintChange::setBaseColumnNames,
        LkAddForeignKeyConstraint::baseTableCatalogName - AddForeignKeyConstraintChange::setBaseTableCatalogName,
        LkAddForeignKeyConstraint::baseTableName - AddForeignKeyConstraintChange::setBaseTableName,
        LkAddForeignKeyConstraint::baseTableSchemaName - AddForeignKeyConstraintChange::setBaseTableSchemaName,
        LkAddForeignKeyConstraint::constraintName - AddForeignKeyConstraintChange::setConstraintName,
        LkAddForeignKeyConstraint::deferrable - AddForeignKeyConstraintChange::setDeferrable,
        LkAddForeignKeyConstraint::initiallyDeferred - AddForeignKeyConstraintChange::setInitiallyDeferred,
        LkAddForeignKeyConstraint::onDelete - AddForeignKeyConstraintChange::setOnDelete,
        LkAddForeignKeyConstraint::onUpdate - AddForeignKeyConstraintChange::setOnUpdate,
        LkAddForeignKeyConstraint::referencedColumnNames - AddForeignKeyConstraintChange::setReferencedColumnNames,
        LkAddForeignKeyConstraint::referencedTableCatalogName - AddForeignKeyConstraintChange::setReferencedTableCatalogName,
        LkAddForeignKeyConstraint::referencedTableName - AddForeignKeyConstraintChange::setReferencedTableName,
        LkAddForeignKeyConstraint::referencedTableSchemaName - AddForeignKeyConstraintChange::setReferencedTableSchemaName,
        LkAddForeignKeyConstraint::referencesUniqueColumn - AddForeignKeyConstraintChange::setReferencesUniqueColumn
    )

open class AddLookupTableIntegration : ChangeIntegration<LkAddLookupTable, AddLookupTableChange>(
    ::AddLookupTableChange,
    LkAddLookupTable::constraintName - AddLookupTableChange::setConstraintName,
    LkAddLookupTable::existingColumnName - AddLookupTableChange::setExistingColumnName,
    LkAddLookupTable::existingTableCatalogName - AddLookupTableChange::setExistingTableCatalogName,
    LkAddLookupTable::existingTableName - AddLookupTableChange::setExistingTableName,
    LkAddLookupTable::existingTableSchemaName - AddLookupTableChange::setExistingTableSchemaName,
    LkAddLookupTable::newColumnDataType - AddLookupTableChange::setNewColumnDataType,
    LkAddLookupTable::newColumnName - AddLookupTableChange::setNewColumnName,
    LkAddLookupTable::newTableCatalogName - AddLookupTableChange::setNewTableCatalogName,
    LkAddLookupTable::newTableName - AddLookupTableChange::setNewTableName,
    LkAddLookupTable::newTableSchemaName - AddLookupTableChange::setNewTableSchemaName
)

open class AddNotNullConstraintIntegration : ChangeIntegration<LkAddNotNullConstraint, AddNotNullConstraintChange>(
    ::AddNotNullConstraintChange,
    LkAddNotNullConstraint::catalogName - AddNotNullConstraintChange::setCatalogName,
    LkAddNotNullConstraint::columnDataType - AddNotNullConstraintChange::setColumnDataType,
    LkAddNotNullConstraint::columnName - AddNotNullConstraintChange::setColumnName,
    LkAddNotNullConstraint::defaultNullValue - AddNotNullConstraintChange::setDefaultNullValue,
    LkAddNotNullConstraint::schemaName - AddNotNullConstraintChange::setSchemaName,
    LkAddNotNullConstraint::tableName - AddNotNullConstraintChange::setTableName
)

open class AddPrimaryKeyIntegration : ChangeIntegration<LkAddPrimaryKey, AddPrimaryKeyChange>(
    ::AddPrimaryKeyChange,
    LkAddPrimaryKey::catalogName - AddPrimaryKeyChange::setCatalogName,
    LkAddPrimaryKey::columnNames - AddPrimaryKeyChange::setColumnNames,
    LkAddPrimaryKey::constraintName - AddPrimaryKeyChange::setConstraintName,
    LkAddPrimaryKey::schemaName - AddPrimaryKeyChange::setSchemaName,
    LkAddPrimaryKey::tableName - AddPrimaryKeyChange::setTableName,
    LkAddPrimaryKey::tablespace - AddPrimaryKeyChange::setTablespace
)

open class AddUniqueConstraintIntegration : ChangeIntegration<LkAddUniqueConstraint, AddUniqueConstraintChange>(
    ::AddUniqueConstraintChange,
    LkAddUniqueConstraint::catalogName - AddUniqueConstraintChange::setCatalogName,
    LkAddUniqueConstraint::columnNames - AddUniqueConstraintChange::setColumnNames,
    LkAddUniqueConstraint::constraintName - AddUniqueConstraintChange::setConstraintName,
    LkAddUniqueConstraint::deferrable - AddUniqueConstraintChange::setDeferrable,
    LkAddUniqueConstraint::disabled - AddUniqueConstraintChange::setDisabled,
    LkAddUniqueConstraint::initiallyDeferred - AddUniqueConstraintChange::setInitiallyDeferred,
    LkAddUniqueConstraint::schemaName - AddUniqueConstraintChange::setSchemaName,
    LkAddUniqueConstraint::tableName - AddUniqueConstraintChange::setTableName,
    LkAddUniqueConstraint::tablespace - AddUniqueConstraintChange::setTablespace
)

open class CreateIndexIntegration : ChangeIntegration<LkCreateIndex, CreateIndexChange>(
    ::CreateIndexChange,
    LkCreateIndex::catalogName - CreateIndexChange::setCatalogName,
    LkCreateIndex::indexName - CreateIndexChange::setIndexName,
    LkCreateIndex::schemaName - CreateIndexChange::setSchemaName,
    LkCreateIndex::tableName - CreateIndexChange::setTableName,
    LkCreateIndex::tablespace - CreateIndexChange::setTablespace,
    LkCreateIndex::unique - CreateIndexChange::setUnique
)

open class CreateProcedureIntegration : ChangeIntegration<LkCreateProcedure, CreateProcedureChange>(
    ::CreateProcedureChange,
    LkCreateProcedure::catalogName - CreateProcedureChange::setCatalogName,
    LkCreateProcedure::comments - CreateProcedureChange::setComments,
    LkCreateProcedure::dbms - CreateProcedureChange::setDbms,
    LkCreateProcedure::encoding - CreateProcedureChange::setEncoding,
    LkCreateProcedure::path - CreateProcedureChange::setPath,
    LkCreateProcedure::procedureName - CreateProcedureChange::setProcedureName,
    LkCreateProcedure::procedureText - CreateProcedureChange::setProcedureText,
    LkCreateProcedure::relativeToChangelogFile - CreateProcedureChange::setRelativeToChangelogFile,
    LkCreateProcedure::schemaName - CreateProcedureChange::setSchemaName
)

open class CreateSequenceIntegration : ChangeIntegration<LkCreateSequence, CreateSequenceChange>(
    ::CreateSequenceChange,
    LkCreateSequence::catalogName - CreateSequenceChange::setCatalogName,
    LkCreateSequence::cycle - CreateSequenceChange::setCycle,
    LkCreateSequence::incrementBy - CreateSequenceChange::setIncrementBy,
    LkCreateSequence::maxValue - CreateSequenceChange::setMaxValue,
    LkCreateSequence::minValue - CreateSequenceChange::setMinValue,
    LkCreateSequence::ordered - CreateSequenceChange::setOrdered,
    LkCreateSequence::schemaName - CreateSequenceChange::setSchemaName,
    LkCreateSequence::sequenceName - CreateSequenceChange::setSequenceName,
    LkCreateSequence::startValue - CreateSequenceChange::setStartValue
)

open class CreateTableIntegration : ChangeIntegration<LkCreateTable, CreateTableChange>(
    ::CreateTableChange,
    LkCreateTable::catalogName - CreateTableChange::setCatalogName,
    LkCreateTable::remarks - CreateTableChange::setRemarks,
    LkCreateTable::schemaName - CreateTableChange::setSchemaName,
    LkCreateTable::tableName - CreateTableChange::setTableName,
    LkCreateTable::tablespace - CreateTableChange::setTablespace
)

open class CreateViewIntegration : ChangeIntegration<LkCreateView, CreateViewChange>(
    ::CreateViewChange,
    LkCreateView::catalogName - CreateViewChange::setCatalogName,
    LkCreateView::replaceIfExists - CreateViewChange::setReplaceIfExists,
    LkCreateView::schemaName - CreateViewChange::setSchemaName,
    LkCreateView::selectQuery - CreateViewChange::setSelectQuery,
    LkCreateView::viewName - CreateViewChange::setViewName
)