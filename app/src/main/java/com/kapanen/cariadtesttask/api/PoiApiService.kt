package com.kapanen.cariadtesttask.api

import com.kapanen.cariadtesttask.model.Poi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PoiApiService {

    @GET("poi")
    suspend fun pois(@Query("key") apiKey: String): Response<List<Poi>>

}
