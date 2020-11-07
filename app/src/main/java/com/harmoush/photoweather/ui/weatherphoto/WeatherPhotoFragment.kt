package com.harmoush.photoweather.ui.weatherphoto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.harmoush.photoweather.BR
import com.harmoush.photoweather.data.model.LocationCoordinate
import com.harmoush.photoweather.databinding.FragmentWeatherPhotoBinding
import com.harmoush.photoweather.ui.base.BaseFragment
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherPhotoBinding.inflate(inflater, container, false)

        initUi()
        return binding.root
    }

    private fun initUi() {
        viewModel.getWeatherDetails(LocationCoordinate(29.92f, 31.2f))
    }

    override fun observeViewModel() {
        viewModel.weatherDetailsResult.observe(viewLifecycleOwner, Observer { res ->
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