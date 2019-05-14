package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.*
import liquibase.precondition.CustomPreconditionWrapper
import liquibase.precondition.Precondition
import liquibase.precondition.PreconditionLogic
import liquibase.precondition.core.*

open class BasePreconditionIntegration<SelfT : LbDslNode<SelfT>, LinkedT : Precondition>(
    linkedConstructor: () -> LinkedT,
    vararg childMappings: PropertyMapping<SelfT, LinkedT, *>
) : LiquibaseIntegrator<SelfT, LinkedT, PreconditionLogic>(
    linkedConstructor,
    { container, precondition, _, _ -> container.addNestedPrecondition(precondition) }
) { init {
    propertyMappings.addAll(childMappings)
}
}

abstract class SchAndCatIntegration<SelfT : PreconditionWithSchAndCat<SelfT>, LinkedT : Precondition>(
    linkedConstructor: () -> LinkedT,
    schemaSetter: (LinkedT, String?) -> Any,
    catalogSetter: (LinkedT, String?) -> Any,
    vararg childMappings: PropertyMapping<SelfT, LinkedT, *>
) : BasePreconditionIntegration<SelfT, LinkedT>(
    linkedConstructor,
    PreconditionWithSchAndCat<SelfT>::schemaName - schemaSetter,
    PreconditionWithSchAndCat<SelfT>::catalogName - catalogSetter
) { init {
    propertyMappings.addAll(childMappings)
}
}

// --- Bundled preconditions ---

open class PreconditionContainerIntegration<ParentT : Any>(
    parentSetter: (ParentT, PreconditionContainer?, LkPrecondition, LbArg?) -> Unit
) : LiquibaseIntegrator<LkPrecondition, PreconditionContainer, ParentT>(
    ::PreconditionContainer,
    parentSetter,
    LkPrecondition::onFail - PreconditionContainer::setOnFail,
    LkPrecondition::onError - PreconditionContainer::setOnError,
    LkPrecondition::onFailMessage - PreconditionContainer::setOnFailMessage,
    LkPrecondition::onErrorMessage - PreconditionContainer::setOnErrorMessage,
    LkPrecondition::onSqlOutput - PreconditionContainer::setOnSqlOutput
)

open class AndPreconditionIntegration : BasePreconditionIntegration<LkAndPrecondition, AndPrecondition>(::AndPrecondition)

open class OrPreconditionIntegration : BasePreconditionIntegration<LkOrPrecondition, OrPrecondition>(::OrPrecondition)

open class DbmsPreconditionIntegration : BasePreconditionIntegration<LkDbmsPrecondition, DBMSPrecondition>(
    ::DBMSPrecondition,
    LkDbmsPrecondition::type - DBMSPrecondition::setType
)

open class RunningAsPreconditionIntegration :
    BasePreconditionIntegration<LkRunningAsPrecondition, RunningAsPrecondition>(
        ::RunningAsPrecondition,
        LkRunningAsPrecondition::username - RunningAsPrecondition::setUsername
    )

open class ChangeSetExecutedPreconditionIntegration :
    BasePreconditionIntegration<LkChangeSetExecutedPrecondition, ChangeSetExecutedPrecondition>(
        ::ChangeSetExecutedPrecondition,
        LkChangeSetExecutedPrecondition::id - ChangeSetExecutedPrecondition::setId,
        LkChangeSetExecutedPrecondition::author - ChangeSetExecutedPrecondition::setAuthor,
        LkChangeSetExecutedPrecondition::changeLogFile - ChangeSetExecutedPrecondition::setChangeLogFile
    )

open class ColumnExistsPreconditionIntegration :
    SchAndCatIntegration<LkColumnExistsPrecondition, ColumnExistsPrecondition>(
        ::ColumnExistsPrecondition,
        ColumnExistsPrecondition::setSchemaName,
        ColumnExistsPrecondition::setCatalogName,
        LkColumnExistsPrecondition::tableName - ColumnExistsPrecondition::setTableName,
        LkColumnExistsPrecondition::columnName - ColumnExistsPrecondition::setColumnName
    )

open class TableExistsPreconditionIntegration :
    SchAndCatIntegration<LkTableExistsPrecondition, TableExistsPrecondition>(
        ::TableExistsPrecondition,
        TableExistsPrecondition::setSchemaName,
        TableExistsPrecondition::setCatalogName,
        LkTableExistsPrecondition::tableName - TableExistsPrecondition::setTableName
    )

open class ViewExistsPreconditionIntegration : SchAndCatIntegration<LkViewExistsPrecondition, ViewExistsPrecondition>(
    ::ViewExistsPrecondition,
    ViewExistsPrecondition::setSchemaName,
    ViewExistsPrecondition::setCatalogName
    ,
    LkViewExistsPrecondition::viewName - ViewExistsPrecondition::setViewName
)

open class ForeignKeyConstraintExistsPreconditionIntegration :
    SchAndCatIntegration<LkForeignKeyConstraintExistsPrecondition, ForeignKeyExistsPrecondition>(
        ::ForeignKeyExistsPrecondition,
        ForeignKeyExistsPrecondition::setSchemaName,
        ForeignKeyExistsPrecondition::setCatalogName,
        LkForeignKeyConstraintExistsPrecondition::foreignKeyName - ForeignKeyExistsPrecondition::setForeignKeyName,
        LkForeignKeyConstraintExistsPrecondition::foreignKeyTableName - ForeignKeyExistsPrecondition::setForeignKeyTableName
    )

open class IndexExistsPreconditionIntegration :
    SchAndCatIntegration<LkIndexExistsPrecondition, IndexExistsPrecondition>(
        ::IndexExistsPrecondition,
        IndexExistsPrecondition::setSchemaName,
        IndexExistsPrecondition::setCatalogName
        ,
        LkIndexExistsPrecondition::indexName - IndexExistsPrecondition::setIndexName,
        LkIndexExistsPrecondition::tableName - IndexExistsPrecondition::setTableName,
        LkIndexExistsPrecondition::columnNames - IndexExistsPrecondition::setColumnNames
    )

open class SequenceExistsPreconditionIntegration :
    SchAndCatIntegration<LkSequenceExistsPrecondition, SequenceExistsPrecondition>(
        ::SequenceExistsPrecondition,
        SequenceExistsPrecondition::setSchemaName,
        SequenceExistsPrecondition::setCatalogName
        ,
        LkSequenceExistsPrecondition::sequenceName - SequenceExistsPrecondition::setSequenceName
    )

open class PrimaryKeyExistsPreconditionIntegration :
    SchAndCatIntegration<LkPrimaryKeyExistsPrecondition, PrimaryKeyExistsPrecondition>(
        ::PrimaryKeyExistsPrecondition,
        PrimaryKeyExistsPrecondition::setSchemaName,
        PrimaryKeyExistsPrecondition::setCatalogName
        ,
        LkPrimaryKeyExistsPrecondition::primaryKeyName - PrimaryKeyExistsPrecondition::setPrimaryKeyName,
        LkPrimaryKeyExistsPrecondition::tableName - PrimaryKeyExistsPrecondition::setTableName
    )

open class SqlCheckPreconditionIntegration : BasePreconditionIntegration<LkSqlCheckPrecondition, SqlPrecondition>(
    ::SqlPrecondition,
    LkSqlCheckPrecondition::expectedResult - SqlPrecondition::setExpectedResult,
    LkSqlCheckPrecondition::sql - SqlPrecondition::setSql
)

open class ChangeLogPropertyDefinedPreconditionIntegration :
    BasePreconditionIntegration<LkChangeLogPropertyDefinedPrecondition, ChangeLogPropertyDefinedPrecondition>(
        ::ChangeLogPropertyDefinedPrecondition,
        LkChangeLogPropertyDefinedPrecondition::property - ChangeLogPropertyDefinedPrecondition::setProperty,
        LkChangeLogPropertyDefinedPrecondition::value - ChangeLogPropertyDefinedPrecondition::setValue
    )

open class CustomPreconditionIntegration : BasePreconditionIntegration<LkCustomPrecondition, CustomPreconditionWrapper>(
    ::CustomPreconditionWrapper,
    LkCustomPrecondition::className - CustomPreconditionWrapper::setClassName
)

open class CustomPreconditionParamIntegration :
    LiquibaseIntegrator<LkCustomPreconditionParam, Any, CustomPreconditionWrapper>(
        ::Any,
        { wrapper, _, self, _ ->
            wrapper.setParam(self.name.current, self.value.current?.toString())
        }
    )
