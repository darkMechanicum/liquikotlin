package com.tsarev.liquikotlin.complex

import com.tsarev.liquikotlin.bundled.LkChangeLog
import com.tsarev.liquikotlin.integration.LiquibaseIntegrationFactory
import com.tsarev.liquikotlin.util.*
import liquibase.change.Change
import liquibase.change.ChangeWithColumns
import liquibase.change.CheckSum
import liquibase.change.core.*
import liquibase.changelog.DatabaseChangeLog
import liquibase.precondition.CustomPreconditionWrapper
import liquibase.precondition.PreconditionLogic
import liquibase.precondition.core.*
import org.junit.Assert
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * Comprehensive test that checks all node types.
 */
class AllHierarchyTest : RuleChainAwareTest() {

    /**
     * Test integration factory.
     */
    private val liquibaseIntegration = LiquibaseIntegrationFactory()

    /**
     * Temp filder to use within include all.
     */
    val tempDir by rule { TemporaryFolder() }

    @Test
    fun testAllNodes() {
        // Prepare test data.
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

            changeset.id(1) - {
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
                    empty
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
                empty
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
            includeAll.path(tempDir.root.absolutePath).relativeToChangelogFile(false).errorIfMissingOrEmpty(false)
        })


        val result: DatabaseChangeLog = changelog.evalSafe(liquibaseIntegration, testPath to DummyAccessor.instance)

        result.run {
            Assert.assertEquals(1, changeSets.size)
            Assert.assertEquals(16, preconditions.nestedPreconditions.size) // One more from include logic.
            Assert.assertEquals(testValue, changeLogParameters.getValue(testProperty, this))
            preconditions.assertPreconditionClasses()
            preconditions.assertNestedPreconditions()

            changeSets.first().run {
                Assert.assertEquals(42, rollback.changes.size)
                Assert.assertEquals(42, changes.size)
                Assert.assertEquals(testComment, comments)
                Assert.assertEquals(1, validCheckSums.size)
                Assert.assertEquals(CheckSum.parse(testCheckSum), validCheckSums.first())
                preconditions.assertPreconditionClasses()
                preconditions.assertNestedPreconditions()
                rollback.changes.assertChangesClasses()
                changes.assertChangesClasses()
                rollback.changes.assertColumnAffectedChanges()
                changes.assertColumnAffectedChanges()
            }
        }
    }

    /**
     * Assert that preconditions have specified classes honoring order.
     */
    private fun PreconditionLogic.assertPreconditionClasses() = this.nestedPreconditions.assertClasses(
        AndPrecondition::class,
        OrPrecondition::class,
        DBMSPrecondition::class,
        RunningAsPrecondition::class,
        ChangeSetExecutedPrecondition::class,
        ColumnExistsPrecondition::class,
        TableExistsPrecondition::class,
        ViewExistsPrecondition::class,
        ForeignKeyExistsPrecondition::class,
        IndexExistsPrecondition::class,
        SequenceExistsPrecondition::class,
        PrimaryKeyExistsPrecondition::class,
        SqlPrecondition::class,
        ChangeLogPropertyDefinedPrecondition::class,
        CustomPreconditionWrapper::class
    )

    /**
     * Assert that passed precondition container has AndPrecondition
     * and OrPrecondition as its first two elements. This AndPrecondition
     * and OrPrecondition should also have nested DBMSPrecondition.
     */
    private fun PreconditionLogic.assertNestedPreconditions() {
        operator fun PreconditionLogic.get(index: Int) = this.nestedPreconditions[index]
        assertType(DBMSPrecondition::class, this[0].assertedCast<AndPrecondition>()[0])
        assertType(DBMSPrecondition::class, this[1].assertedCast<OrPrecondition>()[0])
    }

    /**
     * Assert that changes have specified classes honoring order.
     */
    private fun List<Change>.assertChangesClasses() = this.assertClasses(
        AddAutoIncrementChange::class,
        AddColumnChange::class,
        AddDefaultValueChange::class,
        AddForeignKeyConstraintChange::class,
        AddLookupTableChange::class,
        AddNotNullConstraintChange::class,
        AddPrimaryKeyChange::class,
        AddUniqueConstraintChange::class,
        CreateIndexChange::class,
        CreateProcedureChange::class,
        CreateSequenceChange::class,
        CreateTableChange::class,
        CreateViewChange::class,
        DeleteDataChange::class,
        DropAllForeignKeyConstraintsChange::class,
        DropColumnChange::class,
        DropDefaultValueChange::class,
        DropForeignKeyConstraintChange::class,
        DropIndexChange::class,
        DropNotNullConstraintChange::class,
        DropPrimaryKeyChange::class,
        DropProcedureChange::class,
        DropSequenceChange::class,
        DropTableChange::class,
        DropUniqueConstraintChange::class,
        DropViewChange::class,
        AlterSequenceChange::class,
        EmptyChange::class,
        ExecuteShellCommandChange::class,
        InsertDataChange::class,
        LoadDataChange::class,
        LoadUpdateDataChange::class,
        MergeColumnChange::class,
        ModifyDataTypeChange::class,
        RenameColumnChange::class,
        RenameTableChange::class,
        RenameViewChange::class,
        RawSQLChange::class,
        SQLFileChange::class,
        StopChange::class,
        TagDatabaseChange::class,
        UpdateDataChange::class
    )

    /**
     * Assert that some of changes has specific column config.
     */
    private fun List<Change>.assertColumnAffectedChanges() {
        (get(1) as AddColumnChange).assertColumns()
        (get(8) as CreateIndexChange).assertColumns()
        (get(11) as CreateTableChange).assertColumns()
        (get(29) as InsertDataChange).assertColumns()
        (get(30) as LoadDataChange).assertColumns()
        (get(31) as LoadUpdateDataChange).assertColumns()
        (get(41) as UpdateDataChange).assertColumns()
    }

    /**
     * Assert that column aware element has exactly one column and
     * this column has nullable constraint.
     */
    private fun ChangeWithColumns<*>.assertColumns() {
        Assert.assertEquals(1, columns.size)
        Assert.assertEquals(testNullable, columns.first().constraints.isNullable)
    }

}
