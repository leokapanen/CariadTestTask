package com.kapanen.cariadtesttask.data

import com.kapanen.cariadtesttask.model.Poi

interface PoiRemoteDataSource {

    suspend fun poiList(): Result<List<Poi>>

}
