package com.tsarev.liquikotlin.util

import liquibase.precondition.core.PreconditionContainer
import java.math.BigInteger
import java.util.*

const val afterColumn = "AfterColumn"
const val baseColumnNames = "baseColumnNames"
const val baseTableCatalogName = "baseTableCatalogName"
const val baseTableName = "baseTableName"
const val baseTableSchemaName = "baseTableSchemaName"
const val beforeColumn = "BeforeColumn"
const val catalogName = "catalogName"
const val checkConstraint = "select true from dual"
const val columnAutoIncrement = true
const val columnComputed = true
const val columnDefaultValue = "default"
const val columnDefaultValueBoolean = true
const val columnDefaultValueComputed = "default computed"
const val columnDefaultValueNumeric = 5L
const val columnEncoding = "UTF-8"
const val columnName = "myColumn"
const val columnNames = "columnNames"
const val columnRemarks = "My remarks"
const val columnType = "number"
const val columnValue = "value"
const val columnValueBlobFile = "blob file"
const val columnValueBoolean = true
const val columnValueClobFile = "clob file"
const val columnValueComputed = "some"
const val columnValueNumeric = 54L
const val comments = "My Comments"
const val constraintName = "constraintName"
const val cycle = true
const val dbms = "Oracle"
const val defaultNullValue = "defaultNullValue"
const val defaultValue = "default"
const val defaultValueBoolean = true
const val defaultValueComputed = "default computed"
const val defaultValueNumeric = "365"
const val deferrable = true
const val deleteCascade = true
const val disabled = true
const val encoding = "UTF-8"
const val existingColumnName = "existingColumnName"
const val existingTableCatalogName = "existingTableCatalogName"
const val existingTableName = "existingTableName"
const val existingTableSchemaName = "existingTableSchemaName"
const val foreignKeyName = "foreignKey"
const val fullDefinition = "some definition"
const val header = "myHeader"
const val index = 42
const val indexName = "myIndex"
const val initiallyDeferred = true
const val newColumnDataType = "newColumnDataType"
const val newColumnName = "newColumnName"
const val newTableCatalogName = "newTableCatalogName"
const val newTableName = "newTableName"
const val newTableSchemaName = "newTableSchemaName"
const val nullable = true
const val onDelete = "onDelete"
const val onUpdate = "onUpdate"
const val ordered = true
const val path = "/myFile.txt"
const val position = 5
const val primaryKey = true
const val primaryKeyName = "primaryKeyName"
const val primaryKeyTablespace = "primaryKeyTablespace"
const val procedureName = "My Procedure Name"
const val procedureText = "My Procedure Text"
const val referencedColumnNames = "referenceColumn1,referenceColumn2"
const val referencedTableCatalogName = "referencedTableCatalogName"
const val referencedTableName = "referencedTable"
const val referencedTableSchemaName = "referencedTableSchemaName"
const val references = "reference1,reference2"
const val relativeToChangelogFile = true
const val remarks = "MyRemarks"
const val replaceIfExists = true
const val schemaName = "schemaName"
const val selectQuery = "select 1 from dual"
const val sequenceName = "MySequence"
const val sql = "select 1 from dual"
const val tableName = "tableName"
const val tablespace = "tablespace"
const val unique = true
const val uniqueConstraintName = "uniqueName"
const val viewName = "MyView"
val columnDefaultValueDate = Date()
val columnValueDate = Date()
val defaultValueDate = "${Date()}"
val incrementBy = BigInteger.valueOf(406L)
val maxValue = BigInteger.valueOf(64634L)
val minValue = BigInteger.valueOf(1L)
val startValue = BigInteger.valueOf(34L)
val startWith = BigInteger.valueOf(507L)

const val where = "1 = 1"
const val cascadeConstraints = false
const val stripComments = false
const val splitStatements = true
const val endDelimiter = "#"
const val cacheSize = 50
const val executable = "bash"
const val os = "ColibriOS"
const val file = "MyFile.txt"
const val column1Name = "firstColumn"
const val joinString = "left join secondColumn on firstColumn.id = secondColumn.id"
const val column2Name = "secondColumn"
const val finalColumnName = "resultColumn"
const val finalColumnType = "finalColumn"
const val newDataType = "varchar(256)"
const val oldColumnName = "oldColumn"
const val oldTableName = "oldTable"
const val oldViewName = "oldView"
const val newViewName = "newView"
const val comment = "My Comment"
const val message = "Stop message"
const val tag = "NewTag"
val onFail = PreconditionContainer.FailOption.MARK_RAN
val onError = PreconditionContainer.ErrorOption.MARK_RAN
val onSqlOutput = PreconditionContainer.OnSqlOutputOption.FAIL
const val onFailMessage = "Execution failed!"
const val onErrorMessage = "Error occured!"
const val username = "root"
const val foreignKeyTableName = "ForeignTable"
const val expectedResult = "42"
const val property = "answer"
const val value = "42"
const val className = "com.tsarev.test.MyTestClass"