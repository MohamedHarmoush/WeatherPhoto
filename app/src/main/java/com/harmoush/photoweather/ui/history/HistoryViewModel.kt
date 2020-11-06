package com.harmoush.photoweather.ui.history

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harmoush.photoweather.data.source.local.entity.WeatherPhoto
import com.harmoush.photoweather.utils.DataUtil
import com.voctag.android.model.Resource

/*
Created by Harmoush on 2020-11-06 
*/

class HistoryViewModel(app: Application) : AndroidViewModel(app) {

    var historyListLiveData: LiveData<Resource<List<WeatherPhoto>>> =
        MutableLiveData(Resource.success(DataUtil.generateDummyHistoryList()))

    fun showToast() {
        Toast.makeText(getApplication(), "Go Next", Toast.LENGTH_SHORT).show()
    }
}