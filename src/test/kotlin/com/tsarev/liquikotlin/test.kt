package com.tsarev.liquikotlin

import com.tsarev.liquikotlin.bundled.LkChange
import com.tsarev.liquikotlin.bundled.LkChangeSet
import com.tsarev.liquikotlin.bundled.LkSql
import com.tsarev.liquikotlin.embedded.KtsObjectLoader
import com.tsarev.liquikotlin.infrastructure.EvaluatableDslNode
import liquibase.change.core.RawSQLChange
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
    var loadedObj: EvaluatableDslNode<*, *, Any?> = KtsObjectLoader().load(scriptReader)
    while (loadedObj.parent != null) {
        loadedObj = loadedObj.parent!!
    }
    val result = loadedObj.eval(filePath.toAbsolutePath().toString().replace("\\", "/") to FileSystemResourceAccessor())
    println(result)
}