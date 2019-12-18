package com.tsarev.liquikotlin.complex

import com.tsarev.liquikotlin.bundled.LkChangeLog
import com.tsarev.liquikotlin.infrastructure.LbArg
import com.tsarev.liquikotlin.infrastructure.eval
import com.tsarev.liquikotlin.integration.LiquibaseIntegrationFactory
import com.tsarev.liquikotlin.util.DummyAccessor
import com.tsarev.liquikotlin.util.testIncludePath
import com.tsarev.liquikotlin.util.testPath
import liquibase.changelog.IncludeAllFilter
import liquibase.parser.ext.DummyParser
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Simple test that checks correct child node processing.
 */
class IncludeTest {

    /**
     * Test integration factory.
     */
    private val liquibaseIntegration = LiquibaseIntegrationFactory()

    @Before
    fun clearDummyParser() = DummyParser.resetParser()

    @Test
    fun testInclude() {
        // Prepare test data.
        val root = LkChangeLog()
        val changelog = (root - {
            include.path(testIncludePath).relativeToChangelogFile(true)
            include.path(testIncludePath).relativeToChangelogFile(false)
        })

        // Evaluate.
        changelog.eval<Any, LbArg>(liquibaseIntegration, testPath to DummyAccessor.instance)

        // Test.
        Assert.assertEquals(listOf("/$testIncludePath", testIncludePath), DummyParser.hitFilePaths)
    }

    @Test
    fun testIncludeAll() {
        // Prepare test data.
        val includeDir = "root/"
        val fooFile = "foo.dummy"
        val barFile = "bar.dummy"
        val accessor = DummyAccessor(mutableSetOf(fooFile, barFile))
        val root = LkChangeLog()
        val changelog = (root - {
            includeAll.path(includeDir)
                .relativeToChangelogFile(true)
                .errorIfMissingOrEmpty(true)
                .resourceFilter(DummyFilter::class.qualifiedName)
        })

        // Evaluate.
        changelog.eval<Any, LbArg>(liquibaseIntegration, testPath to accessor)

        // Test.
        Assert.assertEquals(listOf(barFile, fooFile), DummyParser.hitFilePaths)
    }

}

class DummyFilter : IncludeAllFilter {
    override fun include(changeLogPath: String?) = true
}
