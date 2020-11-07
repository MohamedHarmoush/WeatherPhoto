package com.harmoush.photoweather.ui.weatherphoto

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.harmoush.photoweather.BR
import com.harmoush.photoweather.data.model.LocationCoordinate
import com.harmoush.photoweather.databinding.FragmentWeatherPhotoBinding
import com.harmoush.photoweather.ui.base.BaseFragment
import com.harmoush.photoweather.ui.weatherphoto.camera.CameraInteractionListener
import com.harmoush.photoweather.ui.weatherphoto.camera.UiProvider
import com.harmoush.photoweather.utils.*
import com.voctag.android.model.Status.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

/*
Created by Harmoush on 2020-11-07 
*/

class WeatherPhotoFragment : BaseFragment(), CameraInteractionListener, UiProvider {

    private val viewModel by viewModel<WeatherPhotoViewModel>()
    private var binding by autoCleared<FragmentWeatherPhotoBinding>()
    private var locationManager by autoCleared<LocationManager>()
    private var cameraManager by autoCleared<CustomCameraManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initCamera()
        initLocationManager()
    }

    private fun initLocationManager() {
        locationManager = LocationManager(requireContext())
        getUserCurrentLocation()
    }

    private fun initCamera() {
        cameraManager = CustomCameraManager(requireContext(), lifecycle, this, this)
        lifecycle.addObserver(cameraManager)
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

    private fun initUi() {
        binding.btnCapturePhoto.setOnClickListener {
            showProgress()
            cameraManager.onPhotoCaptureClicked()
        }
        binding.btnSharePhoto.setOnClickListener {
            toast("Share")
        }
    }

    override fun onPhotoCaptureSuccess(imageFile: File?) {
        hideProgress()
        if (imageFile != null) {
            UiUtil.hideViews(binding.btnCapturePhoto)
            UiUtil.showViews(binding.btnSharePhoto)
        }

        binding.ivCapturedPhoto.loadImage(imageFile)
    }

    override fun onPhotoCaptureFailure(errorMessage: String?) {
        hideProgress()
        showMessage(errorMessage)
    }

    override fun getTextureView(): TextureView {
        return binding.textureWeatherPhoto
    }

}