package com.tsarev.liquikotlin

import com.tsarev.liquikotlin.embedded.KtsObjectLoader
import com.tsarev.liquikotlin.infrastructure.EvaluatableDslNode
import com.tsarev.liquikotlin.printing.PrinterArg
import com.tsarev.liquikotlin.printing.PrinterMode
import com.tsarev.liquikotlin.printing.PrintingEvaluatorFactory
import com.tsarev.liquikotlin.util.letWhile
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
    val rootNode = KtsObjectLoader().load<EvaluatableDslNode<*>>(scriptReader).letWhile { it.parent }
    rootNode.eval<Any, PrinterArg>(PrintingEvaluatorFactory(), PrinterMode.PRETTY to System.err)
}