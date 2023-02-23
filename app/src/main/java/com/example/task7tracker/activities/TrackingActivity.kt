package com.example.task7tracker.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.task7tracker.R
import com.example.task7tracker.dataClasses.LocationData
import com.example.task7tracker.databinding.ActivityTrackingBinding
import com.example.task7tracker.utils.NetworkStatusTracker
import com.example.task7tracker.viewModels.TrackingViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TrackingActivity : AppCompatActivity(), OnMapReadyCallback {

    private val vm: TrackingViewModel by viewModels()
    private lateinit var binding: ActivityTrackingBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null
    private var mLocationRequest: LocationRequest? = null
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var latitude = 0.00000
    private var longitude = 0.00000
    private var currentDate = ""
    private var currentTime = ""
    private var isTrackingActive = false
    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLocationPermission()

        userName = intent.extras?.get("userName").toString()
        vm.networkStatus.observe(this, androidx.lifecycle.Observer {
            if (it) {
                binding.trackingImage.setImageDrawable(getDrawable(R.drawable.point))
                vm.sendLocationsFromDBToFirebase(userName)
            } else {
                binding.trackingImage.setImageDrawable(getDrawable(R.drawable.point_gps_off))
            }
        })

        NetworkStatusTracker(applicationContext, vm)

        binding.startBtn.setOnClickListener {
            if (!isTrackingActive) {
                isTrackingActive = true
                if (vm.networkStatus.value == true) {
                    binding.trackingImage.setImageDrawable(getDrawable(R.drawable.point_active))
                    binding.startBtn.background = getDrawable(R.drawable.btn_tracking)
                    binding.startBtn.text = getText(R.string.stop)
                    binding.startBtn.setTextColor(resources.getColor(R.color.black))

                    CoroutineScope(Dispatchers.Main).launch {
                        while (isTrackingActive) {
                            getDeviceLocation()

                            binding.progressBar.setProgress(100.0f, true, 3000)
                            delay(3000)
                            binding.progressBar.setProgress(0.0f)
                        }
                    }
                } else {
                    Toast.makeText(this, "Please turn on internet", Toast.LENGTH_SHORT).show()
                }
            } else {
                isTrackingActive = false
                binding.trackingImage.setImageDrawable(getDrawable(R.drawable.point))
                binding.startBtn.background = getDrawable(R.drawable.btn_sign_enable)
                binding.startBtn.text = getText(R.string.start)
                binding.startBtn.setTextColor(resources.getColor(R.color.white))
                binding.progressBar.setProgress(0.0f)
            }
        }
        binding.exitBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation == null)
                            Log.d("DeviceLocation", "task successful but last location is null")
                        if (lastKnownLocation != null) {
                            if (latitude - lastKnownLocation!!.latitude > 0.00001 || longitude - lastKnownLocation!!.longitude > 0.00001) {
                                latitude = lastKnownLocation!!.latitude
                                longitude = lastKnownLocation!!.longitude
                                currentDate = SimpleDateFormat(
                                    "dd-MM-yyyy",
                                    Locale.getDefault()
                                ).format(Date())
                                currentTime =
                                    SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                                val locationData =
                                    LocationData(longitude, latitude, currentDate, currentTime)
                                vm.saveLocation(userName, locationData)
                            }
                        }
                    } else {
                        Log.d("DeviceLocation", "Current location is null. Using defaults.")
                        Log.e("DeviceLocation", "Exception: %s", task.exception)
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }
}