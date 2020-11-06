package com.harmoush.photoweather.utils

import com.harmoush.photoweather.data.source.local.entity.WeatherPhoto

object DataUtil {
    fun generateDummyHistoryList(): List<WeatherPhoto> {
        val list = ArrayList<WeatherPhoto>()
        list.add(
            WeatherPhoto(
                "test",
                "https://www.nasa.gov/sites/default/files/thumbnails/image/smap-weather.jpg"
            )
        )
        list.add(
            WeatherPhoto(
                "test",
                "https://media.nationalgeographic.org/assets/photos/000/263/26383.jpg"
            )
        )
        list.add(
            WeatherPhoto(
                "test",
                "https://media.nationalgeographic.org/assets/photos/000/263/26384.jpg"
            )
        )
        list.add(
            WeatherPhoto(
                "test",
                "https://media.nationalgeographic.org/assets/photos/000/263/26380.jpg"
            )
        )
        return list
    }

}
