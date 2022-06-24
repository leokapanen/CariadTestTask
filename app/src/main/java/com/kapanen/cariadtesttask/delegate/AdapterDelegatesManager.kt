package com.kapanen.cariadtesttask.delegate

import androidx.recyclerview.widget.RecyclerView
import com.kapanen.hearthstoneassessment.delegate.DefaultDelegatesManager
import com.kapanen.hearthstoneassessment.delegate.RecyclerViewAdapterDelegate

interface AdapterDelegatesManager {

    fun getDelegateFor(viewType: Int): RecyclerViewAdapterDelegate<Any, RecyclerView.ViewHolder>

    fun getViewTypeFor(position: Int, data: Any): Int

    fun addDelegate(delegate: RecyclerViewAdapterDelegate<Any, RecyclerView.ViewHolder>): DefaultDelegatesManager

}
