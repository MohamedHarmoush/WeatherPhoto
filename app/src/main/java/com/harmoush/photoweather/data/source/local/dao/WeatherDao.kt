package com.harmoush.photoweather.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.harmoush.photoweather.data.source.local.entity.WeatherDetails
import com.harmoush.photoweather.data.source.local.entity.WeatherPhoto

/*
Created by Harmoush on 2020-11-06 
*/

@Dao
interface WeatherDao : BaseDao<WeatherDetails> {

    @Query("SELECT * FROM WeatherDetails WHERE lat =:lat AND lon =:lon")
    fun getWeatherData(lat: Float, lon: Float): LiveData<WeatherDetails>

    @Insert
    fun insertWeatherPhoto(weatherPhoto: WeatherPhoto): Long

    @Query("SELECT * FROM WeatherPhotos")
    fun getWeatherPhotos(): LiveData<List<WeatherPhoto>>
}