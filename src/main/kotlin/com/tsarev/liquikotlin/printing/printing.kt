package com.tsarev.liquikotlin.printing

import com.tsarev.liquikotlin.bundled.*
import com.tsarev.liquikotlin.infrastructure.LbDslNode
import com.tsarev.liquikotlin.infrastructure.api.EvalAction
import com.tsarev.liquikotlin.infrastructure.api.EvalFactory
import com.tsarev.liquikotlin.infrastructure.default.DefaultNode
import com.tsarev.liquikotlin.infrastructure.evalSafe
import java.io.PrintStream
import kotlin.reflect.KClass

/**
 * Utility property to collect all primary props with static callback.
 */
val DefaultNode.primaryProps
    get() = this.metas
        .values
        .filter { it.annotations.any { Primary::class.isInstance(it) } }
        .map { it.name to this.properties[it.name] }
        .filter { it.second != null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = ", ") { "${it.first}:${it.second}" }
        ?: identificationRules[this::class]?.let { it(this) }

/**
 * Rules which field is more valuable for printing in pretty mode.
 */
val identificationRules: Map<KClass<*>, (Any) -> Any?> = mapOf(
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
    -LkSql::sql,
    -LkDbmsPrecondition::type,
    -LkRunningAsPrecondition::username,
    -LkChangeLogPropertyDefinedPrecondition::property,
    -LkChangeSetExecutedPrecondition::id,
    -LkChangeLog::logicalFilePath
)

const val indent = 3
const val emptySymbol = ""
const val linkingSymbol = '│'
const val branchingSymbol = '├'
const val lastChildBranchingSymbol = '└'
const val branchSymbol = '─'
const val spaceSymbol = ' '
const val beforeChild = "$linkingSymbol"
val identificationPattern: Pattern = { "[$it]" }
val commonPattern: Pattern = { "$linkingSymbol${"$spaceSymbol".repeat(indent - 1)}$it" }
val branchPattern: Pattern = { "$branchingSymbol${"$branchSymbol".repeat(indent - 2)}$spaceSymbol$it" }
val lastBranchPattern: Pattern = { "$lastChildBranchingSymbol${"$branchSymbol".repeat(indent - 2)}$spaceSymbol$it" }
val lastPattern: Pattern = { "${"$spaceSymbol".repeat(indent)}$it" }

/**
 * Applies pattern and trims tailing space symbols.
 */
infix fun Any?.withPattern(pattern: Pattern) = (this?.let(pattern) ?: emptySymbol).dropLastWhile { it == spaceSymbol }

/**
 * Various printing modes.
 */
enum class PrinterMode(
    val printingLogic: MutableList<String>.(DefaultNode, Boolean) -> Unit,
    val compact: Boolean = false
) {
    FULL({ node, noChildren ->
        val specialPrefix = beforeChild.takeIf { !noChildren } ?: emptySymbol
        this addStart "${node.selfClass.qualifiedName}"
        this addLine "$specialPrefix${node.properties}"
    }),
    PRETTY({ node, _ -> this addStart "${node.selfClass.simpleName}${node.primaryProps withPattern identificationPattern}" }, true),
    MINIMUM({ node, _ -> this addStart "${node.selfClass.simpleName}" }, true);
}

/**
 * Printer argument.
 */
typealias PrinterArg = Pair<PrinterMode, PrintStream>

/**
 * Function to print tree.
 */
fun LbDslNode<*>.print(mode: PrinterMode = PrinterMode.PRETTY, stream: PrintStream = System.out) {
    this.evalSafe<Any, PrinterArg>(Printer, mode to stream)
}

/**
 * Evaluator that prints the tree upon evaluating.
 * Can accept print mode and output stream.
 */
object Printer :
    EvalAction<DefaultNode, MutableList<String>, PrinterArg>,
    EvalFactory<PrinterArg, DefaultNode> {

    override fun <EvalT : Any> getAction(node: DefaultNode): EvalAction<DefaultNode, EvalT, PrinterArg> =
        Printer as EvalAction<DefaultNode, EvalT, PrinterArg>

    override fun doBefore(thisNode: DefaultNode, argument: PrinterArg?) = ArrayList<String>()

    override fun doAfter(
        argument: PrinterArg?,
        thisNode: DefaultNode,
        childEvaluations: Collection<Any?>,
        childNodes: Collection<DefaultNode>,
        parentEval: Any?,
        resultEval: MutableList<String>?
    ): MutableList<String> {
        resultEval!!
        val noChildren = childEvaluations.isEmpty()
        val chosenMode = (argument?.first ?: PrinterMode.PRETTY)
        val chosenPrinting = chosenMode.printingLogic
        resultEval.chosenPrinting(thisNode, noChildren)
        if (!chosenMode.compact) {
            resultEval addLine if (noChildren) emptySymbol else beforeChild
        }
        childEvaluations
            .safeCastMap<List<String>>()
            .forEachExceptLast {
                it.forFirst { resultEval addStart (it withPattern branchPattern) }
                    .forEach { resultEval addStart (it withPattern commonPattern) }
            }?.also {
                it.forFirst { resultEval addStart (it withPattern lastBranchPattern) }
                    .forEach { resultEval addStart (it withPattern lastPattern) }
            }
        if (parentEval == null) {
            argument?.second?.println()
            resultEval.forEach { argument?.second?.println(it) }
        }
        return resultEval
    }
}