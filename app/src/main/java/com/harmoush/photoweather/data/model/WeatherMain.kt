package com.harmoush.photoweather.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/*
Created by Harmoush on 2020-11-07 
*/

@Parcelize
data class WeatherMain(
    val temp: Float,
    @SerializedName("feels_like") val feelsLike: Float,
    @SerializedName("temp_min") val minTemp: Float,
    @SerializedName("temp_max") val maxTemp: Float,
    val pressure: Int,
    val humidity: Int
) : Parcelable