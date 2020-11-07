package com.harmoush.photoweather.ui.weatherphoto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.harmoush.photoweather.databinding.FragmentWeatherPhotoBinding
import com.harmoush.photoweather.ui.base.BaseFragment
import com.harmoush.photoweather.utils.autoCleared

/*
Created by Harmoush on 2020-11-07 
*/

class WeatherPhotoFragment : BaseFragment() {

    private var binding by autoCleared<FragmentWeatherPhotoBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherPhotoBinding.inflate(inflater, container, false)
        initUi()
        return binding.root
    }

    private fun initUi() {

    }
}