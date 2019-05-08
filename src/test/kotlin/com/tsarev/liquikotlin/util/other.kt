package com.tsarev.liquikotlin.util

import com.tsarev.liquikotlin.infrastructure.EvaluatableDslNode
import org.junit.runners.model.Statement

fun <ArgT> EvaluatableDslNode<*>.generalEval(factory: EvaluatableDslNode.EvaluatorFactory<ArgT>, arg: ArgT? = null) =
    this.eval<Any, ArgT>(factory, arg)

fun <T> T.asStatement(): Statement where T : () -> Unit = object : Statement() {
    override fun evaluate() = this@asStatement.invoke()
}