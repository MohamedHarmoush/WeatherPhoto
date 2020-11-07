package com.harmoush.photoweather.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
Created by Harmoush on 2020-11-07 
*/

@Parcelize
data class WeatherSys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Int,
    val sunset: Int
) : Parcelable