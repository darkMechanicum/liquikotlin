package com.tsarev.liquikotlin.bundled

import com.tsarev.liquikotlin.infrastructure.LbDslNode
import java.math.BigInteger

open class LkAlterSequence : LbDslNode<LkAlterSequence>(LkAlterSequence::class) {
    open val catalogName by nullable(String::class)
    open val incrementBy by nullable(BigInteger::class)
    open val maxValue by nullable(BigInteger::class)
    open val minValue by nullable(BigInteger::class)
    open val ordered by nullable(Boolean::class)
    open val schemaName by nullable(String::class)
    open val sequenceName by nullable(String::class)
    open val cacheSize by nullable(BigInteger::class)
    open val willCycle by nullable(Boolean::class)
}

// TODO Add lazy builder callback for instant creation upon use.
open class LkEmpty : LbDslNode<LkEmpty>(LkEmpty::class)

open class LkExecuteCommand : LbDslNode<LkExecuteCommand> (LkExecuteCommand::class) {
    open val executable by nullable(String::class)
    open val os by nullable(String::class)
}

open class LkInsert : LbDslNode<LkInsert> (LkInsert::class) {
    open val catalogName by nullable(String::class)
    open val dbms by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)

    open val column by child(::LkAddColumnConfig)
}

open class LkLoadData : LbDslNode<LkLoadData> (LkLoadData::class) {
    open val catalogName by nullable(String::class)
    open val encoding by nullable(String::class)
    open val file by nullable(String::class)
    open val quotchar by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val separator by nullable(String::class)
    open val tableName by nullable(String::class)
    open val relativeToChangelogFile by nullable(Boolean::class)

    open val column by child(::LkLoadColumnConfig)
}

open class LkLoadUpdateData : LbDslNode<LkLoadUpdateData> (LkLoadUpdateData::class) {
    open val catalogName by nullable(String::class)
    open val encoding by nullable(String::class)
    open val file by nullable(String::class)
    open val primaryKey by nullable(String::class)
    open val quotchar by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val separator by nullable(String::class)
    open val tableName by nullable(String::class)
    open val relativeToChangelogFile by nullable(Boolean::class)

    open val column by child(::LkLoadColumnConfig)
}

open class LkMergeColumns : LbDslNode<LkMergeColumns> (LkMergeColumns::class) {
    open val catalogName by nullable(String::class)
    open val column1Name by nullable(String::class)
    open val column2Name by nullable(String::class)
    open val finalColumnName by nullable(String::class)
    open val finalColumnType by nullable(String::class)
    open val joinString by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)
}

open class LkModifyDataType : LbDslNode<LkModifyDataType> (LkModifyDataType::class) {
    open val catalogName by nullable(String::class)
    open val columnName by nullable(String::class)
    open val newDataType by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)
}

open class LkRenameColumn : LbDslNode<LkRenameColumn> (LkRenameColumn::class) {
    open val catalogName by nullable(String::class)
    open val columnDataType by nullable(String::class)
    open val newColumnName by nullable(String::class)
    open val oldColumnName by nullable(String::class)
    open val remarks by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)
}

open class LkRenameTable : LbDslNode<LkRenameTable> (LkRenameTable::class) {
    open val catalogName by nullable(String::class)
    open val newTableName by nullable(String::class)
    open val oldTableName by nullable(String::class)
    open val schemaName by nullable(String::class)
}

open class LkRenameView : LbDslNode<LkRenameView> (LkRenameView::class) {
    open val catalogName by nullable(String::class)
    open val newViewName by nullable(String::class)
    open val oldViewName by nullable(String::class)
    open val schemaName by nullable(String::class)
}

open class LkSql : LbDslNode<LkSql> (LkSql::class) {
    open val comment by nullable(String::class)
    open val dbms by nullable(String::class)
    open val endDelimiter by nullable(String::class)
    open val splitStatements by nullable(Boolean::class)
    open val sql by nonNullable(String::class)
    open val stripComments by nullable(Boolean::class)
}

open class LkSqlFile : LbDslNode<LkSqlFile> (LkSqlFile::class) {
    open val dbms by nullable(String::class)
    open val encoding by nullable(String::class)
    open val endDelimiter by nullable(String::class)
    open val path by nullable(String::class)
    open val relativeToChangelogFile by nullable(Boolean::class)
    open val splitStatements by nullable(Boolean::class)
    open val stripComments by nullable(Boolean::class)
}

open class LkStop : LbDslNode<LkStop>(LkStop::class) {
    open val message by nullable(String::class)
}

open class LkTagDatabase : LbDslNode<LkTagDatabase>(LkTagDatabase::class) {
    open val tag by nullable(String::class)
}

open class LkUpdate : LbDslNode<LkUpdate>(LkUpdate::class) {
    open val catalogName by nullable(String::class)
    open val schemaName by nullable(String::class)
    open val tableName by nullable(String::class)
    open val where by nullable(String::class)

    open val column by child(::LkCommonColumnConfig)
}