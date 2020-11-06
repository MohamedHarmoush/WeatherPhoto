package com.harmoush.photoweather.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import com.harmoush.photoweather.BR
import com.harmoush.photoweather.data.source.local.entity.WeatherPhoto
import com.harmoush.photoweather.databinding.CellWeatherPhotoBinding
import com.harmoush.photoweather.ui.base.recyclerview.BaseRecyclerViewAdapter
import com.harmoush.photoweather.ui.base.recyclerview.BaseViewHolder
import com.harmoush.photoweather.ui.base.recyclerview.OnItemClickListener

/*
Created by Harmoush on 2020-11-06 
*/

class HistoryAdapter(clickListener: OnItemClickListener<WeatherPhoto>? = null) :
    BaseRecyclerViewAdapter<WeatherPhoto, BaseViewHolder>(clickListener = clickListener) {

    override fun bindCurrentItem(holder: BaseViewHolder, currentItem: WeatherPhoto, position: Int) {
        holder.viewBinding.setVariable(BR.currentItem, currentItem)
    }

    override fun getItemViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        val binding = CellWeatherPhotoBinding.inflate(inflater, parent, false)
        return BaseViewHolder(binding)
    }
}