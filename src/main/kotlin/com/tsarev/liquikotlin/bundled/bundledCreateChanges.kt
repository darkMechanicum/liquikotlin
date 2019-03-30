package com.tsarev.liquikotlin.bundled

import com.tsarev.liquikotlin.infrastructure.LbDslNode
import java.math.BigInteger
import java.util.*
import kotlin.reflect.KClass

// --- Generalized classes ---

abstract class LkBaseColumnConfig<SelfT : LkBaseColumnConfig<SelfT>>(thisClass: KClass<SelfT>) :
    LbDslNode<SelfT>(thisClass) {
    open val name by nullable(String::class)
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

    open val constraints by child(::LkConstraints)
}

open class LkAddColumnConfig : LkBaseColumnConfig<LkAddColumnConfig>(LkAddColumnConfig::class) {
    open val beforeColumn by nullable(String::class)
    open val afterColumn by nullable(String::class)
    open val position by nullable(Int::class)
}

open class LkCommonColumnConfig : LkBaseColumnConfig<LkCommonColumnConfig>(LkCommonColumnConfig::class)

// --- Definitions ---

open class LkAddAutoIncrement : LbDslNode<LkAddAutoIncrement>(LkAddAutoIncrement::class) {
    open val catalogName by nullable(String::class)
    open val columnDataType by nullable(String::class)
    open val columnName by nonNullable(String::class)
    open val incrementBy by nullable(BigInteger::class)
    open val schemaName by nullable(String::class)
    open val startWith by nullable(BigInteger::class)
    open val tableName by nonNullable(String::class)
}

open class LkAddColumn : LbDslNode<LkAddColumn>(LkAddColumn::class) {
    open val catalogName by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nonNullable(String::class)

    open val column by child(::LkAddColumnConfig)
}

open class LkConstraints : LbDslNode<LkConstraints>(LkConstraints::class) {
    open val nullable by nullable(Boolean::class)
    open val primaryKey by nullable(Boolean::class)
    open val primaryKeyName by nullable(String::class)
    open val unique by nullable(Boolean::class)
    open val uniqueConstraintName by nullable(String::class)
    open val references by nullable(String::class)
    open val foreignKeyName by nullable(String::class)
    open val deleteCascade by nullable(Boolean::class)
    open val deferrable by nullable(Boolean::class)
    open val initiallyDeferred by nullable(Boolean::class)
}

open class LkAddDefaultValue : LbDslNode<LkAddDefaultValue>(LkAddDefaultValue::class) {
    open val catalogName by nullable(String::class)
    open val columnDataType by nullable(String::class)
    open val columnName by nullable(String::class)
    open val defaultValue by nullable(String::class)
    open val defaultValueBoolean by nullable(Boolean::class)
    open val defaultValueComputed by nullable(String::class)
    open val defaultValueDate by nullable(String::class)
    open val defaultValueNumeric by nullable(String::class)
    open val defaultValueSequenceNext by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)
}

open class LkAddForeignKeyConstraint : LbDslNode<LkAddForeignKeyConstraint>(LkAddForeignKeyConstraint::class) {
    open val baseColumnNames by nullable(String::class)
    open val baseTableCatalogName by nullable(String::class)
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

open class LkAddLookupTable : LbDslNode<LkAddLookupTable>(LkAddLookupTable::class) {
    open val constraintName by nullable(String::class)
    open val existingColumnName by nullable(String::class)
    open val existingTableCatalogName by nullable(String::class)
    open val existingTableName by nullable(String::class)
    open val existingTableSchemaName by nullable(String::class)
    open val newColumnDataType by nullable(String::class)
    open val newColumnName by nullable(String::class)
    open val newTableCatalogName by nullable(String::class)
    open val newTableName by nullable(String::class)
    open val newTableSchemaName by nullable(String::class)
}

open class LkAddNotNullConstraint : LbDslNode<LkAddNotNullConstraint>(LkAddNotNullConstraint::class) {
    open val catalogName by nullable(String::class)
    open val columnDataType by nullable(String::class)
    open val columnName by nullable(String::class)
    open val defaultNullValue by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)
}

open class LkAddPrimaryKey : LbDslNode<LkAddPrimaryKey>(LkAddPrimaryKey::class) {
    open val catalogName by nullable(String::class)
    open val columnNames by nullable(String::class)
    open val constraintName by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)
    open val tablespace by nullable(String::class)
}

open class LkAddUniqueConstraint : LbDslNode<LkAddUniqueConstraint>(LkAddUniqueConstraint::class) {
    open val catalogName by nullable(String::class)
    open val columnNames by nullable(String::class)
    open val constraintName by nullable(String::class)
    open val deferrable by nullable(Boolean::class)
    open val disabled by nullable(Boolean::class)
    open val initiallyDeferred by nullable(Boolean::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)
    open val tablespace by nullable(String::class)
}

open class LkCreateIndex : LbDslNode<LkCreateIndex>(LkCreateIndex::class) {
    open val catalogName by nullable(String::class)
    open val indexName by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)
    open val tablespace by nullable(String::class)
    open val unique by nullable(Boolean::class)

    open val column by child(::LkAddColumnConfig)
}

open class LkCreateProcedure : LbDslNode<LkCreateProcedure>(LkCreateProcedure::class) {
    open val catalogName by nullable(String::class)
    open val comments by nullable(String::class)
    open val dbms by nullable(String::class)
    open val encoding by nullable(String::class)
    open val path by nullable(String::class)
    open val procedureName by nullable(String::class)
    open val procedureText by nullable(String::class)
    open val relativeToChangelogFile by nullable(Boolean::class)
    open val schemaName by nullable(String::class)
}

open class LkCreateSequence : LbDslNode<LkCreateSequence>(LkCreateSequence::class) {
    open val catalogName by nullable(String::class)
    open val cycle by nullable(Boolean::class)
    open val incrementBy by nullable(BigInteger::class)
    open val maxValue by nullable(BigInteger::class)
    open val minValue by nullable(BigInteger::class)
    open val ordered by nullable(Boolean::class)
    open val schemaName by nullable(String::class)
    open val sequenceName by nullable(String::class)
    open val startValue by nullable(BigInteger::class)
}

open class LkCreateTable : LbDslNode<LkCreateTable>(LkCreateTable::class) {
    open val catalogName by nullable(String::class)
    open val remarks by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)
    open val tablespace by nullable(String::class)

    open val column by child(::LkCommonColumnConfig)
}

open class LkCreateView : LbDslNode<LkCreateView>(LkCreateView::class) {
    open val catalogName by nullable(String::class)
    open val replaceIfExists by nullable(Boolean::class)
    open val schemaName by nullable(String::class)
    open val selectQuery by nullable(String::class)
    open val viewName by nullable(String::class)
}