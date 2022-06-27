package com.kapanen.cariadtesttask.util

private const val ITEM_DELIMITER = "|"

/**
 * Using for updating base url to avoid issue
 * `Fatal Exception: java.lang.IllegalArgumentExceptionbaseUrl must end in /`
 */
fun String.withTrailingSlash() = if (this.endsWith("/")) this else "$this/"


fun List<String>.toItemsString(): String = this.joinToString(separator = ITEM_DELIMITER)

fun String.toStringList(): List<String> {
    return if (!this.isNullOrBlank()) {
        this.split(ITEM_DELIMITER)
    } else {
        emptyList()
    }
}

fun String.toStringSet(): Set<String> {
    return if (!this.isNullOrBlank()) {
        this.split(ITEM_DELIMITER).toSet()
    } else {
        emptySet()
    }
}

fun Set<String>.toItemsString(): String = this.joinToString(separator = ITEM_DELIMITER)
