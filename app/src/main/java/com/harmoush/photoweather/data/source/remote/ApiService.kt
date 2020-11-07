package com.harmoush.photoweather.data.source.remote

import androidx.lifecycle.LiveData
import com.harmoush.photoweather.data.model.WeatherInfo
import retrofit2.http.GET
import retrofit2.http.Query

/*
Created by Harmoush on 2020-11-06 
*/

interface ApiService {

    @GET("data/2.5/weather")
    fun getWeatherData(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float
    ): LiveData<ApiResponse<WeatherInfo>>
}