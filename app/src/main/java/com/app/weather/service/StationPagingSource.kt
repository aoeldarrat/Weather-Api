package com.app.weather.service

import WeatherService
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.weather.model.StationFeature
import com.app.weather.utilities.Constants

class StationPagingSource(
    val weatherService: WeatherService
): PagingSource<Int, StationFeature>() {
    override fun getRefreshKey(state: PagingState<Int, StationFeature>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(Constants.numOfOffScreenPage)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(Constants.numOfOffScreenPage)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StationFeature> {

        val pageIndex = params.key ?: 1
        val pageSize = params.loadSize

        return try {
            val result = weatherService.getStations("$pageIndex", pageSize)

            LoadResult.Page(
                data = result.features,
                prevKey = if(pageIndex == 1) null else pageIndex - 1 ,
                nextKey = if(result.features.isEmpty()) null else pageIndex + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}