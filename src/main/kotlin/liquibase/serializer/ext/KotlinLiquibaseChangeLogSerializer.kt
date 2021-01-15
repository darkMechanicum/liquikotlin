package liquibase.serializer.ext

import liquibase.change.Change
import liquibase.change.ColumnConfig
import liquibase.change.ConstraintsConfig
import liquibase.changelog.ChangeLogChild
import liquibase.changelog.ChangeSet
import liquibase.serializer.ChangeLogSerializer
import liquibase.serializer.LiquibaseSerializable
import liquibase.sql.visitor.SqlVisitor
import liquibase.util.ISODateFormat
import java.io.File
import java.io.OutputStream
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Timestamp

/**
 * Main class for liquibase serialization logic.
 * It should be in `liquibase.serializer.ext` to be
 * found by liquibase itself.
 */
@Suppress("unused")
class KotlinLiquibaseChangeLogSerializer : ChangeLogSerializer {

    private val isoFormat: ISODateFormat = ISODateFormat()

    private val indentation = "    ";

    override fun getValidFileExtensions() = arrayOf("kts")

    override fun getPriority() = ChangeLogSerializer.PRIORITY_DEFAULT

    /**
     * Convert a single serializable Liquibase change into its Groovy
     * representation.
     * @param change the change to serialize.
     * @param pretty whether or not to make it pretty.  It doesn't matter what
     *        you pass here because this DSL refuses to make a serialization that
     *        isn't pretty.
     * @return the Groovy representation of the change.
     */
    override fun serialize(change: LiquibaseSerializable, pretty: Boolean): String {
        return when(change) {
            is ChangeSet -> serializeChangeSet(change)
            is Change -> serializeChange(change)
            is ColumnConfig -> serializeColumnConfig(change)
            is ConstraintsConfig -> serializeConstraintsConfig(change)
            is SqlVisitor -> serializeVisitor(change)
            else -> return serializeObject(change)
        }
    }

    override fun <T : ChangeLogChild> write(children: MutableList<T>, out: OutputStream) {
        out.writer().apply {
            appendln("changelog - {")
            appendln(children.joinToString("\n") { indent(serialize(it, true)) })
            appendln("}")
        }
    }

    override fun append(changeSet: ChangeSet, changeLogFile: File) {
        throw NotImplementedError(
            """KotlinLiquibaseChangeLogSerializer does not append changelog content.
  To append a newly generated changelog to an existing changelog, specify a new filename
  for the new changelog, then copy and paste that content into the existing file."""
        )
    }


    //---------------------------------------------------------------------------
    // In Liquibase 2.x, there were many different serialize methods for different
    // types of liquibase objects. In version 3.0, the different serializable
    // objects all implement a common interface, and the serialize methods were
    // replaced by a single method.  However, we want to do different things with
    // different objects with respect to ordering of attributes, etc. Therefore,
    // certain objects have their own serialize methods which will be called by
    // the master method.  These methods are private to force outside classes to
    // use serialization method defined by the interface.

    private fun serializeObject(change: LiquibaseSerializable): String {
        val fields = change.serializableFields
        val children = mutableListOf<String>()
        val attributes = mutableListOf<String>()
        var textBody: String? = null
        for (field in fields) {
            val fieldValue = change.getSerializableFieldValue(field) ?: continue
            val serializationType = change.getSerializableFieldType(field)

            when {
                fieldValue is Collection<*> -> {
                    fieldValue.filterIsInstance<LiquibaseSerializable>()
                        .forEach { children += serialize(it, true) }
                }
                field in listOf("procedureBody", "sql", "selectQuery") -> {
                    textBody = fieldValue.toString()
                }
                fieldValue is ChangeSet -> {
                    children += serializeChangeSet(fieldValue)
                }
                fieldValue is LiquibaseSerializable -> {
                    children += serialize(fieldValue, true)
                    //            } else if (serializationType == LiquibaseSerializable.SerializationType.NESTED_OBJECT) {
                    //                children += serialize(fieldValue, true); TODO what is it?
                }
                serializationType == LiquibaseSerializable.SerializationType.DIRECT_VALUE -> {
                    textBody += fieldValue.toString()
                }
                else -> {
                    attributes += field
                }
            }
        }

        attributes.sort()

        var serializedChange = if (attributes.isNotEmpty()) {
            "${change.serializedObjectName}.${buildPropertyListFrom(attributes, objectToMap(change)).joinToString(".")}"
        } else {
            change.serializedObjectName
        }

        if (children.isNotEmpty()) {
            val renderedChildren = children.joinToString("\n") { indent(it) }
            serializedChange = """
                $serializedChange - {
                $renderedChildren
                }""".trimIndent()
        } else if (!textBody.isNullOrBlank()) {
            serializedChange = """
                $serializedChange - ""${'"'}
                $indentation$textBody
                ""${'"'}
                """
        }

        return serializedChange
    }

    fun serializeChangeSet(changeSet: ChangeSet): String {
        val attrNames = listOf("id", "author", "runAlways", "runOnChange", "failOnError", "context", "dbms")
        val attributes = mutableMapOf(
            "id" to changeSet.id,
            "author" to changeSet.author
        )

        val children = mutableListOf<String>()

        //
        // Do these the hard way to keep them out of the map if they're false
        //

        if (changeSet.isAlwaysRun) attributes["runAlways"] = "true"
        if (changeSet.isRunOnChange) attributes["runOnChange"] = "true"
        if (changeSet.failOnError) attributes["failOnError"] = changeSet.failOnError?.toString()
        if (changeSet.contexts != null && changeSet.contexts.contexts != null)
            attributes["context"] = changeSet.contexts.contexts.joinToString(",")
        if (changeSet.dbmsSet != null)
            attributes["dbms"] = changeSet.dbmsSet.joinToString(",")
        if (changeSet.comments?.trim()?.isNotBlank() == true) {
            children += "comment(\"${changeSet.comments.replace("\"", "\\\"")}\")"
        }
        changeSet.changes.forEach { children += serialize(it, true) }

        val renderedChildren = children.joinToString("\n") { indent(it) }
        return """
        |changeSet.${buildPropertyListFrom(attrNames, attributes).joinToString(".")} - {
        |$renderedChildren
        |}
        """.trimMargin()
    }


    fun serializeChange(change: Change): String {
        val fields = change.serializableFields
        val children = mutableListOf<String>()
        val attributes = mutableListOf<String>()
        var textBody: String? = null
        for (field in fields) {
            val fieldValue = change.getSerializableFieldValue(field)
            when {
                fieldValue is Collection<*> -> {
                    fieldValue.filterIsInstance<ColumnConfig>().forEach { children += serializeColumnConfig(it) }
                }
                fieldValue is ColumnConfig -> {
                    children += serialize(fieldValue, true)
                }
                field in listOf("procedureBody", "sql", "selectQuery") -> {
                    textBody = fieldValue?.toString()
                }
                else -> {
                    attributes += field
                }
            }
        }

        attributes.sort()

        var serializedChange = if (attributes.isNotEmpty()) {
            "${change.serializedObjectName}.${buildPropertyListFrom(attributes, objectToMap(change)).joinToString(".")}"
        } else {
            change.serializedObjectName
        }

        if (children.isNotEmpty()) {
            val renderedChildren = children.joinToString("\n") { indent(it) }
            serializedChange = """
                |$serializedChange - {
                |$renderedChildren
                |}""".trimMargin()
        } else if (!textBody.isNullOrEmpty()) {
            serializedChange = """
                |$serializedChange - ""${'"'}
                |$indentation$textBody
                |""${'"'}
                """.trimMargin()
        }

        return serializedChange
    }
    
    fun serializeColumnConfig(columnConfig: ColumnConfig): String {
        val propertyNames = listOf(
            "name", "type", "value", "valueNumeric", 
            "valueDate", "valueBoolean", "valueComputed", "defaultValue", 
            "defaultValueNumeric", "defaultValueDate", "defaultValueBoolean", "defaultValueComputed", 
            "autoIncrement", "remarks"
        )
        val properties = buildPropertyListFrom(propertyNames, objectToMap(columnConfig))
        val column = "column.${properties.joinToString(".")}"
        return if (columnConfig.constraints != null) {
            """
            $column - {
            $indentation${serialize(columnConfig.constraints, true)}
            }""".trimIndent()
        } else {
            column
        }
    }


    fun serializeConstraintsConfig(constraintsConfig: ConstraintsConfig): String {
        val propertyNames = listOf(
            "nullable", "primaryKey", "primaryKeyName", "primaryKeyTablespace",
            "references", "referencedTableName", "referencedColumnNames", "unique",
            "uniqueConstraintName", "checkConstraint", "deleteCascade", "foreignKeyName",
            "initiallyDeferred", "deferrable"
        )
        return "constraints.${buildPropertyListFrom(propertyNames, objectToMap(constraintsConfig)).joinToString(".")}"
    }

    fun serializeVisitor(visitor: SqlVisitor): String {
        return "${visitor.name}.${buildPropertyListFrom(visitor.serializableFields, objectToMap(visitor)).joinToString(".")}"
    }

    /**
     * Indents lines of text by two spaces.
     * @param text the text to indent
     * @return the indented text
     */
    private fun <T: String?> indent(text: T) = text?.lines()?.joinToString("\n") { "$indentation$it" } as T

    /**
     * Builds the correct string representation of an object's properties based
     * on the property's type.
     * @param propertyNames the names of the properties we're interested in.
     * @param parameters an array of strings representing each property.
     * @return
     */
    private fun buildPropertyListFrom(propertyNames: Collection<String>, parameters: Map<String, Any?>): List<String> {
        val properties = mutableListOf<String>()
        propertyNames.forEach { propertyName ->
            val propertyValue = parameters[propertyName]
            if (propertyValue != null) {
                val propertyString = when(propertyValue) {
                    is Boolean,
                    is BigInteger,
                    is BigDecimal,
                    is Number -> propertyValue.toString()
                    is Timestamp -> "\"\"\"${isoFormat.format(propertyValue)}\"\"\""
                    else -> "\"$propertyValue\""
                }
                properties += "${propertyName}($propertyString)"
            }
        }
        return properties
    }

    private fun objectToMap(any: Any): MutableMap<String, Any?> {
        val result = mutableMapOf<String, Any?>()
        var clazz: Class<*> = any::class.java
        while (clazz != Object::class.java) {
            clazz.declaredFields.forEach {
                it.isAccessible = true
                result[it.name] = it.get(any)
            }
            clazz = clazz.superclass
        }
        return result
    }

}