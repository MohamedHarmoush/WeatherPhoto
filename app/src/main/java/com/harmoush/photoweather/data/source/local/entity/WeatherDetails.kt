package com.harmoush.photoweather.data.source.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.harmoush.photoweather.data.model.WeatherInfo
import kotlinx.android.parcel.Parcelize

/*
Created by Harmoush on 2020-11-07 
*/

@Entity(tableName = "WeatherDetails")
@Parcelize
data class WeatherDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val temp: Int,
    val minTemp: Int,
    val maxTemp: Int,
    val feelsLike: Int,
    val windSpeed: Int,
    val location: String,
    val lat: Float,
    val lon: Float,
    val time: Int
) : Parcelable {
    constructor(weatherInfo: WeatherInfo) : this(
        0,
        weatherInfo.main.temp.toInt(),
        weatherInfo.main.minTemp.toInt(),
        weatherInfo.main.maxTemp.toInt(),
        weatherInfo.main.feelsLike.toInt(),
        weatherInfo.wind.speed.toInt(),
        "${weatherInfo.name}, ${weatherInfo.sys.country}",
        weatherInfo.coord.lat,
        weatherInfo.coord.lon,
        weatherInfo.dt
    )
}