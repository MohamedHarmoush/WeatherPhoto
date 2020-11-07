package com.harmoush.photoweather.ui.weatherphoto

import androidx.lifecycle.*
import com.harmoush.photoweather.data.model.LocationCoordinate
import com.harmoush.photoweather.data.source.local.entity.WeatherDetails
import com.harmoush.photoweather.data.source.local.entity.WeatherPhoto
import com.harmoush.photoweather.data.source.remote.Resource
import com.harmoush.photoweather.data.source.repository.WeatherRepository
import com.harmoush.photoweather.utils.AbsentLiveData
import java.io.File

/*
Created by Harmoush on 2020-11-07 
*/

class WeatherPhotoViewModel(private val weatherRepo: WeatherRepository) : ViewModel() {

    var sharedFile: File? = null
    private val coordinateLiveData by lazy {
        MutableLiveData<LocationCoordinate>()
    }
    var weatherDetailsResult: LiveData<Resource<WeatherDetails>> =
        coordinateLiveData.switchMap { coordinate ->
            if (coordinate == null) {
                AbsentLiveData.create()
            } else {
                weatherRepo.getWeatherData(viewModelScope, coordinate)
            }
        }

    fun updateUserCurrentLocation(coordinates: LocationCoordinate) {
        coordinateLiveData.value = coordinates
    }

    fun insertSharedWeatherPhoto() {
        val file = sharedFile ?: return
        val weatherPhoto = WeatherPhoto(file.name, file.path)
        weatherRepo.insertWeatherPhoto(viewModelScope, weatherPhoto)
    }
}