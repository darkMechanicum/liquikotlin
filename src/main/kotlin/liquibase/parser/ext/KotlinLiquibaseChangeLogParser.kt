/*
 * Copyright 2011-2017 Tim Berglund and Steven C. Saliman
 * Kotlin conversion done by Jason Blackwell
 * Adaptation for Liquikotlin made by Tsarev Alexander
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package liquibase.parser.ext

import com.tsarev.liquikotlin.bundled.LkChangeLog
import com.tsarev.liquikotlin.bundled.changelog
import com.tsarev.liquikotlin.infrastructure.EvaluatableDslNode
import com.tsarev.liquikotlin.infrastructure.LbArg
import com.tsarev.liquikotlin.integration.LiquibaseIntegrationFactory
import com.tsarev.liquikotlin.util.letWhile
import liquibase.changelog.ChangeLogParameters
import liquibase.changelog.DatabaseChangeLog
import liquibase.exception.ChangeLogParseException
import liquibase.parser.ChangeLogParser
import liquibase.resource.ResourceAccessor
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.PrintStream
import java.util.*
import javax.script.Compilable
import javax.script.ScriptEngineManager
import javax.script.ScriptException

/**
 * This is the main parser class for the Liquibase Kotlin DSL.  It is the
 * integration point to Liquibase itself.  It must be in the
 * bundled.parser.ext package to be found by Liquibase at runtime.
 *
 * @author Tim Berglund
 * @author Steven C. Saliman
 * @author Jason Blackwell
 * @author Tsarev Alexander
 */
@Suppress("unused")
open class KotlinLiquibaseChangeLogParser : ChangeLogParser {

    companion object {
        /**
         * Singleton script engine instance.
         */
        val scriptEngine = ScriptEngineManager().getEngineByExtension("kts")!! as Compilable

        /**
         * NoOp printer to ignore junk messages.
         */
        val noOpPrinter = PrintStream(NoOpOutputStream())

        /**
         * Stack to isolate changelogs from each other.
         */
        val changeLogStack = ArrayDeque<LkChangeLog>()
    }

    override fun parse(
        physicalChangeLogLocation: String,
        changeLogParameters: ChangeLogParameters?,
        resourceAccessor: ResourceAccessor
    ): DatabaseChangeLog {
        val realLocation = physicalChangeLogLocation.replace("\\\\", "/")
        val inputStreams = resourceAccessor.getResourcesAsStream(realLocation)
        val firstStream = inputStreams?.first() ?: throw ChangeLogParseException("$realLocation does not exist")
        if (inputStreams.size > 1) {
            System.err.println("Warning: Ambiguous resources for path: $physicalChangeLogLocation")
        }
        return handleStream(firstStream, physicalChangeLogLocation, resourceAccessor)
    }

    /**
     * Handle single script stream with ScriptEngine.
     */
    private fun handleStream(
        stream: InputStream,
        location: String,
        resourceAccessor: ResourceAccessor
    ): DatabaseChangeLog {
        val err = System.err
        val out = System.out
        val compiled = try {
            System.setErr(noOpPrinter)
            System.setOut(noOpPrinter)
            InputStreamReader(stream).use {
                scriptEngine.compile(it)
            }
        } catch (e: ScriptException) {
            throw ScriptException("Compilation error", "$location.kts", e.lineNumber, e.columnNumber)
                .apply { initCause(e) }
        } finally {
            System.setErr(err)
            System.setOut(out)
        }
        try {
            // Save current script state, if any, since next script can ruin it.
            changeLogStack.push(changelog)
            changelog = LkChangeLog()
            // TODO Add type check
            val result = compiled.eval() as EvaluatableDslNode<*>
            val arg: LbArg = location to resourceAccessor
            return result.letWhile { it.parent }.evalSafe(LiquibaseIntegrationFactory(), arg)
        } finally {
            // Restore current script state, if any.
            changelog = changeLogStack.pop()
        }
    }

    override fun supports(changeLogFile: String, resourceAccessor: ResourceAccessor): Boolean {
        return changeLogFile.endsWith(".kts")
    }

    override fun getPriority(): Int = ChangeLogParser.PRIORITY_DEFAULT
}

/**
 * Dummy stream to ignore junk messages.
 */
class NoOpOutputStream : OutputStream() {
    override fun write(b: Int) {
        // No-op
    }

}