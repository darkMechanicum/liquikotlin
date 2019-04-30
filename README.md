# Liquikotlin 
Kotlin DSL for liquibase.

### TL;DR
Inspired by liquibase [groovy dsl](#groovy_dsl_anchor) and [gradle kotlin dsl](#kotlin_dsl_anchor)
this library allows writing kotlin scripts instead of raw
xml for liquibase.

Advantages are quite obvious, starting from more compact and IDE
supported scripts to pure kotlin possibilities like extension functions.

### Description
Scripts are written in files with standard kotlin script extension `kts`.
Entry point for each script is changelog variable.

Nested tags can be created via calling needed methods or by
calling closures. These are equivalent:
```kotlin
changelog - {
    changeset(1)
        .createTable(
            "table", 
            "id" to "number")
}
``` 
```kotlin
changelog
    .changeset
    .id(1)
    .createTable
    .tableName("table")
    .column
    .name("id")
    .type("number")
```
Therefore extensions like minus function with closure as argument
or createTable function that accepts table name and column pairs
are not necessary but simplifies script coding.

Almost all of default extensions are located in 
`src\main\kotlin\com\tsarev\liquikotlin\extensions\extensions.kt`.

> Note: Minus function is mplemented as 'core' function so it is 
> protected member function.

Also, there is one feature except standard xml like DSL.
At any place in the script default value can be set to 
any attribute (not children nodes) like this:
```kotlin
changelog.changeset.sql.splitStatements.setDefault(true)
changelog.changeset.sql.stripComments.setDefault(true)

changelog - {
    changeset(5).sql - "select * from dual"
}

```
or this:
```kotlin
changelog - {
    changeset.createTable.schemaName.setDefault("mySchema")
    
    changeset(1).createTable("table", "id" to "number")
}
```

When default value is set all node declarations will
have specified value set to default (value can be explicitly overridden)
within closure (or globally if there is no closure).

### Links
1. <a id="groovy_dsl_anchor"></a> https://github.com/liquibase/liquibase-groovy-dsl
2. <a id="kotlin_dsl_anchor"></a> https://github.com/gradle/kotlin-dsl

# TODO
0. Add tests
0. Add LICENSE.md
0. Add travis build
0. Add badges
