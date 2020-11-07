package com.harmoush.photoweather.data.model

import android.os.Parcelable
import com.harmoush.photoweather.BuildConfig
import kotlinx.android.parcel.Parcelize

/*
Created by Harmoush on 2020-11-07 
*/

@Parcelize
data class Weather(val id: Int, val main: String, val description: String, val icon: String) :
    Parcelable {

    fun getIconUrl() = "${BuildConfig.BASE_IMAGE_URL}$icon.png"
}