package com.app.weather.service

import androidx.paging.PagingData
import com.app.weather.model.StationFeature
import kotlinx.coroutines.flow.Flow

class StationUseCase(
    private val stationRepository: StationRepository
) {
    fun fetchPagedStations(): Flow<PagingData<StationFeature>> {
        return stationRepository.getStations(
            pageSize = 10,
            initialLoadSize = 10,
            maxCacheSize = 20
        )
    }
}