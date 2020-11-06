package com.harmoush.photoweather.data.source.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/*
Created by Harmoush on 2020-11-06 
*/

@Entity(tableName = "WeatherPhotos")
@Parcelize
class WeatherPhoto(@PrimaryKey val name: String, val localPath: String) : Parcelable