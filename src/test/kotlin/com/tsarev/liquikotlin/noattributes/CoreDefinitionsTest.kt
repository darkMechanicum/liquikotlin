package com.tsarev.liquikotlin.noattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.LkChangeLog
import com.tsarev.liquikotlin.bundled.LkChangeSet
import com.tsarev.liquikotlin.integration.ChangesHolder
import com.tsarev.liquikotlin.util.testId
import com.tsarev.liquikotlin.util.testPath
import liquibase.change.Change
import liquibase.changelog.ChangeSet
import liquibase.changelog.DatabaseChangeLog
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
        DatabaseChangeLog::getLogicalFilePath to testPath,
        DatabaseChangeLog::getPhysicalFilePath to testPath,
        DatabaseChangeLog::getChangeSets to emptyList<ChangeSet>()
    )

    @Test
    fun changeSetTest() = testEvaluation(
        LkChangeSet().id(testId),
        ChangesHolder::class,
        ChangesHolder::changes to emptyList<Change>(),
        ChangesHolder::preconditions,
        ChangesHolder::rollback,
        ChangesHolder::comments to emptyList<String>(),
        ChangesHolder::validCheckSums to emptyList<String>()
    )

}