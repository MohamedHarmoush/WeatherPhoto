package com.harmoush.photoweather.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
Created by Harmoush on 2020-11-06 
*/

@Parcelize
data class WeatherInfo(
    val coord: LocationCoordinate,
    val weather: List<Weather>,
    val base: String,
    val main: WeatherMain,
    val wind: Wind,
    val dt: Int,
    val sys: WeatherSys,
    val name: String, //city name
    val id: String // city id
) : Parcelable