package com.harmoush.photoweather.ui.weatherphoto

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.harmoush.photoweather.BR
import com.harmoush.photoweather.data.model.LocationCoordinate
import com.harmoush.photoweather.databinding.FragmentWeatherPhotoBinding
import com.harmoush.photoweather.ui.base.BaseFragment
import com.harmoush.photoweather.ui.weatherphoto.camera.CameraInteractionListener
import com.harmoush.photoweather.ui.weatherphoto.camera.UiProvider
import com.harmoush.photoweather.utils.*
import com.voctag.android.model.Status.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/*
Created by Harmoush on 2020-11-07 
*/

class WeatherPhotoFragment : BaseFragment(), UiProvider {

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
        cameraManager = CustomCameraManager(requireContext(), lifecycle, lifecycleScope, this,
            object : CameraInteractionListener {
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
            })

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

    override fun getTextureView(): TextureView {
        return binding.textureWeatherPhoto
    }

    override fun showProgress() {
        binding.pbLoading.show()
    }

    override fun hideProgress() {
        binding.pbLoading.hide()
    }

    private fun initUi() {
        binding.btnCapturePhoto.setOnClickListener {
            showProgress()
            cameraManager.capturePhoto()
        }
        binding.btnSharePhoto.setOnClickListener {
            shareWeatherPhoto()
        }
    }

    private fun shareWeatherPhoto() {
        showProgress()
        lifecycleScope.launch {
            val imageUri = withContext(lifecycleScope.coroutineContext + Dispatchers.IO) {
                val bitmap = binding.clWeatherPhoto.createBitmap()
                saveImage(bitmap)
            }
            hideProgress()
            imageUri?.let { shareImageUri(imageUri) }
        }
    }

    private fun saveImage(image: Bitmap): Uri? {
        //TODO - Should be processed in another thread
        val imagesFolder = File(context?.cacheDir, SHARED_IMAGES_FOLDER_NAME)
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, SHARED_IMAGE_NAME)
            val stream = FileOutputStream(file)
            image.compress(CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(
                requireContext(), "${requireContext().packageName}.provider", file
            )
        } catch (e: IOException) {
            Log.d("com.test", "IOException while trying to write file for sharing: ${e.message}")
        }
        return uri
    }

    private fun shareImageUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/png"

        startActivity(intent)
    }

    companion object {
        private const val SHARED_IMAGE_NAME = "shared_image.png"
        private const val SHARED_IMAGES_FOLDER_NAME = "images"
    }
}