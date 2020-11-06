package com.harmoush.photoweather.ui.base

import android.os.Bundle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.harmoush.photoweather.ui.MainActivity
import com.harmoush.photoweather.ui.dialogs.LoadingDialog
import com.phelat.navigationresult.BundleFragment

/*
Created by Harmoush on 2020-11-06 
*/

open class BaseFragment : BundleFragment() {

    private var loadingDialog: LoadingDialog? = null

    fun navigate(action: Int, bundle: Bundle? = null) {
        findNavController().navigate(action, bundle)
    }

    fun navigate(direction: NavDirections, args: Bundle? = null) {
        findNavController().navigate(direction.actionId, args)
    }

    fun navigateUp() {
        findNavController().navigateUp()
    }

    fun getMainActivity() = activity as MainActivity?

    open fun showProgress() {
        hideProgress()

        loadingDialog = LoadingDialog(requireContext())
        if (isAdded) {
            loadingDialog?.show()
        }
    }

    open fun hideProgress() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }
}