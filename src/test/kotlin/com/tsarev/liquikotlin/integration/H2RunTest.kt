package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.util.*
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.FileSystemResourceAccessor
import org.junit.Test
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

/**
 * Testing script execution on in memory H2 database.
 */
class H2RunTest : RuleChainAwareTest() {

    val conn: Connection by resource { DriverManager.getConnection("jdbc:h2:mem:test") }

    @Test
    fun simpleChangeLogTest() = testLiquibase("build/resources/test/simpleScript.kts") {
        assertTableOrViewExist(testTableName)
        assertColumnExist(testTableName, testColumnName)
        assertIsNullable(testTableName, testColumnName)
    }

    @Test
    fun complexChangeLogTest() = testLiquibase("build/resources/test/complexScript.kts") {
        assertTableOrViewExist(testTableName)

        assertColumnExist(testTableName, testColumnName)
        assertIsNullable(testTableName, testColumnName, testPrimaryNullable)
        assertIsPrimary(testTableName, testColumnName)
        assertHasAutoIncrement(testTableName, testColumnName)

        assertColumnExist(testTableName, testSecondColumnName)
        assertIsNullable(testTableName, testSecondColumnName, false)
        assertHasConstraint(testTableName, testSecondColumnName, ConstraintTypes.UNIQUE)
        assertHasIndex(testTableName, testSecondColumnName)
        assertHasDefault(testTableName, testSecondColumnName, 5)

        assertTableOrViewExist(testViewName)

        assertSequenceExist(testSequenceName)
    }

    /**
     * Perform liquibase update for specified script and run assertions.
     */
    private fun testLiquibase(path: String, assertions: Connection.() -> Unit) {
        val script = File(path)
        val liquibase = Liquibase(script.patchedAbs, FileSystemResourceAccessor(), JdbcConnection(conn))
        liquibase.update("")
        conn.assertions()
    }

}