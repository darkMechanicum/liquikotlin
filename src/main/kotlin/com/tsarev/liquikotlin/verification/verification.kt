package com.tsarev.liquikotlin.verification

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.DslNode
import com.tsarev.liquikotlin.infrastructure.EvaluatableDslNode
import com.tsarev.liquikotlin.util.twoWayMap
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1


typealias AnalyzeKey = Pair<KClass<*>, Any>

data class OutdatedReport(val outdatedNodes: MutableSet<AnalyzeKey> = HashSet())

open class OutdatedVerificator {

    private inline operator fun <reified NodeT : Any, FieldT> KProperty1<NodeT, DslNode.Valuable<FieldT>>.unaryMinus()
            : Pair<KClass<NodeT>, (Any) -> Any?> = NodeT::class to { node -> this.get(node as NodeT).current }

    private val identificationRules: Map<KClass<*>, (Any) -> Any?> = mapOf(
        -LkCreateView::viewName,
        -LkDropView::viewName,
        -LkCreateTable::tableName,
        -LkDropTable::tableName,
        -LkCreateSequence::sequenceName,
        -LkDropSequence::sequenceName,
        -LkCreateProcedure::procedureName,
        -LkDropProcedure::procedureName,
        -LkCreateIndex::indexName,
        -LkDropIndex::indexName
    )

    private val complianceRules: Map<KClass<*>, KClass<*>> = twoWayMap(
        LkCreateView::class to LkDropView::class,
        LkCreateTable::class to LkDropTable::class,
        LkCreateSequence::class to LkDropSequence::class,
        LkCreateProcedure::class to LkDropProcedure::class,
        LkCreateIndex::class to LkDropIndex::class
    )

    private val analyzedNodesKeys: MutableSet<AnalyzeKey> = HashSet()

    private val report: OutdatedReport = OutdatedReport()

    fun initResult() = OutdatedReport()

    fun eval(thisNode: Any): OutdatedReport {
        val identificationGetter = identificationRules[thisNode::class]
        val identification = identificationGetter?.let { it(thisNode) ?: return report } ?: return report
        val complianceClass = complianceRules[thisNode::class]
        val complianceKey: AnalyzeKey? = complianceClass?.let { it to identification }
        val newKey: AnalyzeKey = thisNode::class to identification
        if (complianceKey != null && analyzedNodesKeys.contains(complianceKey)) {
            report.outdatedNodes.add(complianceKey)
            analyzedNodesKeys.remove(complianceKey)
        }
        analyzedNodesKeys.add(newKey)
        return report
    }

    fun <NodeT : DslNode<NodeT>> getAdapter() = OutdatedVerificatorAdapter<NodeT>()

    inner class OutdatedVerificatorAdapter<NodeT : DslNode<NodeT>> :
        EvaluatableDslNode.Evaluator<NodeT, OutdatedReport, Any>() {

        override fun initResult(thisNode: NodeT, argument: Any?) = this@OutdatedVerificator.initResult()

        override fun eval(
            childEvaluations: Collection<Any?>,
            argument: Any?,
            thisNode: NodeT,
            parentEval: Any?,
            resultEval: OutdatedReport?
        ): OutdatedReport = this@OutdatedVerificator.eval(thisNode)

    }

}

open class VerificationEvaluatorFactory : EvaluatableDslNode.EvaluatorFactory<Any>() {

    private val verificator: OutdatedVerificator = OutdatedVerificator()

    override fun <NodeT : DslNode<NodeT>, EvalT : Any> getEvaluatorFor(node: NodeT): EvaluatableDslNode.Evaluator<NodeT, EvalT, Any> =
        verificator.getAdapter<NodeT>() as EvaluatableDslNode.Evaluator<NodeT, EvalT, Any>

}