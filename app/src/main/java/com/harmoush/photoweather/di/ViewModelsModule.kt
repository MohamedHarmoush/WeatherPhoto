package com.harmoush.photoweather.di

import com.harmoush.photoweather.ui.history.HistoryViewModel
import com.harmoush.photoweather.ui.weatherphoto.WeatherPhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/*
Created by Harmoush on 2020-11-06 
*/

val viewModelsModule = module {
    viewModel { HistoryViewModel(get()) }
    viewModel { WeatherPhotoViewModel(get()) }
}