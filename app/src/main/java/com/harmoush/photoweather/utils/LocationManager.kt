package com.harmoush.photoweather.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.harmoush.photoweather.data.model.LocationCoordinate

class LocationManager(private val context: Context) {
    private var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest()
    private lateinit var listener: OnGetLocationListener

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {

            locationResult?.lastLocation?.let {
                fusedLocationProviderClient.removeLocationUpdates(this)
                listener.onSuccess(LocationCoordinate(it.latitude, it.longitude))
            }
        }
    }

    init {
        locationRequest.apply {
            interval = LONGEST_INTERVAL
            fastestInterval = LONGEST_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation(listener: OnGetLocationListener) {
        this.listener = listener
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(context as Activity, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d("com.test","LocationManager> error> ${sendEx.message}")
                }
            }
        }
    }

    interface OnGetLocationListener {
        fun onSuccess(coordinates: LocationCoordinate)
    }

    companion object {
        const val REQUEST_CHECK_SETTINGS: Int = 0
        private const val LONGEST_INTERVAL: Long = 10000 //10 seconds

        fun getLocationPermissions(): Array<String> {
            return arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

}