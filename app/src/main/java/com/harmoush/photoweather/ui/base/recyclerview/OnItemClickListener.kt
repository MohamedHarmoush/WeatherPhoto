package com.harmoush.photoweather.ui.base.recyclerview

import android.view.View

/*
Created by Harmoush on 2020-11-06 
*/

interface OnItemClickListener<T> {
    fun onItemClick(view: View?, item: T)
}