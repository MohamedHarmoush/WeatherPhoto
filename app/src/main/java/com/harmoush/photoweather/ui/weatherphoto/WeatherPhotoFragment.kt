package com.harmoush.photoweather.ui.weatherphoto

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.harmoush.photoweather.BR
import com.harmoush.photoweather.data.model.LocationCoordinate
import com.harmoush.photoweather.databinding.FragmentWeatherPhotoBinding
import com.harmoush.photoweather.ui.base.BaseFragment
import com.harmoush.photoweather.utils.LocationManager
import com.harmoush.photoweather.utils.autoCleared
import com.harmoush.photoweather.utils.showMessage
import com.voctag.android.model.Status.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/*
Created by Harmoush on 2020-11-07 
*/

class WeatherPhotoFragment : BaseFragment() {

    private val viewModel by viewModel<WeatherPhotoViewModel>()
    private var binding by autoCleared<FragmentWeatherPhotoBinding>()
    private var locationManager by autoCleared<LocationManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = LocationManager(requireContext())
        getUserCurrentLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherPhotoBinding.inflate(inflater, container, false)

        initUi()
        return binding.root
    }

    override fun handleGetUserLocation() {
        locationManager.getUserLocation(object : LocationManager.OnGetLocationListener {
            override fun onSuccess(coordinates: LocationCoordinate) {
                viewModel.updateUserCurrentLocation(coordinates)
            }
        })
    }

    private fun initUi() {

    }

    override fun observeViewModel() {
        viewModel.weatherDetailsResult.observe(viewLifecycleOwner, Observer { res ->
            Log.d("com.test", "weatherDetailsResult> ${res?.status}")
            if (res == null) return@Observer
            when (res.status) {
                SUCCESS -> {
                    hideProgress()
                    binding.setVariable(BR.weather, res.data)
                    binding.executePendingBindings()
                }
                ERROR -> {
                    hideProgress()
                    showMessage(res.message)
                }
                LOADING -> {
                    showProgress()
                }
            }
        })
    }
}