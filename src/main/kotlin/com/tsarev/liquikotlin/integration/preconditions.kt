package com.tsarev.liquikotlin.integration

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.*
import com.tsarev.liquikotlin.infrastructure.default.DefaultNode
import liquibase.precondition.CustomPreconditionWrapper
import liquibase.precondition.Precondition
import liquibase.precondition.PreconditionLogic
import liquibase.precondition.core.*

open class BasePreconditionIntegration<LinkedT : Precondition>(
    linkedConstructor: () -> LinkedT,
    linkedClass: Class<LinkedT>,
    vararg childMappings: PropertyMapping<DefaultNode, LinkedT, *>
) : LiquibaseIntegrator<LinkedT, PreconditionLogic>(
    linkedConstructor,
    linkedClass,
    { container, precondition, _, _ -> container.addNestedPrecondition(precondition) }
) { init {
    propertyMappings.addAll(childMappings)
}
}

abstract class SchAndCatIntegration<SelfT : PreconditionWithSchAndCat<SelfT>, LinkedT : Precondition>(
    linkedConstructor: () -> LinkedT,
    linkedClass: Class<LinkedT>,
    schemaSetter: (LinkedT, String?) -> Any,
    catalogSetter: (LinkedT, String?) -> Any,
    vararg childMappings: PropertyMapping<DefaultNode, LinkedT, *>
) : BasePreconditionIntegration<LinkedT>(
    linkedConstructor,
    linkedClass,
    PreconditionWithSchAndCat<SelfT>::schemaName - schemaSetter,
    PreconditionWithSchAndCat<SelfT>::catalogName - catalogSetter
) { init {
    propertyMappings.addAll(childMappings)
}
}

// --- Bundled preconditions ---

open class PreconditionContainerIntegration<ParentT : Any>(
    parentSetter: (ParentT, PreconditionContainer?, DefaultNode, LbArg?) -> Unit
) : LiquibaseIntegrator<PreconditionContainer, ParentT>(
	::PreconditionContainer,
	PreconditionContainer::class.java,
    parentSetter,
    LkPrecondition::onFail - PreconditionContainer::setOnFail,
    LkPrecondition::onError - PreconditionContainer::setOnError,
    LkPrecondition::onFailMessage - PreconditionContainer::setOnFailMessage,
    LkPrecondition::onErrorMessage - PreconditionContainer::setOnErrorMessage,
    LkPrecondition::onSqlOutput - PreconditionContainer::setOnSqlOutput
)

open class AndPreconditionIntegration : BasePreconditionIntegration<AndPrecondition>(
    ::AndPrecondition,
    AndPrecondition::class.java
)

open class OrPreconditionIntegration : BasePreconditionIntegration<OrPrecondition>(
    ::OrPrecondition,
    OrPrecondition::class.java
)

open class DbmsPreconditionIntegration : BasePreconditionIntegration<DBMSPrecondition>(
	::DBMSPrecondition,
	DBMSPrecondition::class.java,
    LkDbmsPrecondition::type - DBMSPrecondition::setType
)

open class RunningAsPreconditionIntegration :
    BasePreconditionIntegration<RunningAsPrecondition>(
	::RunningAsPrecondition,
	RunningAsPrecondition::class.java,
        LkRunningAsPrecondition::username - RunningAsPrecondition::setUsername
    )

open class ChangeSetExecutedPreconditionIntegration :
    BasePreconditionIntegration<ChangeSetExecutedPrecondition>(
	::ChangeSetExecutedPrecondition,
	ChangeSetExecutedPrecondition::class.java,
        LkChangeSetExecutedPrecondition::id - ChangeSetExecutedPrecondition::setId,
        LkChangeSetExecutedPrecondition::author - ChangeSetExecutedPrecondition::setAuthor,
        LkChangeSetExecutedPrecondition::changeLogFile - ChangeSetExecutedPrecondition::setChangeLogFile
    )

open class ColumnExistsPreconditionIntegration :
    SchAndCatIntegration<LkColumnExistsPrecondition, ColumnExistsPrecondition>(
	::ColumnExistsPrecondition,
	ColumnExistsPrecondition::class.java,
        ColumnExistsPrecondition::setSchemaName,
        ColumnExistsPrecondition::setCatalogName,
        LkColumnExistsPrecondition::tableName - ColumnExistsPrecondition::setTableName,
        LkColumnExistsPrecondition::columnName - ColumnExistsPrecondition::setColumnName
    )

open class TableExistsPreconditionIntegration :
    SchAndCatIntegration<LkTableExistsPrecondition, TableExistsPrecondition>(
	::TableExistsPrecondition,
	TableExistsPrecondition::class.java,
        TableExistsPrecondition::setSchemaName,
        TableExistsPrecondition::setCatalogName,
        LkTableExistsPrecondition::tableName - TableExistsPrecondition::setTableName
    )

open class ViewExistsPreconditionIntegration : SchAndCatIntegration<LkViewExistsPrecondition, ViewExistsPrecondition>(
	::ViewExistsPrecondition,
	ViewExistsPrecondition::class.java,
    ViewExistsPrecondition::setSchemaName,
    ViewExistsPrecondition::setCatalogName
    ,
    LkViewExistsPrecondition::viewName - ViewExistsPrecondition::setViewName
)

open class ForeignKeyConstraintExistsPreconditionIntegration :
    SchAndCatIntegration<LkForeignKeyConstraintExistsPrecondition, ForeignKeyExistsPrecondition>(
	::ForeignKeyExistsPrecondition,
	ForeignKeyExistsPrecondition::class.java,
        ForeignKeyExistsPrecondition::setSchemaName,
        ForeignKeyExistsPrecondition::setCatalogName,
        LkForeignKeyConstraintExistsPrecondition::foreignKeyName - ForeignKeyExistsPrecondition::setForeignKeyName,
        LkForeignKeyConstraintExistsPrecondition::foreignKeyTableName - ForeignKeyExistsPrecondition::setForeignKeyTableName
    )

open class IndexExistsPreconditionIntegration :
    SchAndCatIntegration<LkIndexExistsPrecondition, IndexExistsPrecondition>(
	::IndexExistsPrecondition,
	IndexExistsPrecondition::class.java,
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
	SequenceExistsPrecondition::class.java,
        SequenceExistsPrecondition::setSchemaName,
        SequenceExistsPrecondition::setCatalogName
        ,
        LkSequenceExistsPrecondition::sequenceName - SequenceExistsPrecondition::setSequenceName
    )

open class PrimaryKeyExistsPreconditionIntegration :
    SchAndCatIntegration<LkPrimaryKeyExistsPrecondition, PrimaryKeyExistsPrecondition>(
	::PrimaryKeyExistsPrecondition,
	PrimaryKeyExistsPrecondition::class.java,
        PrimaryKeyExistsPrecondition::setSchemaName,
        PrimaryKeyExistsPrecondition::setCatalogName
        ,
        LkPrimaryKeyExistsPrecondition::primaryKeyName notNull PrimaryKeyExistsPrecondition::setPrimaryKeyName,
        LkPrimaryKeyExistsPrecondition::tableName - PrimaryKeyExistsPrecondition::setTableName
    )

open class SqlCheckPreconditionIntegration : BasePreconditionIntegration<SqlPrecondition>(
	::SqlPrecondition,
	SqlPrecondition::class.java,
    LkSqlCheckPrecondition::expectedResult - SqlPrecondition::setExpectedResult,
    LkSqlCheckPrecondition::sql - SqlPrecondition::setSql
)

open class ChangeLogPropertyDefinedPreconditionIntegration :
    BasePreconditionIntegration<ChangeLogPropertyDefinedPrecondition>(
	::ChangeLogPropertyDefinedPrecondition,
	ChangeLogPropertyDefinedPrecondition::class.java,
        LkChangeLogPropertyDefinedPrecondition::property - ChangeLogPropertyDefinedPrecondition::setProperty,
        LkChangeLogPropertyDefinedPrecondition::value - ChangeLogPropertyDefinedPrecondition::setValue
    )

open class CustomPreconditionIntegration : BasePreconditionIntegration<CustomPreconditionWrapper>(
	::CustomPreconditionWrapper,
	CustomPreconditionWrapper::class.java,
    LkCustomPrecondition::className - CustomPreconditionWrapper::setClassName
)

open class CustomPreconditionParamIntegration :
    LiquibaseIntegrator<Any, CustomPreconditionWrapper>(
	::Any,
	Any::class.java,
        { wrapper, _, self, _ ->
            wrapper.setParam(self.get(LkCustomPreconditionParam::name), self.getNullable(LkCustomPreconditionParam::value)?.toString())
        }
    )
