package com.kapanen.cariadtesttask.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

private const val DEFAULT_IS_ACTIVE_FILTER_ENABLED = true
private const val DEFAULT_IS_INACTIVE_FILTER_ENABLED = true

open class AppSettings constructor(storage: Storage) : StoragePropertyDelegate(storage) {

    private val _filteringUpdates = MutableLiveData<Long>()
    val filteringUpdates: LiveData<Long> = _filteringUpdates

    var isFilterActiveEnabled: Boolean by default(DEFAULT_IS_ACTIVE_FILTER_ENABLED)
    var isFilterInactiveEnabled: Boolean by default(DEFAULT_IS_INACTIVE_FILTER_ENABLED)

    var connectionTypes: String by default("")
    var connectionTypeFilters: String by default("")

    fun notifyFilteringUpdate() {
        _filteringUpdates.postValue(System.currentTimeMillis())
    }

}
