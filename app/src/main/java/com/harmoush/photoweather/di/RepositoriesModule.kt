package com.harmoush.photoweather.di

import com.harmoush.photoweather.data.source.repository.WeatherRepository
import com.harmoush.photoweather.data.source.repository.WeatherRepositoryImpl
import org.koin.dsl.module

/*
Created by Harmoush on 2020-11-06 
*/

val repositoriesModule = module {
    factory<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }
}