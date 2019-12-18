package com.tsarev.liquikotlin.bundled

import com.tsarev.liquikotlin.infrastructure.LbDslNode
import com.tsarev.liquikotlin.infrastructure.default.nullable

open class LkDelete : LbDslNode<LkDelete> (LkDelete::class) {
    open val catalogName by nullable(String::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val tableName by nullable(String::class)
    open val where by nullable(String::class)
}

open class LkDropAllForeignKeyConstraints :
    LbDslNode<LkDropAllForeignKeyConstraints> (LkDropAllForeignKeyConstraints::class) {
    open val baseTableCatalogName by nullable(String::class)
    @Primary
    open val baseTableName by nullable(String::class)
    open val baseTableSchemaName by nullable(String::class)
}

open class LkDropColumn : LbDslNode<LkDropColumn> (LkDropColumn::class) {
    open val catalogName by nullable(String::class)
    @Primary
    open val columnName by nullable(String::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val tableName by nullable(String::class)
}

open class LkDropDefaultValue : LbDslNode<LkDropDefaultValue> (LkDropDefaultValue::class) {
    open val catalogName by nullable(String::class)
    open val columnDataType by nullable(String::class)
    @Primary
    open val columnName by nullable(String::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val tableName by nullable(String::class)
}

open class LkDropForeignKeyConstraint : LbDslNode<LkDropForeignKeyConstraint> (LkDropForeignKeyConstraint::class) {
    open val baseTableCatalogName by nullable(String::class)
    open val baseTableName by nullable(String::class)
    open val baseTableSchemaName by nullable(String::class)
    @Primary
    open val constraintName by nullable(String::class)
}

open class LkDropIndex : LbDslNode<LkDropIndex> (LkDropIndex::class) {
    open val catalogName by nullable(String::class)
    @Primary
    open val indexName by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)
    open val associatedWith by nullable(String::class)
}

open class LkDropNotNullConstraint : LbDslNode<LkDropNotNullConstraint> (LkDropNotNullConstraint::class) {
    open val catalogName by nullable(String::class)
    open val columnDataType by nullable(String::class)
    @Primary
    open val columnName by nullable(String::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val tableName by nullable(String::class)
}

open class LkDropPrimaryKey : LbDslNode<LkDropPrimaryKey> (LkDropPrimaryKey::class) {
    open val catalogName by nullable(String::class)
    @Primary
    open val constraintName by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)
}

open class LkDropProcedure : LbDslNode<LkDropProcedure> (LkDropProcedure::class) {
    open val catalogName by nullable(String::class)
    @Primary
    open val procedureName by nullable(String::class)
    open val schemaName by nullable(String::class)
}

open class LkDropSequence : LbDslNode<LkDropSequence> (LkDropSequence::class) {
    open val catalogName by nullable(String::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val sequenceName by nullable(String::class)
}

open class LkDropTable : LbDslNode<LkDropTable> (LkDropTable::class) {
    open val cascadeConstraints by nullable(Boolean::class)
    open val catalogName by nullable(String::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val tableName by nullable(String::class)
}

open class LkDropUniqueConstraint : LbDslNode<LkDropUniqueConstraint> (LkDropUniqueConstraint::class) {
    open val catalogName by nullable(String::class)
    @Primary
    open val constraintName by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)
    open val uniqueColumns by nullable(String::class)
}

open class LkDropView : LbDslNode<LkDropView>(LkDropView::class) {
    open val catalogName by nullable(String::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val viewName by nullable(String::class)
}
