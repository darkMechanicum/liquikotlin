import com.tsarev.liquikotlin.bundled.changelog
import com.tsarev.liquikotlin.extensions.*

changelog.changeset.sql.splitStatements.setDefault(true)
changelog.changeset.sql.stripComments.setDefault(true)

changelog - {
    changeset(1).createTable("table", "id" to "number")
    changeset(2).dropTable("table")
    changeset(3).createTable("table2", "id" to "number")
    changeset(4).dropTable("table2")
}