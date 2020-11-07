package com.harmoush.photoweather.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
Created by Harmoush on 2020-11-07 
*/

@Parcelize
data class LocationCoordinate(val lon: Float, val lat: Float) : Parcelable