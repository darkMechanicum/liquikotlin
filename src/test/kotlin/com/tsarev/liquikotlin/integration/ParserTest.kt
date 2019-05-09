package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.util.assertedCast
import liquibase.change.core.RawSQLChange
import liquibase.changelog.DatabaseChangeLog
import liquibase.parser.ext.KotlinLiquibaseChangeLogParser
import liquibase.resource.FileSystemResourceAccessor
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
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
    changelog.changeset(1).sql - "$firstSql"
"""

const val secondScript = """
    import com.tsarev.liquikotlin.bundled.*
    changelog - {
        include("$firstFileName").relativeToChangelogFile(true)
        changeset(2).sql - "$secondSql"
    }
"""

const val thirdScript = """
    import com.tsarev.liquikotlin.bundled.*
    changelog.include("$secondFileName").relativeToChangelogFile(true)
"""

/**
 * Testing parser work.
 */
class ParserTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var firstFile: File

    private lateinit var secondFile: File

    private lateinit var thirdFile: File

    private val parser = KotlinLiquibaseChangeLogParser()

    @Before
    fun initFiles() {
        firstFile = tempFolder.newFile(firstFileName).setContent(firstScript)
        secondFile = tempFolder.newFile(secondFileName).setContent(secondScript)
        thirdFile = tempFolder.newFile(thirdFileName).setContent(thirdScript)
    }

    /**
     * Path with replaced '\'s since liquibase interprets them incorrectly in win environment.
     */
    private val File.patchedAbs get() = this.absolutePath.replace('\\', '/')

    private fun File.setContent(content: String): File = this.apply {
        PrintStream(this).use {
            it.print(content)
        }
    }

    private fun DatabaseChangeLog.assertChanges() {
        val firstChange = changeSets.firstOrNull()?.changes?.firstOrNull()
        val secondChange = changeSets.getOrNull(1)?.changes?.firstOrNull()

        Assert.assertEquals(firstSql, firstChange.assertedCast<RawSQLChange>().sql)
        Assert.assertEquals(secondSql, secondChange.assertedCast<RawSQLChange>().sql)
    }

    @Test
    fun simpleParseTest() {
        val changeLog = parser.parse(firstFile.patchedAbs, null, FileSystemResourceAccessor())
        val firstChange = changeLog.changeSets.firstOrNull()?.changes?.firstOrNull()
        Assert.assertEquals(firstSql, firstChange.assertedCast<RawSQLChange>().sql)
    }

    @Test
    fun includeParseTest() {
        val changeLog = parser.parse(secondFile.patchedAbs, null, FileSystemResourceAccessor())
        changeLog.assertChanges()
    }

    @Test
    fun transitiveIncludeParseTest() {
        val changeLog = parser.parse(thirdFile.patchedAbs, null, FileSystemResourceAccessor())
        changeLog.assertChanges()
    }

}