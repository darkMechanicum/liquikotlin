package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.util.RuleChainAwareTest
import com.tsarev.liquikotlin.util.assertedCast
import com.tsarev.liquikotlin.util.patchedAbs
import liquibase.change.core.RawSQLChange
import liquibase.changelog.DatabaseChangeLog
import liquibase.parser.ext.KotlinLiquibaseChangeLogParser
import liquibase.resource.FileSystemResourceAccessor
import org.junit.Assert
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.PrintStream

const val firstFileName = "file1.kts"
const val secondFileName = "file2.kts"
const val thirdFileName = "file3.kts"

const val firstSql = "select 1 from dual"
const val secondSql = "select 2 from dual"

const val firstScript = """
    import com.tsarev.liquikotlin.bundled.*
    changelog.changeSet(1).sql - "$firstSql"
"""

const val secondScript = """
    import com.tsarev.liquikotlin.bundled.*
    changelog - {
        include("$firstFileName").relativeToChangelogFile(true)
        changeSet(2).sql - "$secondSql"
    }
"""

const val thirdScript = """
    import com.tsarev.liquikotlin.bundled.*
    changelog.include("$secondFileName").relativeToChangelogFile(true)
"""

/**
 * Testing parser work.
 */
class ParserTest : RuleChainAwareTest() {

    private val tempFolder by rule { TemporaryFolder() }

    private val firstFile by init { tempFolder.newFile(firstFileName).setContent(firstScript) }

    private val secondFile by init { tempFolder.newFile(secondFileName).setContent(secondScript) }

    private val thirdFile by init { tempFolder.newFile(thirdFileName).setContent(thirdScript) }

    private val parser = KotlinLiquibaseChangeLogParser()

    private fun File.setContent(content: String): File = this.apply {
        PrintStream(this).use {
            it.print(content)
        }
    }

    /**
     * Assert that two first changes from two change sets are RawSQLChange with specific query.
     */
    private fun DatabaseChangeLog.assertChanges() {
        val firstChange = changeSets.firstOrNull()?.changes?.firstOrNull()
        val secondChange = changeSets.getOrNull(1)?.changes?.firstOrNull()
        Assert.assertEquals(firstSql, firstChange.assertedCast<RawSQLChange>().sql)
        Assert.assertEquals(secondSql, secondChange.assertedCast<RawSQLChange>().sql)
    }

    @Test
    fun simpleParseTest() {
        val changeLog = parser.parse(
            firstFile.patchedAbs,
            null,
            FileSystemResourceAccessor(File("/"))
        )
        val firstChange = changeLog.changeSets.firstOrNull()?.changes?.firstOrNull()
        Assert.assertEquals(firstSql, firstChange.assertedCast<RawSQLChange>().sql)
    }

    @Test
    fun includeParseTest() {
        val changeLog = parser.parse(
            secondFile.patchedAbs,
            null,
            FileSystemResourceAccessor(File("/"))
        )
        changeLog.assertChanges()
    }

    @Test
    fun transitiveIncludeParseTest() {
        val changeLog = parser.parse(
            thirdFile.patchedAbs,
            null,
            FileSystemResourceAccessor(File("/"))
        )
        changeLog.assertChanges()
    }

}