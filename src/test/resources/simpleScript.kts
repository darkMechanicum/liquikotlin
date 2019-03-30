import com.tsarev.liquikotlin.bundled.changelog
import com.tsarev.liquikotlin.extensions.*

changelog.changeset.sql.splitStatements.setDefault(true)
changelog.changeset.sql.stripComments.setDefault(true)

changelog - {
    precondition.dbms("oracle")

    changeset(1).sql - "insert all"

    changeset(2) - {
        validCheckSum("5")

        createTable("newTable", "id" to "number")
        dropTable("oldTable")
    }
}