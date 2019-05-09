package com.tsarev.liquikotlin

import com.tsarev.liquikotlin.embedded.KtsObjectLoader
import com.tsarev.liquikotlin.infrastructure.EvaluatableDslNode
import com.tsarev.liquikotlin.infrastructure.LbArg
import com.tsarev.liquikotlin.integration.LiquibaseIntegrationFactory
import com.tsarev.liquikotlin.printing.PrinterArg
import com.tsarev.liquikotlin.printing.PrinterMode
import com.tsarev.liquikotlin.printing.PrintingEvaluatorFactory
import com.tsarev.liquikotlin.verification.OutdatedReport
import com.tsarev.liquikotlin.verification.VerificationEvaluatorFactory
import liquibase.changelog.DatabaseChangeLog
import liquibase.resource.FileSystemResourceAccessor
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Instead of normal testing for a while.
 */
fun main() {
    // TODO check what the heck is this.
    System.setProperty("idea.use.native.fs.for.win", "false")
    val currentPath = Paths.get(".")
    println(currentPath.toAbsolutePath().toString())
    val filePath = Paths.get("build/resources/test/simpleScript.kts")
    val scriptReader = Files.newBufferedReader(filePath)
    var loadedObj: EvaluatableDslNode<*> = KtsObjectLoader().load(scriptReader)
    while (loadedObj.parent != null) {
        loadedObj = loadedObj.parent!!
    }
    val arg = filePath.toAbsolutePath().toString().replace("\\", "/") to FileSystemResourceAccessor()
    val result = loadedObj.eval<DatabaseChangeLog, LbArg>(LiquibaseIntegrationFactory(), arg)
    val result2 = loadedObj.eval<OutdatedReport, Any>(VerificationEvaluatorFactory(), null)
    loadedObj.eval<Any, PrinterArg>(PrintingEvaluatorFactory(), PrinterMode.FULL to System.out)
//    println(result2)
}