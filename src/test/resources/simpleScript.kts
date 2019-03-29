import com.tsarev.liquikotlin.bundled.changelog
import com.tsarev.liquikotlin.extensions.*

changelog.changeset.sql.stripComments.setDefault(true)
changelog.changeset.sql.splitStatements.setDefault(true)
changelog.changeset.author("Alice")

changelog - {
    changeset(1) - {
        createTable("newTable").columns(
            "id" to "number",
            "name" to "varchar")

        createIndex("newTable", "id")
    }

    changeset(2) - {

        addColumn("newTable").column("newData")

        addNotNullConstraint("newTable", "newData")

        createView("newView").replaceIfExists(true) -
                "select * from newTable"
    }
}