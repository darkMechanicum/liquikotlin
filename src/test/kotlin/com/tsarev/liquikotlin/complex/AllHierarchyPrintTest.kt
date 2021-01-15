package com.tsarev.liquikotlin.complex

import com.tsarev.liquikotlin.bundled.LkChangeLog
import com.tsarev.liquikotlin.printing.PrinterMode
import com.tsarev.liquikotlin.printing.print
import com.tsarev.liquikotlin.util.*
import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 * Comprehensive test that checks all node types.
 */
class AllHierarchyPrintTest : RuleChainAwareTest() {

    private val predefinedText = """
        LkChangeLog
        ├─ LkPrecondition
        │  ├─ LkAndPrecondition
        │  │  └─ LkDbmsPrecondition[type:Oracle]
        │  ├─ LkOrPrecondition
        │  │  └─ LkDbmsPrecondition[type:Oracle]
        │  ├─ LkDbmsPrecondition[type:Oracle]
        │  ├─ LkRunningAsPrecondition[username:root]
        │  ├─ LkChangeSetExecutedPrecondition[id:simpleId1]
        │  ├─ LkColumnExistsPrecondition[columnName:myColumn]
        │  ├─ LkTableExistsPrecondition[tableName:tableName]
        │  ├─ LkViewExistsPrecondition[viewName:MyView]
        │  ├─ LkForeignKeyConstraintExistsPrecondition[foreignKeyName:foreignKey]
        │  ├─ LkIndexExistsPrecondition[indexName:myIndex]
        │  ├─ LkSequenceExistsPrecondition[sequenceName:MySequence]
        │  ├─ LkPrimaryKeyExistsPrecondition[primaryKeyName:primaryKeyName]
        │  ├─ LkSqlCheckPrecondition[sql:select 1 from dual]
        │  ├─ LkChangeLogPropertyDefinedPrecondition[property:answer]
        │  └─ LkCustomPrecondition[className:com.tsarev.test.MyTestClass]
        ├─ LkProperty[name:answer]
        ├─ LkChangeSet[id:1]
        │  ├─ LkRollback
        │  │  ├─ LkAddAutoIncrement[columnName:myColumn, tableName:tableName]
        │  │  ├─ LkAddColumn[tableName:tableName]
        │  │  │  └─ LkAddColumnConfig[name:myColumn]
        │  │  │     └─ LkConstraints[nullable:true]
        │  │  ├─ LkAddDefaultValue[columnName:myColumn]
        │  │  ├─ LkAddForeignKeyConstraint[baseColumnNames:columnNames]
        │  │  ├─ LkAddLookupTable
        │  │  ├─ LkAddNotNullConstraint
        │  │  ├─ LkAddPrimaryKey
        │  │  ├─ LkAddUniqueConstraint
        │  │  ├─ LkCreateIndex[indexName:myIndex]
        │  │  │  └─ LkAddColumnConfig[name:myColumn]
        │  │  │     └─ LkConstraints[nullable:true]
        │  │  ├─ LkCreateProcedure[procedureName:My Procedure Name]
        │  │  ├─ LkCreateSequence[sequenceName:MySequence]
        │  │  ├─ LkCreateTable[tableName:tableName]
        │  │  │  └─ LkCommonColumnConfig[name:myColumn]
        │  │  │     └─ LkConstraints[nullable:true]
        │  │  ├─ LkCreateView[viewName:MyView]
        │  │  ├─ LkDelete[tableName:tableName]
        │  │  ├─ LkDropAllForeignKeyConstraints[baseTableName:tableName]
        │  │  ├─ LkDropColumn[columnName:myColumn]
        │  │  ├─ LkDropDefaultValue[columnName:myColumn]
        │  │  ├─ LkDropForeignKeyConstraint[constraintName:constraintName]
        │  │  ├─ LkDropIndex[indexName:myIndex]
        │  │  ├─ LkDropNotNullConstraint[columnName:myColumn]
        │  │  ├─ LkDropPrimaryKey[constraintName:constraintName]
        │  │  ├─ LkDropProcedure[procedureName:My Procedure Name]
        │  │  ├─ LkDropSequence[sequenceName:MySequence]
        │  │  ├─ LkDropTable[tableName:tableName]
        │  │  ├─ LkDropUniqueConstraint
        │  │  ├─ LkDropView[viewName:MyView]
        │  │  ├─ LkAlterSequence[sequenceName:MySequence]
        │  │  ├─ LkEmpty
        │  │  ├─ LkExecuteCommand[executable:bash]
        │  │  ├─ LkInsert[tableName:tableName]
        │  │  │  └─ LkAddColumnConfig[name:myColumn]
        │  │  │     └─ LkConstraints[nullable:true]
        │  │  ├─ LkLoadData[tableName:tableName]
        │  │  │  └─ LkLoadColumnConfig[name:myColumn]
        │  │  │     └─ LkConstraints[nullable:true]
        │  │  ├─ LkLoadUpdateData[tableName:tableName]
        │  │  │  └─ LkLoadColumnConfig[name:myColumn]
        │  │  │     └─ LkConstraints[nullable:true]
        │  │  ├─ LkMergeColumns[tableName:tableName]
        │  │  ├─ LkModifyDataType[tableName:tableName]
        │  │  ├─ LkRenameColumn[tableName:tableName]
        │  │  ├─ LkRenameTable[oldTableName:oldTable]
        │  │  ├─ LkRenameView[oldViewName:oldView]
        │  │  ├─ LkSql[sql:select 1 from dual]
        │  │  ├─ LkSqlFile[path:MyFile.txt]
        │  │  ├─ LkStop[message:Stop message]
        │  │  ├─ LkTagDatabase[tag:NewTag]
        │  │  └─ LkUpdate[tableName:tableName]
        │  │     └─ LkCommonColumnConfig[name:myColumn]
        │  │        └─ LkConstraints[nullable:true]
        │  ├─ LkComment[text:My Comment]
        │  ├─ LkValidCheckSum[checkSum:1:1234567890]
        │  ├─ LkPrecondition
        │  │  ├─ LkAndPrecondition
        │  │  │  └─ LkDbmsPrecondition[type:Oracle]
        │  │  ├─ LkOrPrecondition
        │  │  │  └─ LkDbmsPrecondition[type:Oracle]
        │  │  ├─ LkDbmsPrecondition[type:Oracle]
        │  │  ├─ LkRunningAsPrecondition[username:root]
        │  │  ├─ LkChangeSetExecutedPrecondition[id:simpleId1]
        │  │  ├─ LkColumnExistsPrecondition[columnName:myColumn]
        │  │  ├─ LkTableExistsPrecondition[tableName:tableName]
        │  │  ├─ LkViewExistsPrecondition[viewName:MyView]
        │  │  ├─ LkForeignKeyConstraintExistsPrecondition[foreignKeyName:foreignKey]
        │  │  ├─ LkIndexExistsPrecondition[indexName:myIndex]
        │  │  ├─ LkSequenceExistsPrecondition[sequenceName:MySequence]
        │  │  ├─ LkPrimaryKeyExistsPrecondition[primaryKeyName:primaryKeyName]
        │  │  ├─ LkSqlCheckPrecondition[sql:select 1 from dual]
        │  │  ├─ LkChangeLogPropertyDefinedPrecondition[property:answer]
        │  │  └─ LkCustomPrecondition[className:com.tsarev.test.MyTestClass]
        │  ├─ LkAddAutoIncrement[columnName:myColumn, tableName:tableName]
        │  ├─ LkAddColumn[tableName:tableName]
        │  │  └─ LkAddColumnConfig[name:myColumn]
        │  │     └─ LkConstraints[nullable:true]
        │  ├─ LkAddDefaultValue[columnName:myColumn]
        │  ├─ LkAddForeignKeyConstraint[baseColumnNames:columnNames]
        │  ├─ LkAddLookupTable
        │  ├─ LkAddNotNullConstraint
        │  ├─ LkAddPrimaryKey
        │  ├─ LkAddUniqueConstraint
        │  ├─ LkCreateIndex[indexName:myIndex]
        │  │  └─ LkAddColumnConfig[name:myColumn]
        │  │     └─ LkConstraints[nullable:true]
        │  ├─ LkCreateProcedure[procedureName:My Procedure Name]
        │  ├─ LkCreateSequence[sequenceName:MySequence]
        │  ├─ LkCreateTable[tableName:tableName]
        │  │  └─ LkCommonColumnConfig[name:myColumn]
        │  │     └─ LkConstraints[nullable:true]
        │  ├─ LkCreateView[viewName:MyView]
        │  ├─ LkDelete[tableName:tableName]
        │  ├─ LkDropAllForeignKeyConstraints[baseTableName:tableName]
        │  ├─ LkDropColumn[columnName:myColumn]
        │  ├─ LkDropDefaultValue[columnName:myColumn]
        │  ├─ LkDropForeignKeyConstraint[constraintName:constraintName]
        │  ├─ LkDropIndex[indexName:myIndex]
        │  ├─ LkDropNotNullConstraint[columnName:myColumn]
        │  ├─ LkDropPrimaryKey[constraintName:constraintName]
        │  ├─ LkDropProcedure[procedureName:My Procedure Name]
        │  ├─ LkDropSequence[sequenceName:MySequence]
        │  ├─ LkDropTable[tableName:tableName]
        │  ├─ LkDropUniqueConstraint
        │  ├─ LkDropView[viewName:MyView]
        │  ├─ LkAlterSequence[sequenceName:MySequence]
        │  ├─ LkEmpty
        │  ├─ LkExecuteCommand[executable:bash]
        │  ├─ LkInsert[tableName:tableName]
        │  │  └─ LkAddColumnConfig[name:myColumn]
        │  │     └─ LkConstraints[nullable:true]
        │  ├─ LkLoadData[tableName:tableName]
        │  │  └─ LkLoadColumnConfig[name:myColumn]
        │  │     └─ LkConstraints[nullable:true]
        │  ├─ LkLoadUpdateData[tableName:tableName]
        │  │  └─ LkLoadColumnConfig[name:myColumn]
        │  │     └─ LkConstraints[nullable:true]
        │  ├─ LkMergeColumns[tableName:tableName]
        │  ├─ LkModifyDataType[tableName:tableName]
        │  ├─ LkRenameColumn[tableName:tableName]
        │  ├─ LkRenameTable[oldTableName:oldTable]
        │  ├─ LkRenameView[oldViewName:oldView]
        │  ├─ LkSql[sql:select 1 from dual]
        │  ├─ LkSqlFile[path:MyFile.txt]
        │  ├─ LkStop[message:Stop message]
        │  ├─ LkTagDatabase[tag:NewTag]
        │  └─ LkUpdate[tableName:tableName]
        │     └─ LkCommonColumnConfig[name:myColumn]
        │        └─ LkConstraints[nullable:true]
        └─ LkInclude[path:test.dummy]
    """.trimIndent()

    private val predefinedMinimumText = """
        LkChangeLog
        ├─ LkPrecondition
        │  ├─ LkAndPrecondition
        │  │  └─ LkDbmsPrecondition
        │  ├─ LkOrPrecondition
        │  │  └─ LkDbmsPrecondition
        │  ├─ LkDbmsPrecondition
        │  ├─ LkRunningAsPrecondition
        │  ├─ LkChangeSetExecutedPrecondition
        │  ├─ LkColumnExistsPrecondition
        │  ├─ LkTableExistsPrecondition
        │  ├─ LkViewExistsPrecondition
        │  ├─ LkForeignKeyConstraintExistsPrecondition
        │  ├─ LkIndexExistsPrecondition
        │  ├─ LkSequenceExistsPrecondition
        │  ├─ LkPrimaryKeyExistsPrecondition
        │  ├─ LkSqlCheckPrecondition
        │  ├─ LkChangeLogPropertyDefinedPrecondition
        │  └─ LkCustomPrecondition
        ├─ LkProperty
        ├─ LkChangeSet
        │  ├─ LkRollback
        │  │  ├─ LkAddAutoIncrement
        │  │  ├─ LkAddColumn
        │  │  │  └─ LkAddColumnConfig
        │  │  │     └─ LkConstraints
        │  │  ├─ LkAddDefaultValue
        │  │  ├─ LkAddForeignKeyConstraint
        │  │  ├─ LkAddLookupTable
        │  │  ├─ LkAddNotNullConstraint
        │  │  ├─ LkAddPrimaryKey
        │  │  ├─ LkAddUniqueConstraint
        │  │  ├─ LkCreateIndex
        │  │  │  └─ LkAddColumnConfig
        │  │  │     └─ LkConstraints
        │  │  ├─ LkCreateProcedure
        │  │  ├─ LkCreateSequence
        │  │  ├─ LkCreateTable
        │  │  │  └─ LkCommonColumnConfig
        │  │  │     └─ LkConstraints
        │  │  ├─ LkCreateView
        │  │  ├─ LkDelete
        │  │  ├─ LkDropAllForeignKeyConstraints
        │  │  ├─ LkDropColumn
        │  │  ├─ LkDropDefaultValue
        │  │  ├─ LkDropForeignKeyConstraint
        │  │  ├─ LkDropIndex
        │  │  ├─ LkDropNotNullConstraint
        │  │  ├─ LkDropPrimaryKey
        │  │  ├─ LkDropProcedure
        │  │  ├─ LkDropSequence
        │  │  ├─ LkDropTable
        │  │  ├─ LkDropUniqueConstraint
        │  │  ├─ LkDropView
        │  │  ├─ LkAlterSequence
        │  │  ├─ LkEmpty
        │  │  ├─ LkExecuteCommand
        │  │  ├─ LkInsert
        │  │  │  └─ LkAddColumnConfig
        │  │  │     └─ LkConstraints
        │  │  ├─ LkLoadData
        │  │  │  └─ LkLoadColumnConfig
        │  │  │     └─ LkConstraints
        │  │  ├─ LkLoadUpdateData
        │  │  │  └─ LkLoadColumnConfig
        │  │  │     └─ LkConstraints
        │  │  ├─ LkMergeColumns
        │  │  ├─ LkModifyDataType
        │  │  ├─ LkRenameColumn
        │  │  ├─ LkRenameTable
        │  │  ├─ LkRenameView
        │  │  ├─ LkSql
        │  │  ├─ LkSqlFile
        │  │  ├─ LkStop
        │  │  ├─ LkTagDatabase
        │  │  └─ LkUpdate
        │  │     └─ LkCommonColumnConfig
        │  │        └─ LkConstraints
        │  ├─ LkComment
        │  ├─ LkValidCheckSum
        │  ├─ LkPrecondition
        │  │  ├─ LkAndPrecondition
        │  │  │  └─ LkDbmsPrecondition
        │  │  ├─ LkOrPrecondition
        │  │  │  └─ LkDbmsPrecondition
        │  │  ├─ LkDbmsPrecondition
        │  │  ├─ LkRunningAsPrecondition
        │  │  ├─ LkChangeSetExecutedPrecondition
        │  │  ├─ LkColumnExistsPrecondition
        │  │  ├─ LkTableExistsPrecondition
        │  │  ├─ LkViewExistsPrecondition
        │  │  ├─ LkForeignKeyConstraintExistsPrecondition
        │  │  ├─ LkIndexExistsPrecondition
        │  │  ├─ LkSequenceExistsPrecondition
        │  │  ├─ LkPrimaryKeyExistsPrecondition
        │  │  ├─ LkSqlCheckPrecondition
        │  │  ├─ LkChangeLogPropertyDefinedPrecondition
        │  │  └─ LkCustomPrecondition
        │  ├─ LkAddAutoIncrement
        │  ├─ LkAddColumn
        │  │  └─ LkAddColumnConfig
        │  │     └─ LkConstraints
        │  ├─ LkAddDefaultValue
        │  ├─ LkAddForeignKeyConstraint
        │  ├─ LkAddLookupTable
        │  ├─ LkAddNotNullConstraint
        │  ├─ LkAddPrimaryKey
        │  ├─ LkAddUniqueConstraint
        │  ├─ LkCreateIndex
        │  │  └─ LkAddColumnConfig
        │  │     └─ LkConstraints
        │  ├─ LkCreateProcedure
        │  ├─ LkCreateSequence
        │  ├─ LkCreateTable
        │  │  └─ LkCommonColumnConfig
        │  │     └─ LkConstraints
        │  ├─ LkCreateView
        │  ├─ LkDelete
        │  ├─ LkDropAllForeignKeyConstraints
        │  ├─ LkDropColumn
        │  ├─ LkDropDefaultValue
        │  ├─ LkDropForeignKeyConstraint
        │  ├─ LkDropIndex
        │  ├─ LkDropNotNullConstraint
        │  ├─ LkDropPrimaryKey
        │  ├─ LkDropProcedure
        │  ├─ LkDropSequence
        │  ├─ LkDropTable
        │  ├─ LkDropUniqueConstraint
        │  ├─ LkDropView
        │  ├─ LkAlterSequence
        │  ├─ LkEmpty
        │  ├─ LkExecuteCommand
        │  ├─ LkInsert
        │  │  └─ LkAddColumnConfig
        │  │     └─ LkConstraints
        │  ├─ LkLoadData
        │  │  └─ LkLoadColumnConfig
        │  │     └─ LkConstraints
        │  ├─ LkLoadUpdateData
        │  │  └─ LkLoadColumnConfig
        │  │     └─ LkConstraints
        │  ├─ LkMergeColumns
        │  ├─ LkModifyDataType
        │  ├─ LkRenameColumn
        │  ├─ LkRenameTable
        │  ├─ LkRenameView
        │  ├─ LkSql
        │  ├─ LkSqlFile
        │  ├─ LkStop
        │  ├─ LkTagDatabase
        │  └─ LkUpdate
        │     └─ LkCommonColumnConfig
        │        └─ LkConstraints
        └─ LkInclude
    """.trimIndent()

    /**
     * Test change log.
     */
    private val testChangeLog get() = LkChangeLog() - {
        precondition - {
            and.dbms.type(testDbms)
            or.dbms.type(testDbms)
            dbms.type(testDbms)
            runningAs.username(testUsername)
            changeSetExecuted.id(testId)
            columnExists.columnName(testColumnName)
            tableExists.tableName(testTableName)
            viewExists.viewName(testViewName)
            foreignKeyConstraintExists.foreignKeyName(testForeignKeyName)
            indexExists.indexName(testIndexName)
            sequenceExists.sequenceName(testSequenceName)
            primaryKeyExists.primaryKeyName(testPrimaryKeyName)
            sqlCheck.sql(testSql)
            changeLogPropertyDefined.property(testProperty)
            customPrecondition.className(testClassName)
        }

        property.name(testProperty).value(testValue)

        changeSet.id(1) - {
            rollback - {
                // Create changes.
                addAutoIncrement.tableName(testTableName).columnName(testColumnName)
                addColumn.tableName(testTableName) - {
                    column.name(testColumnName)
                        .constraints.nullable(testNullable)
                }
                addDefaultValue.columnName(testColumnName)
                addForeignKeyConstraint.baseColumnNames(testColumnNames)
                addLookupTable.existingTableName(testTableName)
                addNotNullConstraint.constraintName(testConstraintName)
                addPrimaryKey.constraintName(testConstraintName)
                addUniqueConstraint.constraintName(testConstraintName)
                createIndex.indexName(testIndexName) - {
                    column.name(testColumnName)
                        .constraints.nullable(testNullable)
                }
                createProcedure.procedureName(testProcedureName)
                createSequence.sequenceName(testSequenceName)
                createTable.tableName(testTableName) - {
                    column.name(testColumnName)
                        .constraints.nullable(testNullable)
                }
                createView.viewName(testViewName)

                // Drop changes.
                delete.tableName(testTableName)
                dropAllForeignKeyConstraints.baseTableName(testTableName)
                dropColumn.columnName(testColumnName)
                dropDefaultValue.columnName(testColumnName)
                dropForeignKeyConstraint.constraintName(testConstraintName)
                dropIndex.indexName(testIndexName)
                dropNotNullConstraint.columnName(testColumnName)
                dropPrimaryKey.constraintName(testConstraintName)
                dropProcedure.procedureName(testProcedureName)
                dropSequence.sequenceName(testSequenceName)
                dropTable.tableName(testTableName)
                dropUniqueConstraint.tableName(testTableName)
                dropView.viewName(testViewName)

                // Other changes.
                alterSequence.sequenceName(testSequenceName)
                empty()
                executeCommand.executable(testExecutable)
                insert.tableName(testTableName) - {
                    column.name(testColumnName)
                        .constraints.nullable(testNullable)
                }
                loadData.tableName(testTableName) - {
                    column.name(testColumnName)
                        .constraints.nullable(testNullable)
                }
                loadUpdateData.tableName(testTableName) - {
                    column.name(testColumnName)
                        .constraints.nullable(testNullable)
                }
                mergeColumns.tableName(testTableName)
                modifyDataType.tableName(testTableName)
                renameColumn.tableName(testTableName)
                renameTable.oldTableName(testOldTableName)
                renameView.oldViewName(testOldViewName)
                sql.sql(testSql)
                sqlFile.path(testFile)
                stop.message(testMessage)
                tagDatabase.tag(testTag)
                update.tableName(testTableName) - {
                    column.name(testColumnName)
                        .constraints.nullable(testNullable)
                }
            }

            comment.text(testComment)
            validCheckSum.checkSum(testCheckSum)
            precondition - {
                and.dbms.type(testDbms)
                or.dbms.type(testDbms)

                dbms.type(testDbms)
                runningAs.username(testUsername)
                changeSetExecuted.id(testId)
                columnExists.columnName(testColumnName)
                tableExists.tableName(testTableName)
                viewExists.viewName(testViewName)
                foreignKeyConstraintExists.foreignKeyName(testForeignKeyName)
                indexExists.indexName(testIndexName)
                sequenceExists.sequenceName(testSequenceName)
                primaryKeyExists.primaryKeyName(testPrimaryKeyName)
                sqlCheck.sql(testSql)
                changeLogPropertyDefined.property(testProperty)
                customPrecondition.className(testClassName)
            }

            // Create changes.
            addAutoIncrement.tableName(testTableName).columnName(testColumnName)
            addColumn.tableName(testTableName) - {
                column.name(testColumnName)
                    .constraints.nullable(testNullable)
            }
            addDefaultValue.columnName(testColumnName)
            addForeignKeyConstraint.baseColumnNames(testColumnNames)
            addLookupTable.existingTableName(testTableName)
            addNotNullConstraint.constraintName(testConstraintName)
            addPrimaryKey.constraintName(testConstraintName)
            addUniqueConstraint.constraintName(testConstraintName)
            createIndex.indexName(testIndexName) - {
                column.name(testColumnName)
                    .constraints.nullable(testNullable)
            }
            createProcedure.procedureName(testProcedureName)
            createSequence.sequenceName(testSequenceName)
            createTable.tableName(testTableName) - {
                column.name(testColumnName)
                    .constraints.nullable(testNullable)
            }
            createView.viewName(testViewName)

            // Drop changes.
            delete.tableName(testTableName)
            dropAllForeignKeyConstraints.baseTableName(testTableName)
            dropColumn.columnName(testColumnName)
            dropDefaultValue.columnName(testColumnName)
            dropForeignKeyConstraint.constraintName(testConstraintName)
            dropIndex.indexName(testIndexName)
            dropNotNullConstraint.columnName(testColumnName)
            dropPrimaryKey.constraintName(testConstraintName)
            dropProcedure.procedureName(testProcedureName)
            dropSequence.sequenceName(testSequenceName)
            dropTable.tableName(testTableName)
            dropUniqueConstraint.tableName(testTableName)
            dropView.viewName(testViewName)

            // Other changes.
            alterSequence.sequenceName(testSequenceName)
            empty()
            executeCommand.executable(testExecutable)
            insert.tableName(testTableName) - {
                column.name(testColumnName)
                    .constraints.nullable(testNullable)
            }
            loadData.tableName(testTableName) - {
                column.name(testColumnName)
                    .constraints.nullable(testNullable)
            }
            loadUpdateData.tableName(testTableName) - {
                column.name(testColumnName)
                    .constraints.nullable(testNullable)
            }
            mergeColumns.tableName(testTableName)
            modifyDataType.tableName(testTableName)
            renameColumn.tableName(testTableName)
            renameTable.oldTableName(testOldTableName)
            renameView.oldViewName(testOldViewName)
            sql.sql(testSql)
            sqlFile.path(testFile)
            stop.message(testMessage)
            tagDatabase.tag(testTag)
            update.tableName(testTableName) - {
                column.name(testColumnName)
                    .constraints.nullable(testNullable)
            }
        }

        include.path(testIncludePath).relativeToChangelogFile(testRelativeToChangelogFile)
    }

    @Test
    fun testPrettyPrint() {
        val stream = ByteArrayOutputStream()
        testChangeLog.print(stream = PrintStream(stream))
        val streamBytes = stream.toByteArray()
        val streamText = String(streamBytes).trimIndent()
        Assert.assertEquals(predefinedText, streamText)
    }

    @Test
    fun testMinimumPrint() {
        val stream = ByteArrayOutputStream()
        testChangeLog.print(mode = PrinterMode.MINIMUM, stream = PrintStream(stream))
        val streamBytes = stream.toByteArray()
        val streamText = String(streamBytes).trimIndent()
        Assert.assertEquals(predefinedMinimumText, streamText)
    }
}
