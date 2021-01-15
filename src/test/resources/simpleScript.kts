import com.tsarev.liquikotlin.bundled.changelog
import com.tsarev.liquikotlin.util.*

/*
 * This is a test script. See [H2RunTest].
 */

changelog.changeSet.author.default = "darkMechanicum"

changelog - {
    property.name(testProperty).value(testValue)

    changeSet.id(1) - {
        comment.text(testComment)

        rollback - {
            dropTable.tableName(testTableName)
        }

        createTable.tableName(testTableName) - {
            column
                .name(testColumnName)
                .type(testColumnType)
                .constraints.nullable(testNullable)
        }
    }
}