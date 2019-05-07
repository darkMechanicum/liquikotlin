package com.tsarev.liquikotlin.hierarchy

import com.tsarev.liquikotlin.bundled.LkChangeLog
import com.tsarev.liquikotlin.bundled.invoke
import com.tsarev.liquikotlin.bundled.minus
import com.tsarev.liquikotlin.integration.LiquibaseIntegrationFactory
import com.tsarev.liquikotlin.util.*
import liquibase.change.core.CreateTableChange
import liquibase.change.core.CreateViewChange
import liquibase.change.core.DropTableChange
import liquibase.change.core.RawSQLChange
import liquibase.changelog.DatabaseChangeLog
import liquibase.precondition.core.AndPrecondition
import liquibase.precondition.core.DBMSPrecondition
import liquibase.precondition.core.OrPrecondition
import liquibase.precondition.core.RunningAsPrecondition
import org.jetbrains.kotlin.utils.addToStdlib.assertedCast
import org.junit.Assert
import org.junit.Test

/**
 * Simple test that checks correct child node processing.
 */
class SimpleHierarchyTest {

    /**
     * Test integration factory.
     */
    private val liquibaseIntegration = LiquibaseIntegrationFactory()

    @Test
    fun testSingleChangeSet() {

        val changelog = (LkChangeLog() - {
            changeset(1) - {
                sql - testSql
            }
        })

        val result: DatabaseChangeLog = changelog.eval(liquibaseIntegration, testPath to DummyAccessor.instance)
        val changeSet = result.changeSets.firstOrNull()
        val changes = changeSet?.changes
        val change = changes?.firstOrNull()

        Assert.assertEquals(testPath, result.filePath)
        Assert.assertEquals(1, result.changeSets.size)
        Assert.assertEquals(1, changes?.size)
        Assert.assertTrue((change as RawSQLChange).run {true})
        Assert.assertEquals(testSql, change.sql)
    }

    @Test
    fun testAndOrPreconditions() {

        val changelog = (LkChangeLog() - {
            precondition.or - {
                and - {
                    or - {
                        dbms(testDbms)
                        runningAs(testUsername)
                    }
                }
            }
        })

        val result: DatabaseChangeLog = changelog.eval(liquibaseIntegration, testPath to DummyAccessor.instance)

        val changelogNested = result.preconditions.nestedPreconditions
        Assert.assertEquals(1, changelogNested.size)
        val innerPreconditions = changelogNested.firstOrNull()
            .assertedCast<OrPrecondition> { "Level 1 precondition should be Or precondition" }
            .nestedPreconditions
            .firstOrNull()
            .assertedCast<AndPrecondition> { "Level 2 precondition should be And precondition" }
            .nestedPreconditions
            .firstOrNull()
            .assertedCast<OrPrecondition> { "Level 3 precondition should be Or precondition" }
            .nestedPreconditions
        val dbmsPrecondition = innerPreconditions[0]
            .assertedCast<DBMSPrecondition> { "First inner precondition should be DBMSPrecondition" }
        val runningAsPrecondition = innerPreconditions[1]
            .assertedCast<RunningAsPrecondition> { "Second inner precondition should be RunningAsPrecondition" }
        Assert.assertEquals(testDbms.toLowerCase(), dbmsPrecondition.type)
        Assert.assertEquals(testUsername, runningAsPrecondition.username)
    }

    @Test
    fun testSingleChangeSetWithDefault() {
        val root = LkChangeLog()

        root.changeset.sql.splitStatements.setDefault(testSplitStatements)
        root.changeset.sql.stripComments.setDefault(testStripComments)

        val changelog = (root - {
            changeset(1) - {
                sql - testSql
            }
        })

        val result: DatabaseChangeLog = changelog.eval(liquibaseIntegration, testPath to DummyAccessor.instance)
        val changeSet = result.changeSets.firstOrNull()
        val changes = changeSet?.changes
        val change = changes?.firstOrNull()

        Assert.assertTrue((change as RawSQLChange).run {true})
        Assert.assertEquals(testSplitStatements, change.isSplitStatements)
        Assert.assertEquals(testStripComments, change.isStripComments)
    }

    @Test
    fun defaultsNotIntersectTest() {
        val changelog = (LkChangeLog() - {
            changeset(1) - {
                sql.splitStatements.setDefault(testSplitStatements)
                sql.stripComments.setDefault(testStripComments)

                sql - testSql
            }
            changeset(2) - {
                sql.splitStatements.setDefault(!testSplitStatements)
                sql.stripComments.setDefault(!testStripComments)

                sql - testSql
            }
        })

        val result: DatabaseChangeLog = changelog.eval(liquibaseIntegration, testPath to DummyAccessor.instance)
        val firstChange = result.changeSets[0].changes.first().assertedCast<RawSQLChange>()
        val secondChange = result.changeSets[1].changes.first().assertedCast<RawSQLChange>()

        Assert.assertEquals(testSplitStatements, firstChange.isSplitStatements)
        Assert.assertEquals(testStripComments, firstChange.isStripComments)

        Assert.assertEquals(!testSplitStatements, secondChange.isSplitStatements)
        Assert.assertEquals(!testStripComments, secondChange.isStripComments)
    }

    @Test
    fun multipleSipleChanges() {
        val changelog = (LkChangeLog() - {
            changeset(1) - {
                createTable(testTableName, testColumnName to testColumnType)
                dropTable(testTableName)
            }
            changeset(2) - {
                createView(testViewName) - testSql
            }
        })

        val result: DatabaseChangeLog = changelog.eval(liquibaseIntegration, testPath to DummyAccessor.instance)
        val firstChange = result.changeSets[0].changes[0].assertedCast<CreateTableChange>()
        val secondChange = result.changeSets[0].changes[1].assertedCast<DropTableChange>()
        val thirdChange = result.changeSets[1].changes[0].assertedCast<CreateViewChange>()

        Assert.assertEquals(testTableName, firstChange.tableName)
        Assert.assertEquals(testColumnName, firstChange.columns.first().name)
        Assert.assertEquals(testColumnType, firstChange.columns.first().type)
        Assert.assertEquals(testTableName, secondChange.tableName)
        Assert.assertEquals(testViewName, thirdChange.viewName)
        Assert.assertEquals(testSql, thirdChange.selectQuery)
    }

}
