package com.harmoush.photoweather.ui.base.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/*
Created by Harmoush on 2020-11-06 
*/

open class BaseViewHolder(var viewBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(viewBinding.root)