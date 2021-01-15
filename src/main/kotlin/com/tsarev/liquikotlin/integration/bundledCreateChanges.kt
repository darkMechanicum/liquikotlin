package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.*
import com.tsarev.liquikotlin.infrastructure.default.DefaultNode
import liquibase.change.AddColumnConfig
import liquibase.change.ColumnConfig
import liquibase.change.ConstraintsConfig
import liquibase.change.core.*
import liquibase.statement.DatabaseFunction
import liquibase.statement.SequenceCurrentValueFunction
import liquibase.statement.SequenceNextValueFunction

open class BaseColumnConfigIntegration<SelfT : LkBaseColumnConfig<SelfT>, ColumnT : ColumnConfig, ParentT : Any>(
    linkedConstructor: () -> ColumnT,
    linkedClass: Class<ColumnT>,
    parentSetter: (ParentT, ColumnT?, DefaultNode, LbArg?) -> Unit,
    vararg childMappings: PropertyMapping<DefaultNode, ColumnT, *>
) : LiquibaseIntegrator<ColumnT, ParentT>(
    linkedConstructor,
    linkedClass,
    parentSetter,
    LkBaseColumnConfig<SelfT>::name - ColumnConfig::setName,
    LkBaseColumnConfig<SelfT>::type - ColumnConfig::setType,
    LkBaseColumnConfig<SelfT>::value - ColumnConfig::setValue,
    LkBaseColumnConfig<SelfT>::computed - ColumnConfig::setComputed,
    LkBaseColumnConfig<SelfT>::valueNumeric - ColumnConfig::setValueNumeric,
    LkBaseColumnConfig<SelfT>::valueBoolean - ColumnConfig::setValueBoolean,
    LkBaseColumnConfig<SelfT>::valueDate - ColumnConfig::setValueDate,
    LkBaseColumnConfig<SelfT>::valueComputed - { config: ColumnConfig, value ->
        config.setValueComputed(DatabaseFunction(value))
    },
    LkBaseColumnConfig<SelfT>::valueBlobFile - ColumnConfig::setValueBlobFile,
    LkBaseColumnConfig<SelfT>::valueClobFile - ColumnConfig::setValueClobFile,
    LkBaseColumnConfig<SelfT>::encoding - ColumnConfig::setEncoding,
    LkBaseColumnConfig<SelfT>::defaultValue - ColumnConfig::setDefaultValue,
    LkBaseColumnConfig<SelfT>::defaultValueNumeric - ColumnConfig::setDefaultValueNumeric,
    LkBaseColumnConfig<SelfT>::defaultValueBoolean - ColumnConfig::setDefaultValueBoolean,
    LkBaseColumnConfig<SelfT>::defaultValueDate - ColumnConfig::setDefaultValueDate,
    LkBaseColumnConfig<SelfT>::defaultValueComputed - { config: ColumnConfig, value ->
        config.setDefaultValueComputed(DatabaseFunction(value))
    },
    LkBaseColumnConfig<SelfT>::autoIncrement - ColumnConfig::setAutoIncrement,
    LkBaseColumnConfig<SelfT>::remarks - ColumnConfig::setRemarks,
    LkBaseColumnConfig<SelfT>::valueSequenceNext - { config, seqName ->
        config.setValueSequenceNext(SequenceNextValueFunction(seqName))
    },
    LkBaseColumnConfig<SelfT>::valueSequenceCurrent - { config, seqName ->
        config.setValueSequenceCurrent(SequenceCurrentValueFunction(seqName))
    },
    LkBaseColumnConfig<SelfT>::defaultValueSequenceNext - { config, seqName ->
        config.setDefaultValueSequenceNext(SequenceNextValueFunction(seqName))
    },
    LkBaseColumnConfig<SelfT>::startWith - ColumnConfig::setStartWith,
    LkBaseColumnConfig<SelfT>::incrementBy - ColumnConfig::setIncrementBy,
    LkBaseColumnConfig<SelfT>::descending - ColumnConfig::setDescending
) { init {
    propertyMappings.addAll(childMappings)
}
}

open class AddColumnConfigIntegration<ParentT : Any>(
    parentSetter: (ParentT, AddColumnConfig?, DefaultNode, LbArg?) -> Unit,
    vararg childMappings: PropertyMapping<DefaultNode, AddColumnConfig, *>
) : BaseColumnConfigIntegration<LkAddColumnConfig, AddColumnConfig, ParentT>(
    ::AddColumnConfig,
    AddColumnConfig::class.java,
    parentSetter,
    LkAddColumnConfig::beforeColumn - AddColumnConfig::setBeforeColumn,
    LkAddColumnConfig::afterColumn - AddColumnConfig::setAfterColumn,
    LkAddColumnConfig::position - AddColumnConfig::setPosition
) { init {
    propertyMappings.addAll(childMappings)
}
}

open class LoadColumnConfigIntegration<ParentT : Any>(
    parentSetter: (ParentT, LoadDataColumnConfig?, DefaultNode, LbArg?) -> Unit,
    vararg childMappings: PropertyMapping<DefaultNode, LoadDataColumnConfig, *>
) : BaseColumnConfigIntegration<LkLoadColumnConfig, LoadDataColumnConfig, ParentT>(
    ::LoadDataColumnConfig,
    LoadDataColumnConfig::class.java,
    parentSetter,
    LkLoadColumnConfig::header - LoadDataColumnConfig::setHeader,
    LkLoadColumnConfig::index - LoadDataColumnConfig::setIndex
) { init {
    propertyMappings.addAll(childMappings)
}
}

open class CommonColumnConfigIntegration<ParentT : Any>(
    parentSetter: (ParentT, ColumnConfig?, DefaultNode, LbArg?) -> Unit,
    vararg childMappings: PropertyMapping<DefaultNode, ColumnConfig, *>
) : BaseColumnConfigIntegration<LkCommonColumnConfig, ColumnConfig, ParentT>(
    ::ColumnConfig,
    ColumnConfig::class.java,
    parentSetter
) { init {
    propertyMappings.addAll(childMappings)
}
}

open class AddAutoIncrementIntegration : ChangeIntegration<AddAutoIncrementChange>(
    ::AddAutoIncrementChange,
    AddAutoIncrementChange::class.java,
    LkAddAutoIncrement::catalogName - AddAutoIncrementChange::setCatalogName,
    LkAddAutoIncrement::columnDataType - AddAutoIncrementChange::setColumnDataType,
    LkAddAutoIncrement::columnName notNull AddAutoIncrementChange::setColumnName,
    LkAddAutoIncrement::incrementBy - AddAutoIncrementChange::setIncrementBy,
    LkAddAutoIncrement::schemaName - AddAutoIncrementChange::setSchemaName,
    LkAddAutoIncrement::startWith - AddAutoIncrementChange::setStartWith,
    LkAddAutoIncrement::tableName notNull AddAutoIncrementChange::setTableName
)

open class AddColumnIntegration : ChangeIntegration<AddColumnChange>(
    ::AddColumnChange,
    AddColumnChange::class.java,
    LkAddColumn::catalogName - AddColumnChange::setCatalogName,
    LkAddColumn::schemaName - AddColumnChange::setSchemaName,
    LkAddColumn::tableName notNull AddColumnChange::setTableName
)

open class ConstraintsIntegration : LiquibaseIntegrator<ConstraintsConfig, ColumnConfig>(
    ::ConstraintsConfig,
    ConstraintsConfig::class.java,
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
    LkConstraints::initiallyDeferred - ConstraintsConfig::setInitiallyDeferred,
    LkConstraints::primaryKeyTablespace - ConstraintsConfig::setPrimaryKeyTablespace,
    LkConstraints::referencedTableName - ConstraintsConfig::setReferencedTableName,
    LkConstraints::referencedColumnNames - ConstraintsConfig::setReferencedColumnNames,
    LkConstraints::checkConstraint - ConstraintsConfig::setCheckConstraint
)

@Suppress("UsePropertyAccessSyntax")
open class AddDefaultValueIntegration : ChangeIntegration<AddDefaultValueChange>(
    ::AddDefaultValueChange,
    AddDefaultValueChange::class.java,
    LkAddDefaultValue::catalogName - AddDefaultValueChange::setCatalogName,
    LkAddDefaultValue::columnDataType - AddDefaultValueChange::setColumnDataType,
    LkAddDefaultValue::columnName - AddDefaultValueChange::setColumnName,
    LkAddDefaultValue::defaultValue - AddDefaultValueChange::setDefaultValue,
    LkAddDefaultValue::defaultValueBoolean - AddDefaultValueChange::setDefaultValueBoolean,
    LkAddDefaultValue::defaultValueComputed - { change: AddDefaultValueChange, text ->
        change.setDefaultValueComputed(DatabaseFunction(text))
    },
    LkAddDefaultValue::defaultValueDate - { change: AddDefaultValueChange, value -> change.setDefaultValueDate("$value") },
    LkAddDefaultValue::defaultValueNumeric - { change: AddDefaultValueChange, value -> change.setDefaultValueNumeric("$value") },
    LkAddDefaultValue::defaultValueSequenceNext - { change: AddDefaultValueChange, text ->
        change.setDefaultValueSequenceNext(SequenceNextValueFunction(text))
    },
    LkAddDefaultValue::schemaName - AddDefaultValueChange::setSchemaName,
    LkAddDefaultValue::tableName - AddDefaultValueChange::setTableName
)

open class AddForeignKeyConstraintIntegration :
    ChangeIntegration<AddForeignKeyConstraintChange>(
        ::AddForeignKeyConstraintChange,
        AddForeignKeyConstraintChange::class.java,
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

open class AddLookupTableIntegration : ChangeIntegration<AddLookupTableChange>(
    ::AddLookupTableChange,
    AddLookupTableChange::class.java,
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

open class AddNotNullConstraintIntegration : ChangeIntegration<AddNotNullConstraintChange>(
    ::AddNotNullConstraintChange,
    AddNotNullConstraintChange::class.java,
    LkAddNotNullConstraint::catalogName - AddNotNullConstraintChange::setCatalogName,
    LkAddNotNullConstraint::columnDataType - AddNotNullConstraintChange::setColumnDataType,
    LkAddNotNullConstraint::columnName - AddNotNullConstraintChange::setColumnName,
    LkAddNotNullConstraint::defaultNullValue - AddNotNullConstraintChange::setDefaultNullValue,
    LkAddNotNullConstraint::schemaName - AddNotNullConstraintChange::setSchemaName,
    LkAddNotNullConstraint::tableName - AddNotNullConstraintChange::setTableName,
    LkAddNotNullConstraint::constraintName - AddNotNullConstraintChange::setConstraintName
)

open class AddPrimaryKeyIntegration : ChangeIntegration<AddPrimaryKeyChange>(
    ::AddPrimaryKeyChange,
    AddPrimaryKeyChange::class.java,
    LkAddPrimaryKey::catalogName - AddPrimaryKeyChange::setCatalogName,
    LkAddPrimaryKey::columnNames - AddPrimaryKeyChange::setColumnNames,
    LkAddPrimaryKey::constraintName - AddPrimaryKeyChange::setConstraintName,
    LkAddPrimaryKey::schemaName - AddPrimaryKeyChange::setSchemaName,
    LkAddPrimaryKey::tableName - AddPrimaryKeyChange::setTableName,
    LkAddPrimaryKey::tablespace - AddPrimaryKeyChange::setTablespace,
    LkAddPrimaryKey::clustered - AddPrimaryKeyChange::setClustered,
    LkAddPrimaryKey::forIndexName - AddPrimaryKeyChange::setForIndexName,
    LkAddPrimaryKey::forIndexSchemaName - AddPrimaryKeyChange::setForIndexSchemaName,
    LkAddPrimaryKey::forIndexCatalogName - AddPrimaryKeyChange::setForIndexCatalogName
)

open class AddUniqueConstraintIntegration : ChangeIntegration<AddUniqueConstraintChange>(
    ::AddUniqueConstraintChange,
    AddUniqueConstraintChange::class.java,
    LkAddUniqueConstraint::catalogName - AddUniqueConstraintChange::setCatalogName,
    LkAddUniqueConstraint::columnNames - AddUniqueConstraintChange::setColumnNames,
    LkAddUniqueConstraint::constraintName - AddUniqueConstraintChange::setConstraintName,
    LkAddUniqueConstraint::deferrable - AddUniqueConstraintChange::setDeferrable,
    LkAddUniqueConstraint::disabled - AddUniqueConstraintChange::setDisabled,
    LkAddUniqueConstraint::initiallyDeferred - AddUniqueConstraintChange::setInitiallyDeferred,
    LkAddUniqueConstraint::schemaName - AddUniqueConstraintChange::setSchemaName,
    LkAddUniqueConstraint::tableName - AddUniqueConstraintChange::setTableName,
    LkAddUniqueConstraint::tablespace - AddUniqueConstraintChange::setTablespace,
    LkAddUniqueConstraint::forIndexName - AddUniqueConstraintChange::setForIndexName,
    LkAddUniqueConstraint::forIndexSchemaName - AddUniqueConstraintChange::setForIndexSchemaName,
    LkAddUniqueConstraint::forIndexCatalogName - AddUniqueConstraintChange::setForIndexCatalogName
)

open class CreateIndexIntegration : ChangeIntegration<CreateIndexChange>(
    ::CreateIndexChange,
    CreateIndexChange::class.java,
    LkCreateIndex::catalogName - CreateIndexChange::setCatalogName,
    LkCreateIndex::indexName - CreateIndexChange::setIndexName,
    LkCreateIndex::schemaName - CreateIndexChange::setSchemaName,
    LkCreateIndex::tableName - CreateIndexChange::setTableName,
    LkCreateIndex::tablespace - CreateIndexChange::setTablespace,
    LkCreateIndex::unique - CreateIndexChange::setUnique
)

open class CreateProcedureIntegration : ChangeIntegration<CreateProcedureChange>(
    ::CreateProcedureChange,
    CreateProcedureChange::class.java,
    LkCreateProcedure::catalogName - CreateProcedureChange::setCatalogName,
    LkCreateProcedure::comments - CreateProcedureChange::setComments,
    LkCreateProcedure::dbms - CreateProcedureChange::setDbms,
    LkCreateProcedure::encoding - CreateProcedureChange::setEncoding,
    LkCreateProcedure::path - CreateProcedureChange::setPath,
    LkCreateProcedure::procedureName - CreateProcedureChange::setProcedureName,
    LkCreateProcedure::procedureText - CreateProcedureChange::setProcedureText,
    LkCreateProcedure::relativeToChangelogFile - CreateProcedureChange::setRelativeToChangelogFile,
    LkCreateProcedure::schemaName - CreateProcedureChange::setSchemaName,
    LkCreateProcedure::replaceIfExists - CreateProcedureChange::setReplaceIfExists
)

open class CreateSequenceIntegration : ChangeIntegration<CreateSequenceChange>(
    ::CreateSequenceChange,
    CreateSequenceChange::class.java,
    LkCreateSequence::catalogName - CreateSequenceChange::setCatalogName,
    LkCreateSequence::cycle - CreateSequenceChange::setCycle,
    LkCreateSequence::incrementBy - CreateSequenceChange::setIncrementBy,
    LkCreateSequence::maxValue - CreateSequenceChange::setMaxValue,
    LkCreateSequence::minValue - CreateSequenceChange::setMinValue,
    LkCreateSequence::ordered - CreateSequenceChange::setOrdered,
    LkCreateSequence::schemaName - CreateSequenceChange::setSchemaName,
    LkCreateSequence::sequenceName - CreateSequenceChange::setSequenceName,
    LkCreateSequence::startValue - CreateSequenceChange::setStartValue,
    LkCreateSequence::cacheSize - CreateSequenceChange::setCacheSize
)

open class CreateTableIntegration : ChangeIntegration<CreateTableChange>(
    ::CreateTableChange,
    CreateTableChange::class.java,
    LkCreateTable::catalogName - CreateTableChange::setCatalogName,
    LkCreateTable::remarks - CreateTableChange::setRemarks,
    LkCreateTable::schemaName - CreateTableChange::setSchemaName,
    LkCreateTable::tableName - CreateTableChange::setTableName,
    LkCreateTable::tablespace - CreateTableChange::setTablespace
)

open class CreateViewIntegration : ChangeIntegration<CreateViewChange>(
    ::CreateViewChange,
    CreateViewChange::class.java,
    LkCreateView::catalogName - CreateViewChange::setCatalogName,
    LkCreateView::replaceIfExists - CreateViewChange::setReplaceIfExists,
    LkCreateView::schemaName - CreateViewChange::setSchemaName,
    LkCreateView::selectQuery - CreateViewChange::setSelectQuery,
    LkCreateView::viewName - CreateViewChange::setViewName,
    LkCreateView::fullDefinition - CreateViewChange::setFullDefinition
)