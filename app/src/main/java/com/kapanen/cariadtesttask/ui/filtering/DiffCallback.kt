package com.kapanen.cariadtesttask.ui.filtering

import androidx.recyclerview.widget.DiffUtil
import com.kapanen.cariadtesttask.model.FilterHeader
import com.kapanen.cariadtesttask.model.FilterItem

class DiffCallback(
    private val oldItems: List<Any>,
    private val newItems: List<Any>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return areItemsContentTheSame(oldItem, newItem)
    }

    private fun areItemsContentTheSame(oldItem: Any, newItem: Any): Boolean {
        return compareFilterHeaderItem(oldItem, newItem) || compareFilterItem(oldItem, newItem)
    }

    private fun compareFilterHeaderItem(oldItem: Any, newItem: Any): Boolean {
        return oldItem is FilterHeader && newItem is FilterHeader
                && oldItem.title == newItem.title
    }

    private fun compareFilterItem(oldItem: Any, newItem: Any): Boolean {
        return oldItem is FilterItem && newItem is FilterItem
                && oldItem.label == newItem.label
                && oldItem.filterType == newItem.filterType
                && oldItem.isEnabled == newItem.isEnabled
    }

}
