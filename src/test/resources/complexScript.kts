import com.tsarev.liquikotlin.bundled.changelog
import com.tsarev.liquikotlin.bundled.minus
import com.tsarev.liquikotlin.util.*

/*
 * This is a test script. See [H2RunTest].
 */

changelog.changeset.author.default = "darkMechanicum"

changelog - {
    changeset.id(1) - {
        // Create changes.
        createTable.tableName(testTableName) - {
            column.name(testColumnName)
                .type(testColumnType)
                .constraints.nullable(testPrimaryNullable)
        }

        addColumn.tableName(testTableName)
            .column
            .name(testSecondColumnName)
            .type(testColumnType)

        addAutoIncrement
            .tableName(testTableName)
            .columnName(testColumnName)
            .columnDataType(testColumnType)

        addDefaultValue
            .tableName(testTableName)
            .columnName(testSecondColumnName)
            .defaultValueNumeric(5)

        addNotNullConstraint
            .tableName(testTableName)
            .columnDataType(testColumnType)
            .columnName(testSecondColumnName)

        addPrimaryKey
            .tableName(testTableName)
            .columnNames(testColumnName)

        addUniqueConstraint
            .tableName(testTableName)
            .columnNames(testSecondColumnName)

        createIndex
            .tableName(testTableName)
            .indexName(testIndexName)
            .column.name(testColumnName)
            .constraints.nullable(testNullable)

        createSequence.sequenceName(testSequenceName)

        createView.viewName(testViewName) - "select * from $testTableName"

        // TODO Remaining create changes.
        // TODO Drop changes.
        // TODO Other changes.
    }
}