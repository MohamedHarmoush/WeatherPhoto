package com.harmoush.photoweather.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harmoush.photoweather.data.source.local.entity.WeatherPhoto
import com.harmoush.photoweather.data.source.remote.Resource
import com.harmoush.photoweather.data.source.repository.WeatherRepository
import com.harmoush.photoweather.utils.DataUtil

/*
Created by Harmoush on 2020-11-06 
*/

class HistoryViewModel(private val weatherRepo: WeatherRepository) : ViewModel() {

    var historyListLiveData: LiveData<Resource<List<WeatherPhoto>>> =
        MutableLiveData(Resource.success(DataUtil.generateDummyHistoryList()))

    fun getWeatherPhotos() {

    }
}