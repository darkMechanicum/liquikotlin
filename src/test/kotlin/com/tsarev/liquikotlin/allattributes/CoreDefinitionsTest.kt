package com.tsarev.liquikotlin.allattributes

import com.tsarev.liquikotlin.BaseLiquikotlinUnitTest
import com.tsarev.liquikotlin.bundled.LkChangeLog
import com.tsarev.liquikotlin.bundled.invoke
import com.tsarev.liquikotlin.util.*
import liquibase.change.CheckSum
import liquibase.changelog.DatabaseChangeLog
import org.junit.Test

/**
 * Testing that basic evaluations process all attributes
 * correctly without modifications.
 */
class CoreDefinitionsTest : BaseLiquikotlinUnitTest() {

    @Test
    fun propertyTest() = testEvaluation(
        LkChangeLog().property(testProperty, testValue),
        DatabaseChangeLog::class,
        { changelog: DatabaseChangeLog -> changelog.changeLogParameters.getValue(testProperty, changelog) } to testValue
    )

    @Test
    fun changeSetTest() = testEvaluation(
        LkChangeLog().changeset
            .id(testId)
            .author(testAuthor)
            .dbms(testDbms)
            .runAlways(testRunAlways)
            .runOnChange(testRunOnChange)
            .context(testContext)
            .runInTransaction(testRunInTransaction)
            .failOnError(testFailOnError) - {
                validCheckSum(testCheckSum)
                comment(testComment)
            },
        DatabaseChangeLog::class,
        { it: DatabaseChangeLog -> it.changeSets.first().id } to testId,
        { it: DatabaseChangeLog -> it.changeSets.first().author } to testAuthor,
        { it: DatabaseChangeLog -> it.changeSets.first().isAlwaysRun } to testRunAlways,
        { it: DatabaseChangeLog -> it.changeSets.first().isRunOnChange } to testRunOnChange,
        { it: DatabaseChangeLog -> it.changeSets.first().contexts.contexts } to hashSetOf(testContext.toLowerCase()),
        { it: DatabaseChangeLog -> it.changeSets.first().isRunInTransaction } to testRunInTransaction,
        { it: DatabaseChangeLog -> it.changeSets.first().failOnError } to testFailOnError,
        { it: DatabaseChangeLog -> it.changeSets.first().validCheckSums } to hashSetOf(CheckSum.parse(testCheckSum)),
        { it: DatabaseChangeLog -> it.changeSets.first().comments } to testComment,
        { it: DatabaseChangeLog -> it.changeSets.first().dbmsSet } to setOf(testDbms.toLowerCase())
    )

}