package com.kapanen.cariadtesttask.data

import androidx.lifecycle.LiveData
import com.kapanen.cariadtesttask.model.Poi

interface PoiRepository {

    fun observePois(): LiveData<List<Poi>>

    fun observeError(): LiveData<Throwable>

    fun enableLiveUpdate()

    fun disableLiveUpdate()

}
