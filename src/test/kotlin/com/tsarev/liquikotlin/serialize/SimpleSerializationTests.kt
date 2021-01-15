package com.tsarev.liquikotlin.serialize

import com.tsarev.liquikotlin.bundled.LkChangeLog
import com.tsarev.liquikotlin.bundled.invoke
import com.tsarev.liquikotlin.bundled.minus
import com.tsarev.liquikotlin.integration.LiquibaseIntegrationFactory
import com.tsarev.liquikotlin.util.*
import liquibase.changelog.DatabaseChangeLog
import liquibase.serializer.ext.KotlinLiquibaseChangeLogSerializer
import org.junit.Test

/**
 * Testing script execution on in memory H2 database.
 */
class SimpleSerializationTests : RuleChainAwareTest() {

    /**
     * Test integration factory.
     */
    private val liquibaseIntegration = LiquibaseIntegrationFactory()

    @Test
    fun simpleSerializeTest() {

        val root = LkChangeLog()
        val changelog = (root - {
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
        })

        val result: DatabaseChangeLog = changelog.node.evalSafe(liquibaseIntegration, testPath to DummyAccessor.instance)

        val serializer = KotlinLiquibaseChangeLogSerializer()

        println(serializer.serialize(result.changeSets.first(), true))
    }

}