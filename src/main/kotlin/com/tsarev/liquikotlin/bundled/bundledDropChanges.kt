package com.tsarev.liquikotlin.bundled

import liquibase.change.core.*

open class LkDelete : LkChange<LkDelete, DeleteDataChange>(
    LkDelete::class,
    ::DeleteDataChange
) {
    open val catalogName by nullableWS(String::class, DeleteDataChange::setCatalogName)
    open val schemaName by nullableWS(String::class, DeleteDataChange::setSchemaName)
    open val tableName by nullableWS(String::class, DeleteDataChange::setTableName)
    open val where by nullableWS(String::class, DeleteDataChange::setWhere)
}

open class LkDropAllForeignKeyConstraints :
    LkChange<LkDropAllForeignKeyConstraints, DropAllForeignKeyConstraintsChange>(
        LkDropAllForeignKeyConstraints::class,
        ::DropAllForeignKeyConstraintsChange
    ) {
    open val baseTableCatalogName by nullableWS(String::class, DropAllForeignKeyConstraintsChange::setBaseTableCatalogName)
    open val baseTableName by nullableWS(String::class, DropAllForeignKeyConstraintsChange::setBaseTableName)
    open val baseTableSchemaName by nullableWS(String::class, DropAllForeignKeyConstraintsChange::setBaseTableSchemaName)
}

open class LkDropColumn : LkChange<LkDropColumn, DropColumnChange>(
    LkDropColumn::class,
    ::DropColumnChange
) {
    open val catalogName by nullableWS(String::class, DropColumnChange::setCatalogName)
    open val columnName by nullableWS(String::class, DropColumnChange::setColumnName)
    open val schemaName by nullableWS(String::class, DropColumnChange::setSchemaName)
    open val tableName by nullableWS(String::class, DropColumnChange::setTableName)
}

open class LkDropDefaultValue : LkChange<LkDropDefaultValue, DropDefaultValueChange>(
    LkDropDefaultValue::class,
    ::DropDefaultValueChange
) {
    open val catalogName by nullableWS(String::class, DropDefaultValueChange::setCatalogName)
    open val columnDataType by nullableWS(String::class, DropDefaultValueChange::setColumnDataType)
    open val columnName by nullableWS(String::class, DropDefaultValueChange::setColumnName)
    open val schemaName by nullableWS(String::class, DropDefaultValueChange::setSchemaName)
    open val tableName by nullableWS(String::class, DropDefaultValueChange::setTableName)
}

open class LkDropForeignKeyConstraint : LkChange<LkDropForeignKeyConstraint, DropForeignKeyConstraintChange>(
    LkDropForeignKeyConstraint::class,
    ::DropForeignKeyConstraintChange
) {
    open val baseTableCatalogName by nullableWS(String::class, DropForeignKeyConstraintChange::setBaseTableCatalogName)
    open val baseTableName by nullableWS(String::class, DropForeignKeyConstraintChange::setBaseTableName)
    open val baseTableSchemaName by nullableWS(String::class, DropForeignKeyConstraintChange::setBaseTableSchemaName)
    open val constraintName by nullableWS(String::class, DropForeignKeyConstraintChange::setConstraintName)
}

open class LkDropIndex : LkChange<LkDropIndex, DropIndexChange>(
    LkDropIndex::class,
    ::DropIndexChange
) {
    open val catalogName by nullableWS(String::class, DropIndexChange::setCatalogName)
    open val indexName by nullableWS(String::class, DropIndexChange::setIndexName)
    open val schemaName by nullableWS(String::class, DropIndexChange::setSchemaName)
    open val tableName by nullableWS(String::class, DropIndexChange::setTableName)
}

open class LkDropNotNullConstraint : LkChange<LkDropNotNullConstraint, DropNotNullConstraintChange>(
    LkDropNotNullConstraint::class,
    ::DropNotNullConstraintChange
) {
    open val catalogName by nullableWS(String::class, DropNotNullConstraintChange::setCatalogName)
    open val columnDataType by nullableWS(String::class, DropNotNullConstraintChange::setColumnDataType)
    open val columnName by nullableWS(String::class, DropNotNullConstraintChange::setColumnName)
    open val schemaName by nullableWS(String::class, DropNotNullConstraintChange::setSchemaName)
    open val tableName by nullableWS(String::class, DropNotNullConstraintChange::setTableName)
}

open class LkDropPrimaryKey : LkChange<LkDropPrimaryKey, DropPrimaryKeyChange>(
    LkDropPrimaryKey::class,
    ::DropPrimaryKeyChange
) {
    open val catalogName by nullableWS(String::class, DropPrimaryKeyChange::setCatalogName)
    open val constraintName by nullableWS(String::class, DropPrimaryKeyChange::setConstraintName)
    open val schemaName by nullableWS(String::class, DropPrimaryKeyChange::setSchemaName)
    open val tableName by nullableWS(String::class, DropPrimaryKeyChange::setTableName)
}

open class LkDropProcedure : LkChange<LkDropProcedure, DropProcedureChange>(
    LkDropProcedure::class,
    ::DropProcedureChange
) {
    open val catalogName by nullableWS(String::class, DropProcedureChange::setCatalogName)
    open val procedureName by nullableWS(String::class, DropProcedureChange::setProcedureName)
    open val schemaName by nullableWS(String::class, DropProcedureChange::setSchemaName)
}

open class LkDropSequence : LkChange<LkDropSequence, DropSequenceChange>(
    LkDropSequence::class,
    ::DropSequenceChange
) {
    open val catalogName by nullableWS(String::class, DropSequenceChange::setCatalogName)
    open val schemaName by nullableWS(String::class, DropSequenceChange::setSchemaName)
    open val sequenceName by nullableWS(String::class, DropSequenceChange::setSequenceName)
}

open class LkDropTable : LkChange<LkDropTable, DropTableChange>(
    LkDropTable::class,
    ::DropTableChange
) {
    open val cascadeConstraints by nullableWS(Boolean::class, DropTableChange::setCascadeConstraints)
    open val catalogName by nullableWS(String::class, DropTableChange::setCatalogName)
    open val schemaName by nullableWS(String::class, DropTableChange::setSchemaName)
    open val tableName by nullableWS(String::class, DropTableChange::setTableName)
}

open class LkDropUniqueConstraint : LkChange<LkDropUniqueConstraint, DropUniqueConstraintChange>(
    LkDropUniqueConstraint::class,
    ::DropUniqueConstraintChange
) {
    open val catalogName by nullableWS(String::class, DropUniqueConstraintChange::setCatalogName)
    open val constraintName by nullableWS(String::class, DropUniqueConstraintChange::setConstraintName)
    open val schemaName by nullableWS(String::class, DropUniqueConstraintChange::setSchemaName)
    open val tableName by nullableWS(String::class, DropUniqueConstraintChange::setTableName)
    open val uniqueColumns by nullableWS(String::class, DropUniqueConstraintChange::setUniqueColumns)
}

open class LkDropView : LkChange<LkDropView, DropViewChange>(
    LkDropView::class,
    ::DropViewChange
) {
    open val catalogName by nullableWS(String::class, DropViewChange::setCatalogName)
    open val schemaName by nullableWS(String::class, DropViewChange::setSchemaName)
    open val viewName by nullableWS(String::class, DropViewChange::setViewName)
}
