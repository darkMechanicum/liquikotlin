package com.tsarev.liquikotlin.printing

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.DslNode
import com.tsarev.liquikotlin.infrastructure.EvaluatableDslNode
import java.io.PrintStream
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

enum class PrinterMode {
    FULL,
    PRETTY,
    MINIMUM
}

typealias PrinterArg = Pair<PrinterMode, PrintStream>

open class Printer {

    private var currentLevel: Int = 0

    private val currentPrefix: String get() = "\t".repeat(currentLevel)

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
        -LkDropIndex::indexName,
        -LkCommonColumnConfig::name,
        -LkChangeSet::id,
        -LkSql::sql
    )

    fun initResult(thisNode: DslNode<*>, arg: PrinterArg?) {
        when (arg?.first ?: PrinterMode.PRETTY) {
            PrinterMode.FULL -> {
                println("$currentPrefix${thisNode::class.qualifiedName}")
                println("$currentPrefix${thisNode.parameters}")
            }
            PrinterMode.PRETTY -> {
                val identificationGetter = identificationRules[thisNode::class]
                val identification = identificationGetter?.let { it(thisNode) }
                println("$currentPrefix${thisNode::class.simpleName}${identification?.let { " - $it" } ?: ""}")
            }
            PrinterMode.MINIMUM -> {
                println("$currentPrefix${thisNode::class.simpleName}")
            }
        }
        currentLevel++
    }

    fun eval() {
        currentLevel--
    }

    fun <NodeT : DslNode<NodeT>> getAdapter() = PrinterAdapter<NodeT>()

    inner class PrinterAdapter<NodeT : DslNode<NodeT>> :
        EvaluatableDslNode.Evaluator<NodeT, Unit, PrinterArg>() {

        override fun initResult(thisNode: NodeT, argument: PrinterArg?) = this@Printer.initResult(thisNode, argument)

        override fun eval(
            childEvaluations: Collection<Any>,
            argument: PrinterArg?,
            thisNode: NodeT,
            parentEval: Any?,
            resultEval: Unit?
        ) = this@Printer.eval()

    }

}

open class PrintingEvaluatorFactory : EvaluatableDslNode.EvaluatorFactory<PrinterArg>() {

    private val printer: Printer = Printer()

    override fun <NodeT : DslNode<NodeT>, EvalT : Any> getEvaluatorFor(node: NodeT): EvaluatableDslNode.Evaluator<NodeT, EvalT, PrinterArg> =
        printer.getAdapter<NodeT>() as EvaluatableDslNode.Evaluator<NodeT, EvalT, PrinterArg>

}