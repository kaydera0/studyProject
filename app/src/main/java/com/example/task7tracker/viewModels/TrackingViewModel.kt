package com.example.task7tracker.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task7tracker.dataClasses.LocationData
import com.example.task7tracker.utils.DataBaseUtils
import com.example.task7tracker.utils.FirebaseUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val dataBaseUtils: DataBaseUtils,
    val firebaseUtils: FirebaseUtils
) : ViewModel() {

    val networkStatus = MutableLiveData(false)

    fun saveLocation(collectionName: String, locationData: LocationData) {
        if (networkStatus.value == true) {
            firebaseUtils.cloudSave(collectionName, locationData)
        } else {
            dataBaseUtils.saveToDB(locationData)
        }
    }

    fun sendLocationsFromDBToFirebase(collectionName: String) {
        val locationsArray = dataBaseUtils.readFromDB()
        if (locationsArray.isNotEmpty()) {
            for (i in locationsArray) {
                firebaseUtils.cloudSave(collectionName, i)
            }
        }
    }
}