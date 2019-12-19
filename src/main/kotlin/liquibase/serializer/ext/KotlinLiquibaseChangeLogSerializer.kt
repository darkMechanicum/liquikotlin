package liquibase.serializer.ext

import liquibase.changelog.ChangeLogChild
import liquibase.changelog.ChangeSet
import liquibase.serializer.ChangeLogSerializer
import liquibase.serializer.LiquibaseSerializable
import java.io.File
import java.io.OutputStream
import java.io.PrintWriter

/**
 * Main class for liquibase serialization logic.
 * It should be in `liquibase.serializer.ext` to be
 * found by liquibase itself.
 */
@Suppress("unused")
class KotlinLiquibaseChangeLogSerializer : ChangeLogSerializer {

    override fun append(changeSet: ChangeSet?, changeLogFile: File?) {
        throw NotImplementedError("Appending is not implemented yet")
    }

    override fun <T : ChangeLogChild?> write(children: MutableList<T>, out: OutputStream) = with(PrintWriter(out)) {
        println("changelog - {")
        children.map {  }
        println("}")
    }

    override fun serialize(`object`: LiquibaseSerializable?, pretty: Boolean): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getValidFileExtensions() = arrayOf("kts")

    override fun getPriority() = ChangeLogSerializer.PRIORITY_DEFAULT

}