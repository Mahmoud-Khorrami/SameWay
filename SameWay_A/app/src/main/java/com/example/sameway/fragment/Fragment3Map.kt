package com.example.sameway.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.carto.styles.*
import com.example.sameway.BuildConfig
import com.example.sameway.R
import com.example.sameway.databinding.Fragment3MapBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import org.neshan.common.model.LatLng
import org.neshan.mapsdk.internal.utils.BitmapUtils
import org.neshan.mapsdk.model.Marker
import java.text.DateFormat
import java.util.*
import kotlin.properties.Delegates


@AndroidEntryPoint
class Fragment3Map : Fragment()
{
    lateinit var binding: Fragment3MapBinding
    private var userLocation: Location? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationSettingsRequest: LocationSettingsRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastUpdateTime: String
    private var mRequestingLocationUpdates by Delegates.notNull<Boolean>()
    private var marker: Marker? = null
    private var b = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = Fragment3MapBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //---------------------------------------------

        initLocation()
        startReceivingLocationUpdates()
        binding.myLocation.setOnClickListener { focusOnUserLocation() }

    }

    private fun initLocation()
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        settingsClient = LocationServices.getSettingsClient(requireContext())

        locationCallback = object : LocationCallback()
        {
            override fun onLocationResult(locationResult: LocationResult)
            {
                super.onLocationResult(locationResult)

                userLocation = locationResult.lastLocation
                lastUpdateTime = DateFormat.getTimeInstance().format(Date())

                onLocationChange()
            }
        }

        mRequestingLocationUpdates = false

        locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime= 100
        }

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        locationSettingsRequest = builder.build()
    }

    private fun startReceivingLocationUpdates()
    {

        Dexter.withContext(requireActivity()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener
            {
                override fun onPermissionGranted(response: PermissionGrantedResponse)
                {
                    mRequestingLocationUpdates = true
                    startLocationUpdates()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse)
                {
                    if (response.isPermanentlyDenied)
                    {
                        openSettings()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken)
                {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun openSettings()
    {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun startLocationUpdates()
    {
        settingsClient.checkLocationSettings(locationSettingsRequest).addOnSuccessListener {
            Log.i(TAG, "All location settings are satisfied.")

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return@addOnSuccessListener
            }

            fusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

            onLocationChange()

        }.addOnFailureListener {
            when ((it as ApiException).statusCode)
            {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                {
                    Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " + "location settings ")
                    try
                    {
                        if (mRequestingLocationUpdates && b)
                        {
                            b = false
                            val builder = AlertDialog.Builder(context)
                            builder.setMessage("موقعیت مکانی شما غیرفعال است. آیا مایلید آن را فعال کنید؟")
                                .setCancelable(false)
                                .setPositiveButton("بله") { dialog, _ ->
                                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                                    dialog.cancel()
                                    b = true
                                }
                                .setNegativeButton("خیر") { dialog, _ ->
                                    dialog.cancel()
                                    mRequestingLocationUpdates = false
                                    b = true
                                }
                            val alert: AlertDialog = builder.create()
                            alert.show()
                        }

                    } catch (sie: IntentSender.SendIntentException)
                    {
                        Log.i(TAG, "PendingIntent unable to execute request.")
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                {
                    val errorMessage =
                        "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                    Log.e(TAG, errorMessage)
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
            onLocationChange()
        }
    }

    private fun stopLocationUpdates()
    {
        // Removing location updates
        fusedLocationClient?.removeLocationUpdates(locationCallback)?.addOnCanceledListener {
            Toast.makeText(context, "Location updates stopped!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onLocationChange()
    {
        if (userLocation != null)
        {
            addUserMarker(LatLng(userLocation!!.latitude, userLocation!!.longitude))
        }
    }

    private fun addUserMarker(loc: LatLng)
    {
        if (marker != null)
            binding.mapview.removeMarker(marker)

        val markStCr = MarkerStyleBuilder()
        markStCr.size = 30f
        markStCr.bitmap = BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_marker))
        val markSt = markStCr.buildStyle()
        marker = Marker(loc, markSt)
        binding.mapview.addMarker(marker)
    }

    private fun focusOnUserLocation()
    {
        if (userLocation != null)
        {
            binding.mapview.moveCamera(LatLng(userLocation!!.latitude, userLocation!!.longitude),
                0.25f)
            binding.mapview.setZoom(15F, 0.25f)
        }
    }

    companion object
    {
        private const val TAG = "Fragment3Map"
    }

    override fun onResume()
    {
        super.onResume()

        startLocationUpdates()
    }

    override fun onPause()
    {
        super.onPause()

        stopLocationUpdates()
    }
}