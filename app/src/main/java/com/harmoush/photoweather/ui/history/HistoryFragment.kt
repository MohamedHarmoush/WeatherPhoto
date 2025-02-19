package com.harmoush.photoweather.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.harmoush.photoweather.R
import com.harmoush.photoweather.data.source.local.entity.WeatherPhoto
import com.harmoush.photoweather.databinding.FragmentHistoryBinding
import com.harmoush.photoweather.ui.base.BaseFragment
import com.harmoush.photoweather.utils.autoCleared
import com.harmoush.photoweather.utils.hide
import com.harmoush.photoweather.utils.show
import com.harmoush.photoweather.utils.showMessage
import com.voctag.android.model.Status.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : BaseFragment() {

    private val viewModel by viewModel<HistoryViewModel>()
    private var adapter by autoCleared<HistoryAdapter>()
    private var binding by autoCleared<FragmentHistoryBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        initUi()
        return binding.root
    }

    override fun observeViewModel() {
        viewModel.historyWeatherPhotosResult.observe(viewLifecycleOwner, Observer { res ->
            when (res.status) {
                LOADING -> {
                    showProgress()
                }
                SUCCESS -> {
                    hideProgress()
                    res?.data?.let {
                        handleSuccessResponse(it)
                    }
                }
                ERROR -> {
                    hideProgress()
                    showMessage(res.message)
                }
            }
        })
    }

    override fun handleCameraPermissionGranted() {
        navigate(R.id.weatherPhotoFragment)
    }

    private fun initUi() {
        setupRecyclerView()
        binding.fabTakePhoto.setOnClickListener {
            checkCameraPermission()
        }
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter()
        binding.rvHistory.adapter = adapter
        binding.rvHistory.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
    }

    private fun handleSuccessResponse(list: List<WeatherPhoto>) {
        if (list.isEmpty()) binding.viewNoData.root.show()
        else binding.viewNoData.root.hide()

        adapter.updateList(list)
    }
}