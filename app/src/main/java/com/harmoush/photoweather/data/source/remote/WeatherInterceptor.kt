package com.harmoush.photoweather.data.source.remote

import com.harmoush.photoweather.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/*
Created by Harmoush on 2020-11-07 
*/

class WeatherInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentRequest = chain.request()
        val url = currentRequest.url
        val newUrl = url.newBuilder()
            .addQueryParameter(QUERY_PARAM_UNITS, QUERY_UNITS_METRIC)
            .addQueryParameter(QUERY_PARAM_API_KEY, BuildConfig.API_KEY)
            .build()
        val newRequest = currentRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val QUERY_PARAM_UNITS = "units"
        private const val QUERY_PARAM_API_KEY = "appid"
        private const val QUERY_UNITS_METRIC = "metric"
    }
}