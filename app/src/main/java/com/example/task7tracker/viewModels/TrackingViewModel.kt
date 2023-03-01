package com.example.task7tracker.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task7tracker.dataClasses.LocationData
import com.example.task7tracker.dataClasses.RoomLocationData
import com.example.task7tracker.utils.FirebaseUtils
import com.example.task7tracker.utils.roomUtils.RoomDB
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val roomBaseUtils: RoomDB,
    private val firebaseUtils: FirebaseUtils,
    @ApplicationContext context: Context
) : ViewModel() {

    val networkStatus = MutableLiveData(false)
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var lastKnownLocation: Location? = null
    var locationPermissionGranted = MutableLiveData(false)
    private var latitude = 0.00000
    private var longitude = 0.00000
    private var currentDate = ""
    private var currentTime = ""

    fun saveLocation(collectionName: String, locationData: LocationData) {
        if (networkStatus.value == true) {
            firebaseUtils.cloudSave(collectionName, locationData)
        } else {
            viewModelScope.launch {
                roomBaseUtils.roomLocationDataDao()?.insertUserLocation(RoomLocationData.toLocationDataRoom(locationData))
            }
        }
    }

    fun sendLocationsFromDBToFirebase(collectionName: String) {
        viewModelScope.launch {
            val locationsArray = roomBaseUtils.roomLocationDataDao()?.getUserLocations()
                if (locationsArray?.isNotEmpty()!!) {
                for (i in locationsArray) {
                    firebaseUtils.cloudSave(collectionName, i.toLocationData())
                }
            }
            roomBaseUtils.roomLocationDataDao()?.clearTable()
        }
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(userName: String) {
        try {
            if (locationPermissionGranted.value == true) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener() { task ->
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
                                saveLocation(userName, locationData)
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
}