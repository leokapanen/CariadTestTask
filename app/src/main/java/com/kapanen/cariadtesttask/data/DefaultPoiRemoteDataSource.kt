package com.kapanen.cariadtesttask.data

import com.kapanen.cariadtesttask.BuildConfig
import com.kapanen.cariadtesttask.api.PoiApiService
import com.kapanen.cariadtesttask.model.Poi
import timber.log.Timber

class DefaultPoiRemoteDataSource(
    private val poiApiService: PoiApiService
) : PoiRemoteDataSource {

    override suspend fun poiList(): Result<List<Poi>> {
        return try {
            val result = poiApiService.pois(BuildConfig.API_KEY)
            if (result.isSuccessful) {
                Result.success(result.body() ?: emptyList())
            } else {
                Result.failure(
                    Exception(
                        result.errorBody()?.toString() ?: "Couldn't load POIs"
                    )
                )
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            Result.failure(
                Exception("Couldn't load POIs")
            )
        }
    }

}
