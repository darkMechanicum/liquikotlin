package com.tsarev.liquikotlin.embedded

import de.swirtz.ktsrunner.objectloader.LoadException
import java.io.InputStream
import java.io.Reader
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

/**
 * Embedded class from https://github.com/s1monw1/KtsRunner since it is build
 * with 1.6 and so inline functions cannot be linked within 1.8 project.
 */
class KtsObjectLoader(classLoader: ClassLoader? = Thread.currentThread().contextClassLoader) {

    val engine: ScriptEngine = ScriptEngineManager(classLoader).getEngineByExtension("kts")!!

    inline fun <R> safeEval(evaluation: () -> R?) = try {
        evaluation()
    } catch (e: Exception) {
        throw LoadException("Cannot load script", e)
    }

    inline fun <reified T> Any?.castOrError() = takeIf { it is T }?.let { it as T }
        ?: throw IllegalArgumentException("Cannot cast $this to expected type ${T::class}")

    inline fun <reified T> load(reader: Reader): T = safeEval { engine.eval(reader) }.castOrError()

    inline fun <reified T> loadAll(vararg readers: Reader): List<T> = readers.map(::load)
}

