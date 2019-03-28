package com.tsarev.liquikotlin.bundled

import liquibase.change.core.*

open class LkDelete : LkChange<LkDelete, DeleteDataChange>(
    LkDelete::class,
    ::DeleteDataChange
) {
    val catalogName by nullableWS(String::class, DeleteDataChange::setCatalogName)
    val schemaName by nullableWS(String::class, DeleteDataChange::setSchemaName)
    val tableName by nullableWS(String::class, DeleteDataChange::setTableName)
    val where by nullableWS(String::class, DeleteDataChange::setWhere)

    operator fun invoke(tableName: String) = tableName(tableName)
    operator fun minus(where: String): Any = where(where)
}

open class LkDropAllForeignKeyConstraints :
    LkChange<LkDropAllForeignKeyConstraints, DropAllForeignKeyConstraintsChange>(
        LkDropAllForeignKeyConstraints::class,
        ::DropAllForeignKeyConstraintsChange
    ) {
    val baseTableCatalogName by nullableWS(String::class, DropAllForeignKeyConstraintsChange::setBaseTableCatalogName)
    val baseTableName by nullableWS(String::class, DropAllForeignKeyConstraintsChange::setBaseTableName)
    val baseTableSchemaName by nullableWS(String::class, DropAllForeignKeyConstraintsChange::setBaseTableSchemaName)

    operator fun invoke(baseTableName: String) = baseTableName(baseTableName)
}

open class LkDropColumn : LkChange<LkDropColumn, DropColumnChange>(
    LkDropColumn::class,
    ::DropColumnChange
) {
    val catalogName by nullableWS(String::class, DropColumnChange::setCatalogName)
    val columnName by nullableWS(String::class, DropColumnChange::setColumnName)
    val schemaName by nullableWS(String::class, DropColumnChange::setSchemaName)
    val tableName by nullableWS(String::class, DropColumnChange::setTableName)

    operator fun invoke(tableName: String, columnName: String) = tableName(tableName).columnName(columnName)
}

open class LkDropDefaultValue : LkChange<LkDropDefaultValue, DropDefaultValueChange>(
    LkDropDefaultValue::class,
    ::DropDefaultValueChange
) {
    val catalogName by nullableWS(String::class, DropDefaultValueChange::setCatalogName)
    val columnDataType by nullableWS(String::class, DropDefaultValueChange::setColumnDataType)
    val columnName by nullableWS(String::class, DropDefaultValueChange::setColumnName)
    val schemaName by nullableWS(String::class, DropDefaultValueChange::setSchemaName)
    val tableName by nullableWS(String::class, DropDefaultValueChange::setTableName)

    operator fun invoke(tableName: String, columnName: String) = tableName(tableName).columnName(columnName)
}

open class LkDropForeignKeyConstraint : LkChange<LkDropForeignKeyConstraint, DropForeignKeyConstraintChange>(
    LkDropForeignKeyConstraint::class,
    ::DropForeignKeyConstraintChange
) {
    val baseTableCatalogName by nullableWS(String::class, DropForeignKeyConstraintChange::setBaseTableCatalogName)
    val baseTableName by nullableWS(String::class, DropForeignKeyConstraintChange::setBaseTableName)
    val baseTableSchemaName by nullableWS(String::class, DropForeignKeyConstraintChange::setBaseTableSchemaName)
    val constraintName by nullableWS(String::class, DropForeignKeyConstraintChange::setConstraintName)

    operator fun invoke(baseTableName: String, constraintName: String) =
        baseTableName(baseTableName).constraintName(constraintName)
}

open class LkDropIndex : LkChange<LkDropIndex, DropIndexChange>(
    LkDropIndex::class,
    ::DropIndexChange
) {
    val catalogName by nullableWS(String::class, DropIndexChange::setCatalogName)
    val indexName by nullableWS(String::class, DropIndexChange::setIndexName)
    val schemaName by nullableWS(String::class, DropIndexChange::setSchemaName)
    val tableName by nullableWS(String::class, DropIndexChange::setTableName)

    operator fun invoke(tableName: String, indexName: String) = tableName(tableName).indexName(indexName)
}

open class LkDropNotNullConstraint : LkChange<LkDropNotNullConstraint, DropNotNullConstraintChange>(
    LkDropNotNullConstraint::class,
    ::DropNotNullConstraintChange
) {
    val catalogName by nullableWS(String::class, DropNotNullConstraintChange::setCatalogName)
    val columnDataType by nullableWS(String::class, DropNotNullConstraintChange::setColumnDataType)
    val columnName by nullableWS(String::class, DropNotNullConstraintChange::setColumnName)
    val schemaName by nullableWS(String::class, DropNotNullConstraintChange::setSchemaName)
    val tableName by nullableWS(String::class, DropNotNullConstraintChange::setTableName)

    operator fun invoke(tableName: String, columnName: String) = tableName(tableName).columnName(columnName)
}

open class LkDropPrimaryKey : LkChange<LkDropPrimaryKey, DropPrimaryKeyChange>(
    LkDropPrimaryKey::class,
    ::DropPrimaryKeyChange
) {
    val catalogName by nullableWS(String::class, DropPrimaryKeyChange::setCatalogName)
    val constraintName by nullableWS(String::class, DropPrimaryKeyChange::setConstraintName)
    val schemaName by nullableWS(String::class, DropPrimaryKeyChange::setSchemaName)
    val tableName by nullableWS(String::class, DropPrimaryKeyChange::setTableName)

    operator fun invoke(tableName: String, constraintName: String) =
        tableName(tableName).constraintName(constraintName)
}

open class LkDropProcedure : LkChange<LkDropProcedure, DropProcedureChange>(
    LkDropProcedure::class,
    ::DropProcedureChange
) {
    val catalogName by nullableWS(String::class, DropProcedureChange::setCatalogName)
    val procedureName by nullableWS(String::class, DropProcedureChange::setProcedureName)
    val schemaName by nullableWS(String::class, DropProcedureChange::setSchemaName)

    operator fun invoke(procedureName: String) = procedureName(procedureName)
}

open class LkDropSequence : LkChange<LkDropSequence, DropSequenceChange>(
    LkDropSequence::class,
    ::DropSequenceChange
) {
    val catalogName by nullableWS(String::class, DropSequenceChange::setCatalogName)
    val schemaName by nullableWS(String::class, DropSequenceChange::setSchemaName)
    val sequenceName by nullableWS(String::class, DropSequenceChange::setSequenceName)

    operator fun invoke(sequenceName: String) = sequenceName(sequenceName)
}

open class LkDropTable : LkChange<LkDropTable, DropTableChange>(
    LkDropTable::class,
    ::DropTableChange
) {
    val cascadeConstraints by nullableWS(Boolean::class, DropTableChange::setCascadeConstraints)
    val catalogName by nullableWS(String::class, DropTableChange::setCatalogName)
    val schemaName by nullableWS(String::class, DropTableChange::setSchemaName)
    val tableName by nullableWS(String::class, DropTableChange::setTableName)

    operator fun invoke(tableName: String) = tableName(tableName)
}

open class LkDropUniqueConstraint : LkChange<LkDropUniqueConstraint, DropUniqueConstraintChange>(
    LkDropUniqueConstraint::class,
    ::DropUniqueConstraintChange
) {
    val catalogName by nullableWS(String::class, DropUniqueConstraintChange::setCatalogName)
    val constraintName by nullableWS(String::class, DropUniqueConstraintChange::setConstraintName)
    val schemaName by nullableWS(String::class, DropUniqueConstraintChange::setSchemaName)
    val tableName by nullableWS(String::class, DropUniqueConstraintChange::setTableName)
    val uniqueColumns by nullableWS(String::class, DropUniqueConstraintChange::setUniqueColumns)

    operator fun invoke(tableName: String, constraintName: String) =
        tableName(tableName).constraintName(constraintName)
}

open class LkDropView : LkChange<LkDropView, DropViewChange>(
    LkDropView::class,
    ::DropViewChange
) {
    val catalogName by nullableWS(String::class, DropViewChange::setCatalogName)
    val schemaName by nullableWS(String::class, DropViewChange::setSchemaName)
    val viewName by nullableWS(String::class, DropViewChange::setViewName)

    operator fun invoke(viewName: String) = viewName(viewName)
}
