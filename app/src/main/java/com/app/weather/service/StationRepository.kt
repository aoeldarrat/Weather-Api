package com.app.weather.service

import WeatherService
import android.net.ConnectivityManager
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.app.weather.model.StationFeature
import kotlinx.coroutines.flow.Flow


interface StationRepository {
    fun getStations(
        pageSize: Int = 10,
        initialLoadSize: Int = 10,
        maxCacheSize: Int = 20
    ): Flow<PagingData<StationFeature>>
}

class StationRepositoryImpl(
    val weatherService: WeatherService
): StationRepository, ConnectivityManager.NetworkCallback()  {
    override fun getStations(
        pageSize: Int,
        initialLoadSize: Int,
        maxCacheSize: Int
    ): Flow<PagingData<StationFeature>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
                maxSize = maxCacheSize
            ),
            pagingSourceFactory = {
                StationPagingSource(weatherService)
            }
        ).flow
    }
}