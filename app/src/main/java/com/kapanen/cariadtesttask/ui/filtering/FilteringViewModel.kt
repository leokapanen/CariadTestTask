package com.kapanen.cariadtesttask.ui.filtering

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapanen.cariadtesttask.R
import com.kapanen.cariadtesttask.model.FilterHeader
import com.kapanen.cariadtesttask.model.FilterItem
import com.kapanen.cariadtesttask.model.FilterType
import com.kapanen.cariadtesttask.setting.AppSettings
import com.kapanen.cariadtesttask.util.toStringList
import com.kapanen.cariadtesttask.util.toStringSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilteringViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val appSettings: AppSettings
) : ViewModel() {

    private val _items = MutableLiveData<List<Any>>().apply {
        value = emptyList<List<Any>>()
    }
    val items: LiveData<List<Any>> = _items

    fun prepareItems(resources: Resources) {
        viewModelScope.launch(dispatcher) {
            _items.postValue(mutableListOf<Any>().apply {
                add(FilterHeader(resources.getString(R.string.filter_header_active)))
                add(
                    FilterItem(
                        label = resources.getString(R.string.filter_active),
                        filterType = FilterType.ACTIVE,
                        isEnabled = appSettings.isFilterActiveEnabled
                    )
                )
                add(
                    FilterItem(
                        label = resources.getString(R.string.filter_inactive),
                        filterType = FilterType.INACTIVE,
                        isEnabled = appSettings.isFilterInactiveEnabled
                    )
                )

                val connectionTypeFilterSet = appSettings.connectionTypeFilters.toStringSet()
                val connectionTypeList = appSettings.connectionTypes.toStringList()
                if (connectionTypeList.isNotEmpty()) {
                    add(FilterHeader(resources.getString(R.string.filter_header_connection_type)))
                    connectionTypeList.forEach { filter ->
                        add(
                            FilterItem(
                                label = filter,
                                value = filter,
                                filterType = FilterType.CONNECTION_TYPE,
                                isEnabled = connectionTypeFilterSet.contains(filter)
                            )
                        )
                    }
                }
            }.toList())
        }
    }

    fun onPause() {
        appSettings.notifyFilteringUpdate()
    }

}
