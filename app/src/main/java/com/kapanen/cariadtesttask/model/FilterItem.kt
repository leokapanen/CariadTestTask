package com.kapanen.cariadtesttask.model

data class FilterItem(
    val label: String,
    val value: String? = null,
    val filterType: FilterType,
    val isEnabled: Boolean = false
)
