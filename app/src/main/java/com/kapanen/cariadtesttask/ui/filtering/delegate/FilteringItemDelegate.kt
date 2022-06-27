package com.kapanen.cariadtesttask.ui.filtering.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapanen.cariadtesttask.R
import com.kapanen.cariadtesttask.databinding.FilteringItemBinding
import com.kapanen.cariadtesttask.delegate.SimpleDelegate
import com.kapanen.cariadtesttask.model.FilterItem
import com.kapanen.cariadtesttask.model.FilterType
import com.kapanen.cariadtesttask.setting.AppSettings
import com.kapanen.cariadtesttask.util.toItemsString
import com.kapanen.cariadtesttask.util.toStringSet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FilteringItemDelegate @Inject constructor(
    private val appSettings: AppSettings,
    private val coroutineDispatcher: CoroutineDispatcher
) :
    SimpleDelegate<FilterItem, FilteringItemDelegate.ViewHolder>(R.layout.filtering_item),
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = coroutineDispatcher

    override fun suitFor(position: Int, data: Any) = data is FilterItem

    override fun onCreateViewHolder(parent: ViewGroup) = ViewHolder(
        FilteringItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, data: FilterItem) {
        holder.setIsRecyclable(false)
        holder.binding.filteringItemLabel.text = data.label
        holder.binding.filteringItemSwitch.isChecked = data.isEnabled
        holder.binding.filteringItemSwitch.setOnCheckedChangeListener { _, isChecked ->
            launch(coroutineDispatcher) {
                if (isChecked) {
                    enableFilterItem(data)
                } else {
                    disableFilterItem(data)
                }
                appSettings.notifyFilteringUpdate()
            }
        }
    }

    private fun enableFilterItem(filterItem: FilterItem) {
        when (filterItem.filterType) {
            FilterType.ACTIVE -> {
                appSettings.isFilterActiveEnabled = true
            }
            FilterType.INACTIVE -> {
                appSettings.isFilterInactiveEnabled = true
            }
            FilterType.CONNECTION_TYPE -> {
                appSettings.connectionTypeFilters =
                    addItemToString(appSettings.connectionTypeFilters, filterItem.value.orEmpty())
            }
        }
    }

    private fun disableFilterItem(filterItem: FilterItem) {
        when (filterItem.filterType) {
            FilterType.ACTIVE -> {
                appSettings.isFilterActiveEnabled = false
            }
            FilterType.INACTIVE -> {
                appSettings.isFilterInactiveEnabled = false
            }
            FilterType.CONNECTION_TYPE -> {
                appSettings.connectionTypeFilters =
                    removeItemFromString(
                        appSettings.connectionTypeFilters,
                        filterItem.value.orEmpty()
                    )
            }
        }
    }

    private fun addItemToString(filterStr: String, filterValue: String): String {
        val filterSet = filterStr.toStringSet().toMutableSet()
        filterSet.add(filterValue)
        return filterSet.filter { it.isNotBlank() }.toItemsString()
    }

    private fun removeItemFromString(filterStr: String, filterValue: String): String {
        val filterSet = filterStr.toStringSet().toMutableSet()
        filterSet.remove(filterValue)
        return filterSet.filter { it.isNotBlank() }.toItemsString()
    }

    class ViewHolder(val binding: FilteringItemBinding) : RecyclerView.ViewHolder(binding.root)

}
