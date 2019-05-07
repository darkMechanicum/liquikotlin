package com.tsarev.liquikotlin.noattributes

import liquibase.change.AbstractSQLChange
import liquibase.change.ColumnConfig
import liquibase.change.core.AbstractModifyDataChange

val nullModifyFields = arrayOf(
    AbstractModifyDataChange::getCatalogName,
    AbstractModifyDataChange::getSchemaName,
    AbstractModifyDataChange::getTableName,
    AbstractModifyDataChange::getWhereParams to emptyList<ColumnConfig>(),
    AbstractModifyDataChange::getWhere
)

val nullSql = arrayOf(
    AbstractSQLChange::isStripComments to false,
    AbstractSQLChange::isSplitStatements to true,
    AbstractSQLChange::getEndDelimiter,
    AbstractSQLChange::getDbms
)

val nullColumnConfigFields = arrayOf(
    ColumnConfig::getName,
    ColumnConfig::getComputed,
    ColumnConfig::getType,
    ColumnConfig::getValue,
    ColumnConfig::getValueNumeric,
    ColumnConfig::getValueDate,
    ColumnConfig::getValueBoolean,
    ColumnConfig::getValueBlobFile,
    ColumnConfig::getValueClobFile,
    ColumnConfig::getEncoding,
    ColumnConfig::getValueComputed,
    ColumnConfig::getValueSequenceNext,
    ColumnConfig::getValueSequenceCurrent,
    ColumnConfig::getDefaultValue,
    ColumnConfig::getDefaultValueNumeric,
    ColumnConfig::getDefaultValueDate,
    ColumnConfig::getDefaultValueBoolean,
    ColumnConfig::getDefaultValueComputed,
    ColumnConfig::getDefaultValueSequenceNext,
    ColumnConfig::getConstraints,
    ColumnConfig::isAutoIncrement,
    ColumnConfig::getStartWith,
    ColumnConfig::getIncrementBy,
    ColumnConfig::getRemarks,
    ColumnConfig::getDescending
)