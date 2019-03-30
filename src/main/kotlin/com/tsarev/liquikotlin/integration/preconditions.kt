package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.LbArg
import com.tsarev.liquikotlin.infrastructure.LbDslNode
import com.tsarev.liquikotlin.infrastructure.LiquibaseIntegrator
import liquibase.precondition.CustomPreconditionWrapper
import liquibase.precondition.Precondition
import liquibase.precondition.core.*

open class BasePreconditionIntegration<SelfT : LbDslNode<SelfT>, LinkedT : Precondition>(
    linkedConstructor: () -> LinkedT
) : LiquibaseIntegrator<SelfT, LinkedT, PreconditionContainer>(
    linkedConstructor,
    { container, precondition, _, _ -> container.addNestedPrecondition(precondition) }
)

abstract class SchAndCatIntegration<SelfT : PreconditionWithSchAndCat<SelfT>, LinkedT : Precondition>(
    linkedConstructor: () -> LinkedT,
    schemaSetter: (LinkedT, String?) -> Any,
    catalogSetter: (LinkedT, String?) -> Any
) : BasePreconditionIntegration<SelfT, LinkedT>(
    linkedConstructor
) { init {
    PreconditionWithSchAndCat<SelfT>::schemaName - schemaSetter
    PreconditionWithSchAndCat<SelfT>::catalogName - catalogSetter
}
}

// --- Bundled preconditions ---

open class PreconditionContainerIntegration<ParentT : Any>(
    parentSetter: (ParentT, PreconditionContainer, LkPrecondition, LbArg?) -> Unit
) : LiquibaseIntegrator<LkPrecondition, PreconditionContainer, ParentT>(
    ::PreconditionContainer,
    parentSetter
) { init {
    LkPrecondition::onFail - PreconditionContainer::setOnFail
    LkPrecondition::onError - PreconditionContainer::setOnError
//    LkPrecondition::onUpdateSQL -
//    TODO What is this?
    LkPrecondition::onFailMessage - PreconditionContainer::setOnFailMessage
    LkPrecondition::onErrorMessage - PreconditionContainer::setOnErrorMessage
}
}

open class DbmsPreconditionIntegration :
    BasePreconditionIntegration<LkDbmsPrecondition, DBMSPrecondition>(::DBMSPrecondition) { init {
    LkDbmsPrecondition::type - DBMSPrecondition::setType
}
}

open class RunningAsPreconditionIntegration :
    BasePreconditionIntegration<LkRunningAsPrecondition, RunningAsPrecondition>(::RunningAsPrecondition) { init {
    LkRunningAsPrecondition::username - RunningAsPrecondition::setUsername
}
}

open class ChangeSetExecutedPreconditionIntegration :
    BasePreconditionIntegration<LkChangeSetExecutedPrecondition, ChangeSetExecutedPrecondition>(::ChangeSetExecutedPrecondition) { init {
    LkChangeSetExecutedPrecondition::id - ChangeSetExecutedPrecondition::setId
    LkChangeSetExecutedPrecondition::author - ChangeSetExecutedPrecondition::setAuthor
    LkChangeSetExecutedPrecondition::changeLogFile - ChangeSetExecutedPrecondition::setChangeLogFile
}
}

open class ColumnExistsPreconditionIntegration :
    SchAndCatIntegration<LkColumnExistsPrecondition, ColumnExistsPrecondition>(
        ::ColumnExistsPrecondition,
        ColumnExistsPrecondition::setSchemaName,
        ColumnExistsPrecondition::setCatalogName
    ) { init {
    LkColumnExistsPrecondition::tableName - ColumnExistsPrecondition::setTableName
    LkColumnExistsPrecondition::columnName - ColumnExistsPrecondition::setColumnName
}
}

open class TableExistsPreconditionIntegration :
    SchAndCatIntegration<LkTableExistsPrecondition, TableExistsPrecondition>(
        ::TableExistsPrecondition,
        TableExistsPrecondition::setSchemaName,
        TableExistsPrecondition::setCatalogName
    ) { init {
    LkTableExistsPrecondition::tableName - TableExistsPrecondition::setTableName
}
}

open class ViewExistsPreconditionIntegration : SchAndCatIntegration<LkViewExistsPrecondition, ViewExistsPrecondition>(
    ::ViewExistsPrecondition,
    ViewExistsPrecondition::setSchemaName,
    ViewExistsPrecondition::setCatalogName
) { init {
    LkViewExistsPrecondition::viewName - ViewExistsPrecondition::setViewName
}
}

open class ForeignKeyConstraintExistsPreconditionIntegration :
    SchAndCatIntegration<LkForeignKeyConstraintExistsPrecondition, ForeignKeyExistsPrecondition>(
        ::ForeignKeyExistsPrecondition,
        ForeignKeyExistsPrecondition::setSchemaName,
        ForeignKeyExistsPrecondition::setCatalogName
    ) { init {
    LkForeignKeyConstraintExistsPrecondition::foreignKeyName - ForeignKeyExistsPrecondition::setForeignKeyName
    LkForeignKeyConstraintExistsPrecondition::foreignKeyTableName - ForeignKeyExistsPrecondition::setForeignKeyTableName
}
}

open class IndexExistsPreconditionIntegration :
    SchAndCatIntegration<LkIndexExistsPrecondition, IndexExistsPrecondition>(
        ::IndexExistsPrecondition,
        IndexExistsPrecondition::setSchemaName,
        IndexExistsPrecondition::setCatalogName
    ) { init {
    LkIndexExistsPrecondition::indexName - IndexExistsPrecondition::setIndexName
    LkIndexExistsPrecondition::tableName - IndexExistsPrecondition::setTableName
}
}

open class SequenceExistsPreconditionIntegration :
    SchAndCatIntegration<LkSequenceExistsPrecondition, SequenceExistsPrecondition>(
        ::SequenceExistsPrecondition,
        SequenceExistsPrecondition::setSchemaName,
        SequenceExistsPrecondition::setCatalogName
    ) { init {
    LkSequenceExistsPrecondition::sequenceName - SequenceExistsPrecondition::setSequenceName
}
}

open class PrimaryKeyExistsPreconditionIntegration :
    SchAndCatIntegration<LkPrimaryKeyExistsPrecondition, PrimaryKeyExistsPrecondition>(
        ::PrimaryKeyExistsPrecondition,
        PrimaryKeyExistsPrecondition::setSchemaName,
        PrimaryKeyExistsPrecondition::setCatalogName
    ) { init {
    LkPrimaryKeyExistsPrecondition::primaryKeyName - PrimaryKeyExistsPrecondition::setPrimaryKeyName
    LkPrimaryKeyExistsPrecondition::tableName - PrimaryKeyExistsPrecondition::setTableName
}
}

open class SqlCheckPreconditionIntegration :
    BasePreconditionIntegration<LkSqlCheckPrecondition, SqlPrecondition>(::SqlPrecondition) { init {
    LkSqlCheckPrecondition::expectedResult - SqlPrecondition::setExpectedResult
    LkSqlCheckPrecondition::sql - SqlPrecondition::setSql
}
}

open class ChangeLogPropertyDefinedPreconditionIntegration :
    BasePreconditionIntegration<LkChangeLogPropertyDefinedPrecondition, ChangeLogPropertyDefinedPrecondition>(::ChangeLogPropertyDefinedPrecondition) { init {
    LkChangeLogPropertyDefinedPrecondition::property - ChangeLogPropertyDefinedPrecondition::setProperty
    LkChangeLogPropertyDefinedPrecondition::value - ChangeLogPropertyDefinedPrecondition::setValue
}
}

open class CustomPreconditionIntegration :
    BasePreconditionIntegration<LkCustomPrecondition, CustomPreconditionWrapper>(::CustomPreconditionWrapper) { init {
    LkCustomPrecondition::className - CustomPreconditionWrapper::setClassName
}
}

open class CustomPreconditionParamIntegration :
    LiquibaseIntegrator<LkCustomPreconditionParam, Any, CustomPreconditionWrapper>(
        ::Any,
        { wrapper, _, self, _ ->
            wrapper.setParam(self.name.current, self.value.current?.toString())
        }
    )
