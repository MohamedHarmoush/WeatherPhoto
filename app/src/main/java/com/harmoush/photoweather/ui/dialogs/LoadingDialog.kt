package com.harmoush.photoweather.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.harmoush.photoweather.R

class LoadingDialog(context: Context) : Dialog(context, R.style.LoadingDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_dialog)
        setCancelable(false)
    }
}