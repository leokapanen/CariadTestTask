package com.kapanen.cariadtesttask.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.kapanen.cariadtesttask.data.PoiRepository
import com.kapanen.cariadtesttask.model.Poi
import com.kapanen.cariadtesttask.model.isActive
import com.kapanen.cariadtesttask.setting.AppSettings
import com.kapanen.cariadtesttask.util.toItemsString
import com.kapanen.cariadtesttask.util.toStringSet
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val poiRepository: PoiRepository,
    private val appSettings: AppSettings
) : ViewModel() {

    fun observePois(): LiveData<List<Poi>> = poiRepository.observePois().map { pois ->
        if (appSettings.connectionTypes.isBlank()) {
            val connectionTypesSet = mutableSetOf<String>()
            pois.forEach { poi ->
                poi.connections.forEach { connection ->
                    connectionTypesSet.add(connection.connectionType.title)
                }
            }
            val connectionTypesStr = connectionTypesSet.toItemsString()
            appSettings.connectionTypes = connectionTypesStr
            appSettings.connectionTypeFilters = connectionTypesStr
        }

        val filters = appSettings.connectionTypeFilters.toStringSet()
        pois.filter { poi ->
            val isAvailable = poi.isActive()
            val showAvailableItem = appSettings.isFilterActiveEnabled && isAvailable
            val showInavailableItem = appSettings.isFilterInactiveEnabled && !isAvailable
            poi.connections.firstOrNull { filters.contains(it.connectionType.title) } != null
                    && (showAvailableItem || showInavailableItem)

        }.toList()
    }

    fun observeError(): LiveData<Throwable> = poiRepository.observeError()

    fun enableLiveUpdate() {
        poiRepository.enableLiveUpdate()
    }

    fun disableLiveUpdate() {
        poiRepository.disableLiveUpdate()
    }

}
