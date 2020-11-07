package com.harmoush.photoweather.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harmoush.photoweather.data.source.local.entity.WeatherPhoto
import com.harmoush.photoweather.data.source.remote.Resource
import com.harmoush.photoweather.data.source.repository.WeatherRepository

/*
Created by Harmoush on 2020-11-06 
*/

class HistoryViewModel(private val weatherRepo: WeatherRepository) : ViewModel() {

    val historyWeatherPhotosResult: LiveData<Resource<List<WeatherPhoto>>> =
        weatherRepo.getWeatherPhotos(viewModelScope)
}