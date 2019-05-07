package com.tsarev.liquikotlin.allattributes

import com.tsarev.liquikotlin.util.*
import liquibase.change.AbstractSQLChange
import liquibase.change.ColumnConfig
import liquibase.change.core.AbstractModifyDataChange
import liquibase.statement.DatabaseFunction
import liquibase.statement.SequenceCurrentValueFunction
import liquibase.statement.SequenceNextValueFunction

val notNullModifyFields = arrayOf(
    AbstractModifyDataChange::getCatalogName to testCatalogName,
    AbstractModifyDataChange::getSchemaName to testSchemaName,
    AbstractModifyDataChange::getTableName to testTableName,
    AbstractModifyDataChange::getWhereParams to emptyList<ColumnConfig>(),
    AbstractModifyDataChange::getWhere to testWhere
)

val notNullSql = arrayOf(
    AbstractSQLChange::isStripComments to testStripComments,
    AbstractSQLChange::isSplitStatements to testSplitStatements,
    AbstractSQLChange::getEndDelimiter to testEndDelimiter,
    AbstractSQLChange::getDbms to testDbms
)

val notNullColumnConfigFields = arrayOf(
    ColumnConfig::getName to testColumnName,
    ColumnConfig::getComputed to testComputed,
    ColumnConfig::getType to testColumnType,
    ColumnConfig::getValue to testValue,
    ColumnConfig::getValueNumeric to testValueNumeric,
    ColumnConfig::getValueDate to testValueDate,
    ColumnConfig::getValueBoolean to testValueBoolean,
    ColumnConfig::getValueBlobFile to testValueBlobFile,
    ColumnConfig::getValueClobFile to testValueClobFile,
    ColumnConfig::getEncoding to testEncoding,
    ColumnConfig::getValueComputed to DatabaseFunction(testValueComputed),
    ColumnConfig::getValueSequenceNext to SequenceNextValueFunction(testValueSequenceNext),
    ColumnConfig::getValueSequenceCurrent to SequenceCurrentValueFunction(testValueSequenceCurrent),
    ColumnConfig::getDefaultValue to testDefaultValue,
    ColumnConfig::getDefaultValueNumeric to testDefaultValueNumeric,
    ColumnConfig::getDefaultValueDate to testDefaultValueDate,
    ColumnConfig::getDefaultValueBoolean to testDefaultValueBoolean,
    ColumnConfig::getDefaultValueComputed to DatabaseFunction(testDefaultValueComputed),
    ColumnConfig::getDefaultValueSequenceNext to SequenceNextValueFunction(testDefaultValueSequenceNext),
    ColumnConfig::isAutoIncrement to testAutoIncrement,
    ColumnConfig::getStartWith to testStartWith,
    ColumnConfig::getIncrementBy to testIncrementBy,
    ColumnConfig::getRemarks to testRemarks,
    ColumnConfig::getDescending to testDescending
)