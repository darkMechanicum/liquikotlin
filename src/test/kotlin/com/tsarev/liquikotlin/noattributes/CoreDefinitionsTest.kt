package com.tsarev.liquikotlin.noattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.LkChangeLog
import liquibase.changelog.ChangeSet
import liquibase.changelog.DatabaseChangeLog
import org.junit.Ignore
import org.junit.Test

/**
 * Testing that basic evaluations do not add any
 * values except required ones.
 */
class CoreDefinitionsTest : BaseLiquikotlinUnitTest() {

    @Test
    fun changeLogTest() = testEvaluation(
        LkChangeLog(),
        DatabaseChangeLog::class,
        DatabaseChangeLog::getLogicalFilePath to dummyPath,
        DatabaseChangeLog::getPhysicalFilePath to dummyPath,
        DatabaseChangeLog::getChangeSets to emptyList<ChangeSet>()
    )

    // TODO Implement
    @Test
    @Ignore
    fun includeTest() {
    }

    // TODO Implement
    @Test
    @Ignore
    fun includeAllTest() {
    }

    // TODO Implement
    @Test
    @Ignore
    fun propertyTest() {
    }

    // TODO Implement
    @Test
    @Ignore
    fun changeSetTest() {
    }

    // TODO Implement
    @Test
    @Ignore
    fun rollbackTest() {
    }

    // TODO Implement
    @Test
    @Ignore
    fun validCheckSumTest() {
    }

    // TODO Implement
    @Test
    @Ignore
    fun commentTest() {
    }
}