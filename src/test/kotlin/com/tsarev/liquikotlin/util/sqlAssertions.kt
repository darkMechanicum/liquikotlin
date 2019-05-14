package com.tsarev.liquikotlin.util

import org.junit.Assert
import java.sql.Connection

// Various simple sql tweaks.
private val Boolean.toH2 get() = if (this) "YES" else "NO"
private val String.toH2 get() = this.toUpperCase()
private val Any.toH2 get() = "$this".toUpperCase()
val infoSchema = { it: String -> "select 1 from information_schema.$it where" }

enum class ConstraintTypes { UNIQUE }

private fun tac(tableName: String, columnName: String) =
    "table_name = '${tableName.toH2}' and column_name = '${columnName.toH2}'"

/**
 * Assert that specified sql will execute without errors and return exactly one row.
 */
fun Connection.assertSql(sql: String, message: String = "Specified sql executed with errors: $sql") {
    var execResult = false
    ignore {
        this.createStatement().use {
            it.execute(sql)
            val resultSet = it.resultSet
            execResult = resultSet.next() && !resultSet.next()
        }
    }
    Assert.assertTrue(message, execResult)
}

fun Connection.assertTableOrViewExist(name: String) = assertSql(
    "${infoSchema("tables")} table_name = '${name.toH2}'",
    "Specified table or view does not exist: $name."
)

fun Connection.assertColumnExist(tableName: String, columnName: String) = assertSql(
    "${infoSchema("columns")} table_name = '${tableName.toH2}' and column_name = '${columnName.toH2}'",
    "Specified column does not exist: $tableName.$columnName."
)

fun Connection.assertSequenceExist(sequenceName: String) = assertSql(
    "${infoSchema("sequences")} sequence_name = '${sequenceName.toH2}'",
    "Specified sequence does not exist: $sequenceName."
)

// This is a small hack in H2 since there is no 'auto_increment' flag for column. Instead, system sequence existence is checked.
fun Connection.assertHasAutoIncrement(tableName: String, columnName: String) = assertSql(
    "${infoSchema("columns")} ${tac(tableName, columnName)} and sequence_name like '%SYS%'",
    "Specified column does not have auto increment: $tableName.$columnName."
)

fun Connection.assertHasConstraint(tableName: String, columnName: String, constraintType: ConstraintTypes) =
    assertSql(
        "${infoSchema("constraints")} table_name = '${tableName.toH2}' and column_list = '${columnName.toH2}' and constraint_type = '$constraintType'",
        "Specified column does not have constraint: $tableName.$columnName - $constraintType."
    )

fun Connection.assertIsPrimary(tableName: String, columnName: String) = assertSql(
    "${infoSchema("indexes")} ${tac(tableName, columnName)} and primary_key = 'YES'",
    "Specified column is not primary: $tableName.$columnName."
)

fun Connection.assertHasIndex(tableName: String, columnName: String) = assertSql(
    "${infoSchema("indexes")} ${tac(tableName, columnName)}",
    "Specified column does not have index: $tableName.$columnName."
)

fun Connection.assertHasDefault(tableName: String, columnName: String, defaultValue: Any) = assertSql(
    "${infoSchema("columns")} ${tac(tableName, columnName)} and column_default = '${defaultValue.toH2}'",
    "Specified column does not have default value: $tableName.$columnName."
)

fun Connection.assertIsNullable(tableName: String, columnName: String, isNullable: Boolean = true) = assertSql(
    "${infoSchema("columns")} ${tac(tableName, columnName)} and is_nullable = '${isNullable.toH2}'",
    "Specified column is ${if (isNullable) "not" else ""} nullable: $tableName.$columnName."
)