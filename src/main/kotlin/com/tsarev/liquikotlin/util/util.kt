package com.tsarev.liquikotlin.util

/**
 * Generate two way map from passed pairs.
 */
fun <KeyT> twoWayMap(vararg pairs: Pair<KeyT, KeyT>): Map<KeyT, KeyT> =
    HashMap<KeyT, KeyT>().apply {
        pairs.forEach { this[it.first] = it.second }
        pairs.forEach { this[it.second] = it.first }
    }

/**
 * Transform this value until transformer returns new not null values.
 */
fun <T> T.letWhile(transform: (T) -> T?): T {
    var current = this
    var next = transform(current)
    while (next != null) {
        current = next
        next = transform(current)
    }
    return current
}