package com.tsarev.liquikotlin.bundled

import com.tsarev.liquikotlin.infrastructure.LbDslNode
import com.tsarev.liquikotlin.infrastructure.default.child
import com.tsarev.liquikotlin.infrastructure.default.nullable
import com.tsarev.liquikotlin.infrastructure.default.prop
import java.math.BigInteger
import java.util.*
import kotlin.reflect.KClass

// --- Generalized classes ---

abstract class LkBaseColumnConfig<SelfT : LkBaseColumnConfig<SelfT>>(thisClass: KClass<SelfT>) :
    LbDslNode<SelfT>(thisClass) {
    @Primary
    open val name by nullable(String::class)
    @Primary
    open val type by nullable(String::class)
    open val value by nullable(String::class)
    open val computed by nullable(Boolean::class)
    open val valueNumeric by nullable(Number::class)
    open val valueBoolean by nullable(Boolean::class)
    open val valueDate by nullable(Date::class)
    open val valueComputed by nullable(String::class)
    open val valueBlobFile by nullable(String::class)
    open val valueClobFile by nullable(String::class)
    open val encoding by nullable(String::class)
    open val defaultValue by nullable(String::class)
    open val defaultValueNumeric by nullable(Number::class)
    open val defaultValueBoolean by nullable(Boolean::class)
    open val defaultValueDate by nullable(Date::class)
    open val defaultValueComputed by nullable(String::class)
    open val autoIncrement by nullable(Boolean::class)
    open val remarks by nullable(String::class)
    open val valueSequenceNext by nullable(String::class)
    open val valueSequenceCurrent by nullable(String::class)
    open val defaultValueSequenceNext by nullable(String::class)
    open val startWith by nullable(BigInteger::class)
    open val incrementBy by nullable(BigInteger::class)
    open val descending by nullable(Boolean::class)

    open val constraints by child(::LkConstraints)
}

@LKDsl
open class LkAddColumnConfig : LkBaseColumnConfig<LkAddColumnConfig>(LkAddColumnConfig::class) {
    open val beforeColumn by nullable(String::class)
    open val afterColumn by nullable(String::class)
    open val position by nullable(Int::class)
}

@LKDsl
open class LkLoadColumnConfig : LkBaseColumnConfig<LkLoadColumnConfig>(LkLoadColumnConfig::class) {
    open val index by nullable(Int::class)
    open val header by nullable(String::class)
}

@LKDsl
open class LkCommonColumnConfig : LkBaseColumnConfig<LkCommonColumnConfig>(LkCommonColumnConfig::class)

// --- Definitions ---

@LKDsl
open class LkAddAutoIncrement : LbDslNode<LkAddAutoIncrement>(LkAddAutoIncrement::class) {
    open val catalogName by nullable(String::class)
    open val columnDataType by nullable(String::class)
    @Primary
    open val columnName by prop(String::class)
    open val incrementBy by nullable(BigInteger::class)
    open val schemaName by nullable(String::class)
    open val startWith by nullable(BigInteger::class)
    @Primary
    open val tableName by prop(String::class)
}

@LKDsl
open class LkAddColumn : LbDslNode<LkAddColumn>(LkAddColumn::class) {
    open val catalogName by nullable(String::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val tableName by prop(String::class)

    open val column by child(::LkAddColumnConfig)
}

@LKDsl
open class LkConstraints : LbDslNode<LkConstraints>(LkConstraints::class) {
    @Primary
    open val nullable by nullable(Boolean::class)
    @Primary
    open val primaryKey by nullable(Boolean::class)
    open val primaryKeyName by nullable(String::class)
    @Primary
    open val unique by nullable(Boolean::class)
    open val uniqueConstraintName by nullable(String::class)
    open val references by nullable(String::class)
    @Primary
    open val foreignKeyName by nullable(String::class)
    @Primary
    open val deleteCascade by nullable(Boolean::class)
    @Primary
    open val deferrable by nullable(Boolean::class)
    @Primary
    open val initiallyDeferred by nullable(Boolean::class)
    open val primaryKeyTablespace by nullable(String::class)
    open val referencedTableName by nullable(String::class)
    open val referencedColumnNames by nullable(String::class)
    open val checkConstraint by nullable(String::class)
}

@LKDsl
open class LkAddDefaultValue : LbDslNode<LkAddDefaultValue>(LkAddDefaultValue::class) {
    open val catalogName by nullable(String::class)
    open val columnDataType by nullable(String::class)
    @Primary
    open val columnName by nullable(String::class)
    open val defaultValue by nullable(String::class)
    open val defaultValueBoolean by nullable(Boolean::class)
    open val defaultValueComputed by nullable(String::class)
    open val defaultValueDate by nullable(Date::class)
    open val defaultValueNumeric by nullable(Number::class)
    open val defaultValueSequenceNext by nullable(String::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val tableName by nullable(String::class)
}

@LKDsl
open class LkAddForeignKeyConstraint : LbDslNode<LkAddForeignKeyConstraint>(LkAddForeignKeyConstraint::class) {
    @Primary
    open val baseColumnNames by nullable(String::class)
    open val baseTableCatalogName by nullable(String::class)
    @Primary
    open val baseTableName by nullable(String::class)
    open val baseTableSchemaName by nullable(String::class)
    open val constraintName by nullable(String::class)
    open val deferrable by nullable(Boolean::class)
    open val initiallyDeferred by nullable(Boolean::class)
    open val onDelete by nullable(String::class)
    open val onUpdate by nullable(String::class)
    open val referencedColumnNames by nullable(String::class)
    open val referencedTableCatalogName by nullable(String::class)
    open val referencedTableName by nullable(String::class)
    open val referencedTableSchemaName by nullable(String::class)
    open val referencesUniqueColumn by nullable(Boolean::class)
}

@LKDsl
open class LkAddLookupTable : LbDslNode<LkAddLookupTable>(LkAddLookupTable::class) {
    open val constraintName by nullable(String::class)
    open val existingColumnName by nullable(String::class)
    open val existingTableCatalogName by nullable(String::class)
    open val existingTableName by nullable(String::class)
    open val existingTableSchemaName by nullable(String::class)
    open val newColumnDataType by nullable(String::class)
    @Primary
    open val newColumnName by nullable(String::class)
    open val newTableCatalogName by nullable(String::class)
    @Primary
    open val newTableName by nullable(String::class)
    open val newTableSchemaName by nullable(String::class)
}

@LKDsl
open class LkAddNotNullConstraint : LbDslNode<LkAddNotNullConstraint>(LkAddNotNullConstraint::class) {
    open val catalogName by nullable(String::class)
    open val columnDataType by nullable(String::class)
    @Primary
    open val columnName by nullable(String::class)
    open val defaultNullValue by nullable(String::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val tableName by nullable(String::class)
    open val constraintName by nullable(String::class)
}

@LKDsl
open class LkAddPrimaryKey : LbDslNode<LkAddPrimaryKey>(LkAddPrimaryKey::class) {
    open val catalogName by nullable(String::class)
    @Primary
    open val columnNames by nullable(String::class)
    open val constraintName by nullable(String::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val tableName by nullable(String::class)
    open val tablespace by nullable(String::class)
    open val clustered by nullable(Boolean::class)
    open val forIndexName by nullable(String::class)
    open val forIndexSchemaName by nullable(String::class)
    open val forIndexCatalogName by nullable(String::class)
}

@LKDsl
open class LkAddUniqueConstraint : LbDslNode<LkAddUniqueConstraint>(LkAddUniqueConstraint::class) {
    open val catalogName by nullable(String::class)
    @Primary
    open val columnNames by nullable(String::class)
    open val constraintName by nullable(String::class)
    open val deferrable by nullable(Boolean::class)
    open val disabled by nullable(Boolean::class)
    open val initiallyDeferred by nullable(Boolean::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val tableName by nullable(String::class)
    open val tablespace by nullable(String::class)
    open val forIndexName by nullable(String::class)
    open val forIndexSchemaName by nullable(String::class)
    open val forIndexCatalogName by nullable(String::class)
}

@LKDsl
open class LkCreateIndex : LbDslNode<LkCreateIndex>(LkCreateIndex::class) {
    open val catalogName by nullable(String::class)
    @Primary
    open val indexName by nullable(String::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val tableName by nullable(String::class)
    open val tablespace by nullable(String::class)
    open val unique by nullable(Boolean::class)

    open val column by child(::LkAddColumnConfig)
}

@LKDsl
open class LkCreateProcedure : LbDslNode<LkCreateProcedure>(LkCreateProcedure::class) {
    open val catalogName by nullable(String::class)
    open val comments by nullable(String::class)
    open val dbms by nullable(String::class)
    open val encoding by nullable(String::class)
    open val path by nullable(String::class)
    @Primary
    open val procedureName by nullable(String::class)
    open val procedureText by nullable(String::class)
    open val relativeToChangelogFile by nullable(Boolean::class)
    open val schemaName by nullable(String::class)
    open val replaceIfExists by nullable(Boolean::class)
}

@LKDsl
open class LkCreateSequence : LbDslNode<LkCreateSequence>(LkCreateSequence::class) {
    open val catalogName by nullable(String::class)
    open val cycle by nullable(Boolean::class)
    open val incrementBy by nullable(BigInteger::class)
    open val maxValue by nullable(BigInteger::class)
    open val minValue by nullable(BigInteger::class)
    open val ordered by nullable(Boolean::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val sequenceName by nullable(String::class)
    open val startValue by nullable(BigInteger::class)
    open val cacheSize by nullable(BigInteger::class)
}

@LKDsl
open class LkCreateTable : LbDslNode<LkCreateTable>(LkCreateTable::class) {
    open val catalogName by nullable(String::class)
    open val remarks by nullable(String::class)
    open val schemaName by nullable(String::class)
    @Primary
    open val tableName by nullable(String::class)
    open val tablespace by nullable(String::class)

    open val column by child(::LkCommonColumnConfig)
}

@LKDsl
open class LkCreateView : LbDslNode<LkCreateView>(LkCreateView::class) {
    open val catalogName by nullable(String::class)
    open val replaceIfExists by nullable(Boolean::class)
    open val schemaName by nullable(String::class)
    open val selectQuery by nullable(String::class)
    @Primary
    open val viewName by nullable(String::class)
    open val fullDefinition by nullable(Boolean::class)
}