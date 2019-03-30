package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.bundled.*
import liquibase.change.core.*

open class DeleteIntegration :
    ChangeIntegration<LkDelete, DeleteDataChange>(::DeleteDataChange) { init {
    LkDelete::catalogName - DeleteDataChange::setCatalogName
    LkDelete::schemaName - DeleteDataChange::setSchemaName
    LkDelete::tableName - DeleteDataChange::setTableName
    LkDelete::where - DeleteDataChange::setWhere
}
}

open class DropAllForeignKeyConstraintsIntegration :
    ChangeIntegration<LkDropAllForeignKeyConstraints, DropAllForeignKeyConstraintsChange>(::DropAllForeignKeyConstraintsChange) { init {
    LkDropAllForeignKeyConstraints::baseTableCatalogName - DropAllForeignKeyConstraintsChange::setBaseTableCatalogName
    LkDropAllForeignKeyConstraints::baseTableName - DropAllForeignKeyConstraintsChange::setBaseTableName
    LkDropAllForeignKeyConstraints::baseTableSchemaName - DropAllForeignKeyConstraintsChange::setBaseTableSchemaName
}
}

open class DropColumnIntegration : ChangeIntegration<LkDropColumn, DropColumnChange>(::DropColumnChange) { init {
    LkDropColumn::catalogName - DropColumnChange::setCatalogName
    LkDropColumn::columnName - DropColumnChange::setColumnName
    LkDropColumn::schemaName - DropColumnChange::setSchemaName
    LkDropColumn::tableName - DropColumnChange::setTableName
}
}

open class DropDefaultValueIntegration :
    ChangeIntegration<LkDropDefaultValue, DropDefaultValueChange>(::DropDefaultValueChange) { init {
    LkDropDefaultValue::catalogName - DropDefaultValueChange::setCatalogName
    LkDropDefaultValue::columnDataType - DropDefaultValueChange::setColumnDataType
    LkDropDefaultValue::columnName - DropDefaultValueChange::setColumnName
    LkDropDefaultValue::schemaName - DropDefaultValueChange::setSchemaName
    LkDropDefaultValue::tableName - DropDefaultValueChange::setTableName
}
}

open class DropForeignKeyConstraintIntegration :
    ChangeIntegration<LkDropForeignKeyConstraint, DropForeignKeyConstraintChange>(::DropForeignKeyConstraintChange) { init {
    LkDropForeignKeyConstraint::baseTableCatalogName - DropForeignKeyConstraintChange::setBaseTableCatalogName
    LkDropForeignKeyConstraint::baseTableName - DropForeignKeyConstraintChange::setBaseTableName
    LkDropForeignKeyConstraint::baseTableSchemaName - DropForeignKeyConstraintChange::setBaseTableSchemaName
    LkDropForeignKeyConstraint::constraintName - DropForeignKeyConstraintChange::setConstraintName
}
}

open class DropIndexIntegration : ChangeIntegration<LkDropIndex, DropIndexChange>(::DropIndexChange) { init {
    LkDropIndex::catalogName - DropIndexChange::setCatalogName
    LkDropIndex::indexName - DropIndexChange::setIndexName
    LkDropIndex::schemaName - DropIndexChange::setSchemaName
    LkDropIndex::tableName - DropIndexChange::setTableName
}
}

open class DropNotNullConstraintIntegration :
    ChangeIntegration<LkDropNotNullConstraint, DropNotNullConstraintChange>(::DropNotNullConstraintChange) { init {
    LkDropNotNullConstraint::catalogName - DropNotNullConstraintChange::setCatalogName
    LkDropNotNullConstraint::columnDataType - DropNotNullConstraintChange::setColumnDataType
    LkDropNotNullConstraint::columnName - DropNotNullConstraintChange::setColumnName
    LkDropNotNullConstraint::schemaName - DropNotNullConstraintChange::setSchemaName
    LkDropNotNullConstraint::tableName - DropNotNullConstraintChange::setTableName
}
}

open class DropPrimaryKeyIntegration :
    ChangeIntegration<LkDropPrimaryKey, DropPrimaryKeyChange>(::DropPrimaryKeyChange) { init {
    LkDropPrimaryKey::catalogName - DropPrimaryKeyChange::setCatalogName
    LkDropPrimaryKey::constraintName - DropPrimaryKeyChange::setConstraintName
    LkDropPrimaryKey::schemaName - DropPrimaryKeyChange::setSchemaName
    LkDropPrimaryKey::tableName - DropPrimaryKeyChange::setTableName
}
}

open class DropProcedureIntegration :
    ChangeIntegration<LkDropProcedure, DropProcedureChange>(::DropProcedureChange) { init {
    LkDropProcedure::catalogName - DropProcedureChange::setCatalogName
    LkDropProcedure::procedureName - DropProcedureChange::setProcedureName
    LkDropProcedure::schemaName - DropProcedureChange::setSchemaName
}
}

open class DropSequenceIntegration :
    ChangeIntegration<LkDropSequence, DropSequenceChange>(::DropSequenceChange) { init {
    LkDropSequence::catalogName - DropSequenceChange::setCatalogName
    LkDropSequence::schemaName - DropSequenceChange::setSchemaName
    LkDropSequence::sequenceName - DropSequenceChange::setSequenceName
}
}

open class DropTableIntegration : ChangeIntegration<LkDropTable, DropTableChange>(::DropTableChange) { init {
    LkDropTable::cascadeConstraints - DropTableChange::setCascadeConstraints
    LkDropTable::catalogName - DropTableChange::setCatalogName
    LkDropTable::schemaName - DropTableChange::setSchemaName
    LkDropTable::tableName - DropTableChange::setTableName
}
}

open class DropUniqueConstraintIntegration :
    ChangeIntegration<LkDropUniqueConstraint, DropUniqueConstraintChange>(::DropUniqueConstraintChange) { init {
    LkDropUniqueConstraint::catalogName - DropUniqueConstraintChange::setCatalogName
    LkDropUniqueConstraint::constraintName - DropUniqueConstraintChange::setConstraintName
    LkDropUniqueConstraint::schemaName - DropUniqueConstraintChange::setSchemaName
    LkDropUniqueConstraint::tableName - DropUniqueConstraintChange::setTableName
    LkDropUniqueConstraint::uniqueColumns - DropUniqueConstraintChange::setUniqueColumns
}
}

open class DropViewIntegration : ChangeIntegration<LkDropView, DropViewChange>(::DropViewChange) { init {
    LkDropView::catalogName - DropViewChange::setCatalogName
    LkDropView::schemaName - DropViewChange::setSchemaName
    LkDropView::viewName - DropViewChange::setViewName
}
}
