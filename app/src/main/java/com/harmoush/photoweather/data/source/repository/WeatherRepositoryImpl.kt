package com.harmoush.photoweather.data.source.repository

import androidx.lifecycle.LiveData
import com.harmoush.photoweather.data.model.LocationCoordinate
import com.harmoush.photoweather.data.model.WeatherInfo
import com.harmoush.photoweather.data.source.local.dao.WeatherDao
import com.harmoush.photoweather.data.source.local.entity.WeatherDetails
import com.harmoush.photoweather.data.source.remote.ApiResponse
import com.harmoush.photoweather.data.source.remote.ApiService
import com.harmoush.photoweather.data.source.remote.NetworkBoundResource
import com.harmoush.photoweather.data.source.remote.Resource
import kotlinx.coroutines.CoroutineScope

/*
Created by Harmoush on 2020-11-06 
*/

class WeatherRepositoryImpl(
    private val weatherDao: WeatherDao,
    private val apiService: ApiService
) : WeatherRepository {

    override fun getWeatherData(
        scope: CoroutineScope,
        coordinate: LocationCoordinate
    ): LiveData<Resource<WeatherDetails>> {
        return object : NetworkBoundResource<WeatherDetails, WeatherInfo>(scope) {
            override fun saveCallResult(item: WeatherInfo) {
                weatherDao.insert(WeatherDetails(item,coordinate))
            }

            override fun shouldFetch(data: WeatherDetails?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<WeatherDetails> {
                return weatherDao.getWeatherData(coordinate.lat, coordinate.lon)
            }

            override fun createCall(): LiveData<ApiResponse<WeatherInfo>> {
                return apiService.getWeatherData(coordinate.lat, coordinate.lon)
            }

        }.asLiveData()
    }
}