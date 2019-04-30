package com.tsarev.liquikotlin.util

import com.tsarev.liquikotlin.infrastructure.EvaluatableDslNode

fun <ArgT> EvaluatableDslNode<*>.generalEval(factory: EvaluatableDslNode.EvaluatorFactory<ArgT>, arg: ArgT? = null) =
    this.eval<Any, ArgT>(factory, arg)