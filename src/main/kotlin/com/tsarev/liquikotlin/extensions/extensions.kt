package com.tsarev.liquikotlin.extensions

import com.tsarev.liquikotlin.bundled.*
import liquibase.change.ColumnConfig
import java.lang.IllegalArgumentException

// --- Bundled create changes extension methods ---

operator fun <SelfT : LkBaseColumnConfig<SelfT>> LkBaseColumnConfig<SelfT>.invoke(name: String) = name(name)

operator fun LkAddAutoIncrement.invoke(tableName: String, columnName: String) = columnName(columnName).tableName(tableName)

operator fun LkAddColumn.invoke(tableName: String) = tableName(tableName)

operator fun LkAddColumn.invoke(tableName: String, vararg columns: String) =
    tableName(tableName).apply {
        columns.forEach { column -> this.column(column) }
    }

fun LkAddColumn.columns(vararg columns: Pair<String, String>) =
    this.apply {
        columns.forEach { (name, type) -> this.column(name).type(type) }
    }

fun LkAddDefaultValue.at(tableName: String, columnName: String) = tableName(tableName).columnName(columnName)

operator fun LkAddForeignKeyConstraint.invoke(constraintName: String) = constraintName(constraintName)

operator fun LkAddForeignKeyConstraint.invoke(baseTableName: String, vararg baseColumnNames: String) =
    if (baseColumnNames.isNotEmpty())
        baseTableName(baseTableName).baseColumnNames(baseColumnNames.joinToString())
    else
        throw IllegalArgumentException("Referenced columns cannot be empty.")

operator fun LkAddNotNullConstraint.invoke(tableName: String, columnName: String) = tableName(tableName).columnName(columnName)

operator fun LkAddPrimaryKey.invoke(constraintName: String) = constraintName(constraintName)

operator fun LkAddUniqueConstraint.invoke(constraintName: String) = constraintName(constraintName)

fun LkAddUniqueConstraint.at(tableName: String, vararg columnNames: String) =
    if (columnNames.isNotEmpty())
        tableName(tableName).columnNames(columnNames.joinToString())
    else
        throw IllegalArgumentException("Referenced columns cannot be empty.")

operator fun LkCreateIndex.invoke(indexName: String) = indexName(indexName)

operator fun LkCreateIndex.invoke(tableName: String, vararg columns: String) =
    tableName(tableName).apply {
        columns.forEach { column -> this.column(column) }
    }

operator fun LkCreateProcedure.invoke(procedureName: String) = procedureName(procedureName)

operator fun LkCreateProcedure.minus(procedureText: String): Any = procedureText(procedureText)

operator fun LkCreateSequence.invoke(sequenceName: String) = sequenceName(sequenceName)

operator fun LkCreateTable.invoke(tableName: String) = tableName(tableName)

operator fun LkCreateTable.invoke(tableName: String, vararg columns: Pair<String, String>) =
    tableName(tableName).apply {
        columns.forEach { (name, type) -> this.column(name).type(type) }
    }

fun LkCreateTable.columns(vararg columns: Pair<String, String>) =
    this.apply {
        columns.forEach { (name, type) -> this.column(name).type(type) }
    }

operator fun LkCreateView.invoke(viewName: String) = viewName(viewName)

operator fun LkCreateView.minus(selectQuery: String): Any = selectQuery(selectQuery)

// --- Bundled drop changes extension methods ---

operator fun LkDelete.invoke(tableName: String) = tableName(tableName)

operator fun LkDelete.minus(where: String): Any = where(where)

operator fun LkDropAllForeignKeyConstraints.invoke(baseTableName: String) = baseTableName(baseTableName)

operator fun LkDropColumn.invoke(tableName: String, columnName: String) = tableName(tableName).columnName(columnName)

operator fun LkDropDefaultValue.invoke(tableName: String, columnName: String) =
    tableName(tableName).columnName(columnName)

operator fun LkDropForeignKeyConstraint.invoke(baseTableName: String, constraintName: String) =
    baseTableName(baseTableName).constraintName(constraintName)

operator fun LkDropIndex.invoke(tableName: String, indexName: String) = tableName(tableName).indexName(indexName)

operator fun LkDropNotNullConstraint.invoke(tableName: String, columnName: String) =
    tableName(tableName).columnName(columnName)

operator fun LkDropPrimaryKey.invoke(tableName: String, constraintName: String) =
    tableName(tableName).constraintName(constraintName)

operator fun LkDropProcedure.invoke(procedureName: String) = procedureName(procedureName)

operator fun LkDropSequence.invoke(sequenceName: String) = sequenceName(sequenceName)

operator fun LkDropTable.invoke(tableName: String) = tableName(tableName)

operator fun LkDropUniqueConstraint.invoke(tableName: String, constraintName: String) =
    tableName(tableName).constraintName(constraintName)

operator fun LkDropView.invoke(viewName: String) = viewName(viewName)

// --- Other bundled changes extension methods ---

operator fun LkAlterSequence.invoke(sequenceName: String) = sequenceName(sequenceName)

operator fun LkExecuteCommand.invoke(executable: String) = executable(executable)

operator fun LkInsert.invoke(tableName: String) = tableName(tableName)

operator fun LkLoadData.invoke(tableName: String) = tableName(tableName)

operator fun LkLoadUpdateData.invoke(tableName: String) = tableName(tableName)

operator fun LkMergeColumns.invoke(tableName: String) = tableName(tableName)

operator fun LkModifyDataType.invoke(tableName: String, columnName: String, newDataType: String) =
    tableName(tableName).columnName(columnName).newDataType(newDataType)

operator fun LkRenameColumn.invoke(tableName: String, oldColumnName: String, newColumnName: String) =
    tableName(tableName).oldColumnName(oldColumnName).newColumnName(newColumnName)

operator fun LkRenameTable.invoke(oldTableName: String, newTableName: String) =
    oldTableName(oldTableName).newTableName(newTableName)

operator fun LkRenameView.invoke(oldViewName: String, newViewName: String) =
    oldViewName(oldViewName).newViewName(newViewName)

operator fun LkSql.minus(sql: String) = sql(sql)

operator fun LkSqlFile.invoke(path: String) = path(path)

operator fun LkStop.invoke(message: String) = message(message)

operator fun LkTagDatabase.invoke(tag: String) = tag(tag)

operator fun LkUpdate.invoke(tableName: String) = tableName(tableName)

// --- Core definitions extension methods ---

operator fun LkInclude.invoke(path: String) = path(path)

operator fun LkIncludeAll.invoke(path: String) = path(path)

operator fun LkProperty.invoke(name: String, value: String) = name(name).value(value)

operator fun LkChangeSet.invoke(id: Any) = id(id)

operator fun LkChangeSet.invoke(id: Any, author: String) = id(id).author(author)

operator fun LkValidCheckSum.invoke(checkSum: String) = checkSum(checkSum)

operator fun LkComment.invoke(text: String) = text(text)

operator fun LkComment.minus(text: String) = text(text)

// --- Preconditions extension methods ---

operator fun LkDbmsPrecondition.invoke(type: String) = type(type)

operator fun LkRunningAsPrecondition.invoke(username: String) = username(username)

operator fun LkChangeSetExecutedPrecondition.invoke(id: String) = id(id)

operator fun LkColumnExistsPrecondition.invoke(tableName: String, columnName: String) =
    tableName(tableName).columnName(columnName)

operator fun LkTableExistsPrecondition.invoke(tableName: String) = tableName(tableName)

operator fun LkViewExistsPrecondition.invoke(viewName: String) = viewName(viewName)

operator fun LkForeignKeyConstraintExistsPrecondition.invoke(foreignKeyTableName: String, foreignKeyName: String) =
    foreignKeyTableName(foreignKeyTableName).foreignKeyName(foreignKeyName)

operator fun LkIndexExistsPrecondition.invoke(tableName: String, indexName: String) =
    tableName(tableName).indexName(indexName)

operator fun LkSequenceExistsPrecondition.invoke(sequenceName: String) = sequenceName(sequenceName)

operator fun LkPrimaryKeyExistsPrecondition.invoke(tableName: String, primaryKeyName: String) =
    tableName(tableName).primaryKeyName(primaryKeyName)

operator fun LkSqlCheckPrecondition.invoke(expectedResult: String) = expectedResult(expectedResult)

operator fun LkSqlCheckPrecondition.minus(sql: String) = sql(sql)

operator fun LkChangeLogPropertyDefinedPrecondition.invoke(property: String) = property(property)

operator fun LkChangeLogPropertyDefinedPrecondition.invoke(property: String, value: String) =
    property(property).value(value)

operator fun LkCustomPrecondition.invoke(className: String, vararg params: Pair<String, String>) =
    className(className).apply {
        params.forEach { (name, value) -> this.param.name(name).value(value) }
    }