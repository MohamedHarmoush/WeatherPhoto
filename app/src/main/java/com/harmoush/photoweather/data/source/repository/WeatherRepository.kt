package com.harmoush.photoweather.data.source.repository

import androidx.lifecycle.LiveData
import com.harmoush.photoweather.data.model.LocationCoordinate
import com.harmoush.photoweather.data.source.local.entity.WeatherDetails
import com.harmoush.photoweather.data.source.remote.Resource
import kotlinx.coroutines.CoroutineScope

/*
Created by Harmoush on 2020-11-06 
*/

interface WeatherRepository {

    fun getWeatherData(
        scope: CoroutineScope,
        coordinate: LocationCoordinate
    ): LiveData<Resource<WeatherDetails>>
}