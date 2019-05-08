package liquibase.parser.ext

import liquibase.changelog.ChangeLogParameters
import liquibase.changelog.DatabaseChangeLog
import liquibase.parser.ChangeLogParser
import liquibase.resource.ResourceAccessor

/**
 * Dummy parser for tests to test include and include all.
 */
@Suppress("unused")
open class DummyParser : ChangeLogParser {

    companion object {
        var hitFilePaths = ArrayList<String>()
        fun resetParser() = hitFilePaths.clear()
    }

    override fun parse(
        physicalChangeLogLocation: String,
        changeLogParameters: ChangeLogParameters?,
        resourceAccessor: ResourceAccessor
    ) = DatabaseChangeLog(physicalChangeLogLocation).also { hitFilePaths.add(physicalChangeLogLocation) }

    override fun supports(changeLogFile: String, resourceAccessor: ResourceAccessor) =
        changeLogFile.endsWith(".dummy")

    override fun getPriority() = Int.MAX_VALUE
}