package com.tsarev.liquikotlin.infrastructure.api

import com.tsarev.liquikotlin.util.LazyObservingDelegate
import com.tsarev.liquikotlin.util.letWhile
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

/**
 * Class to allow chaining in DSL.
 */
abstract class Self<out SelfT : Self<SelfT>>(
    private val selfClass: KClass<SelfT>
) {

    /**
     * Self link.
     */
    val self get() = selfClass.cast(this)

    /**
     * Closure support.
     */
    operator fun minus(modification: SelfT.() -> Unit): SelfT = self.apply(modification)
}

/**
 * Self that has support for children.
 */
abstract class ChildAbleSelf<out SelfT : ChildAbleSelf<SelfT>>(selfClass: KClass<SelfT>) :
    Self<SelfT>(selfClass) {

    /**
     * Child add listener.
     */
    internal abstract fun onChildAdded(child: Self<*>)

    /**
     * Child access listener.
     */
    internal abstract fun onChildAccessed(child: Self<*>)

}