package com.tsarev.liquikotlin.util

import liquibase.precondition.core.PreconditionContainer
import java.math.BigInteger
import java.util.*

// A
const val testAfterColumn = "AfterColumn"
const val testAutoIncrement = true
const val testAssociatedWith = "MyAssociation"

// B
const val testBaseColumnNames = "baseColumnNames"
const val testBaseTableCatalogName = "baseTableCatalogName"
const val testBaseTableName = "baseTableName"
const val testBaseTableSchemaName = "baseTableSchemaName"
const val testBeforeColumn = "BeforeColumn"

// C
const val testCascadeConstraints = false
const val testCatalogName = "catalogName"
const val testCheckConstraint = "select true from dual"
const val testClassName = "com.tsarev.test.MyTestClass"
const val testClustered = true
const val testColumn1Name = "firstColumn"
const val testColumn2Name = "secondColumn"
const val testColumnName = "myColumn"
const val testColumnNames = "columnNames"
const val testColumnType = "number"
const val testComment = "My Comment"
const val testComments = "My Comments"
const val testComputed = true
const val testConstraintName = "constraintName"
const val testCycle = true
val testCacheSize = BigInteger.valueOf(345L)!!

// D
const val testDbms = "Oracle"
const val testDefaultNullValue = "defaultNullValue"
const val testDefaultValue = "default"
const val testDefaultValueBoolean = true
const val testDefaultValueComputed = "default computed"
const val testDefaultValueNumeric = 365L
const val testDefaultValueSequenceNext = "mySequence"
const val testDeferrable = true
const val testDeleteCascade = true
const val testDescending = false
const val testDisabled = true
val testDefaultValueDate = Date()

// E
const val testEncoding = "UTF-8"
const val testEndDelimiter = "#"
const val testExecutable = "bash"
const val testExistingColumnName = "existingColumnName"
const val testExistingTableCatalogName = "existingTableCatalogName"
const val testExistingTableName = "existingTableName"
const val testExistingTableSchemaName = "existingTableSchemaName"
const val testExpectedResult = "42"

// F
const val testFile = "MyFile.txt"
const val testFinalColumnName = "resultColumn"
const val testFinalColumnType = "finalColumn"
const val testForeignKeyName = "foreignKey"
const val testForeignKeyTableName = "ForeignTable"
const val testFullDefinition = true

// H
const val testHeader = "myHeader"

// I
const val testIndex = 42
const val testIndexName = "myIndex"
const val testInitiallyDeferred = true
val testIncrementBy = BigInteger.valueOf(406L)!!

// J
const val testJoinString = "left join secondColumn on firstColumn.id = secondColumn.id"

// M
const val testMessage = "Stop message"
val testMaxValue = BigInteger.valueOf(64634L)!!
val testMinValue = BigInteger.valueOf(1L)!!

// N
const val testNewColumnDataType = "newColumnDataType"
const val testNewColumnName = "newColumnName"
const val testNewDataType = "varchar(256)"
const val testNewTableCatalogName = "newTableCatalogName"
const val testNewTableName = "newTableName"
const val testNewTableSchemaName = "newTableSchemaName"
const val testNewViewName = "newView"
const val testNullable = true

// O
const val testOldColumnName = "oldColumn"
const val testOldTableName = "oldTable"
const val testOldViewName = "oldView"
const val testOnDelete = "onDelete"
const val testOnErrorMessage = "Error occured!"
const val testOnFailMessage = "Execution failed!"
const val testOnUpdate = "onUpdate"
const val testOrdered = true
const val testOs = "ColibriOS,Unix"
val testOnError = PreconditionContainer.ErrorOption.MARK_RAN
val testOnFail = PreconditionContainer.FailOption.MARK_RAN
val testOnSqlOutput = PreconditionContainer.OnSqlOutputOption.FAIL

// P
const val testPath = "/myFile.txt"
const val testPosition = 5
const val testPrimaryKey = true
const val testPrimaryKeyName = "primaryKeyName"
const val testPrimaryKeyTablespace = "primaryKeyTablespace"
const val testProcedureName = "My Procedure Name"
const val testProcedureText = "My Procedure Text"
const val testProperty = "answer"

// R
const val testReferencedColumnNames = "referenceColumn1,referenceColumn2"
const val testReferencedTableCatalogName = "referencedTableCatalogName"
const val testReferencedTableName = "referencedTable"
const val testReferencedTableSchemaName = "referencedTableSchemaName"
const val testReferences = "reference1,reference2"
const val testRelativeToChangelogFile = true
const val testRemarks = "MyRemarks"
const val testReplaceIfExists = true

// S
const val testSchemaName = "schemaName"
const val testSelectQuery = "select 1 from dual"
const val testSequenceName = "MySequence"
const val testSplitStatements = true
const val testSql = "select 1 from dual"
const val testStripComments = false
val testStartValue = BigInteger.valueOf(34L)!!
val testStartWith = BigInteger.valueOf(507L)!!

// T
const val testTableName = "tableName"
const val testTablespace = "tablespace"
const val testTag = "NewTag"

// U
const val testUnique = true
const val testUniqueConstraintName = "uniqueName"
const val testUsername = "root"

// V
const val testValue = "42"
const val testValueBlobFile = "blob file"
const val testValueBoolean = true
const val testValueClobFile = "clob file"
const val testValueComputed = "some"
const val testValueNumeric = 54L
const val testValueSequenceCurrent = "mySequence"
const val testValueSequenceNext = "mySequence"
const val testViewName = "MyView"
val testValueDate = Date()

// W
const val testWhere = "1 = 1"
const val testWillCycle = true