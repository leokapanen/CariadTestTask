package com.kapanen.cariadtesttask.ui.filtering

import androidx.recyclerview.widget.DiffUtil
import com.kapanen.cariadtesttask.delegate.AdapterDelegatesManager
import com.kapanen.cariadtesttask.delegate.DelegateAdapter

class FiltersListAdapter(delegatesManager: AdapterDelegatesManager) :
    DelegateAdapter<Any>(delegatesManager) {

    private var items: List<Any> = emptyList()

    fun setItems(items: List<Any>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(this.items, items))
        this.items = items
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItem(position: Int) = items[position]

    override fun getItemCount() = items.size

}
