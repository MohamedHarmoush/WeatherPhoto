package com.harmoush.photoweather.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harmoush.photoweather.BuildConfig
import com.harmoush.photoweather.data.source.local.dao.WeatherDao
import com.harmoush.photoweather.data.source.local.entity.WeatherDetails
import com.harmoush.photoweather.data.source.local.entity.WeatherPhoto

/*
Created by Harmoush on 2020-11-06 
*/

@Database(
    entities = [WeatherPhoto::class, WeatherDetails::class],
    version = BuildConfig.DATABASE_VERSION_CODE,
    exportSchema = true
)

abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}