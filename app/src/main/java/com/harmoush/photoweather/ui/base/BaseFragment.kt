package com.harmoush.photoweather.ui.base

import android.os.Bundle
import android.view.View
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

    override fun onStart() {
        super.onStart()
        getToolbarTitle()?.let {
            getMainActivity()?.setToolbarTitle(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    open fun observeViewModel() {

    }

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
        if (isAdded && loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }

    open fun getToolbarTitle(): String? = null
}