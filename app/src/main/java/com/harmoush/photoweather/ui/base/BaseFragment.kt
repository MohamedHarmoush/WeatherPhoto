package com.harmoush.photoweather.ui.base

import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.harmoush.photoweather.R
import com.harmoush.photoweather.ui.MainActivity
import com.harmoush.photoweather.ui.dialogs.LoadingDialog
import com.harmoush.photoweather.utils.LocationManager
import com.harmoush.photoweather.utils.showMessage
import com.phelat.navigationresult.BundleFragment
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/*
Created by Harmoush on 2020-11-06 
*/

open class BaseFragment : BundleFragment(), EasyPermissions.PermissionCallbacks {

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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == RC_LOCATION_PERMISSIONS) {
            showMessage(getString(R.string.location_permission_rationale))
        } else {
            showMessage(getString(R.string.external_storage__permission_rationale))
        }
    }

    @AfterPermissionGranted(RC_LOCATION_PERMISSIONS)
    open fun getUserCurrentLocation() {
        val locationPermissions = LocationManager.getLocationPermissions()
        if (EasyPermissions.hasPermissions(requireContext(), *locationPermissions)) {
            handleGetUserLocation()
        } else {
            val rationale = getString(R.string.location_permission_rationale)
            EasyPermissions.requestPermissions(
                this, rationale, RC_LOCATION_PERMISSIONS, *locationPermissions
            )
        }
    }

    open fun handleGetUserLocation() {

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

    companion object {
        private const val RC_LOCATION_PERMISSIONS = 3003
        private const val RC_EXTERNAL_STORAGE_PERMISSIONS = 3004
    }
}