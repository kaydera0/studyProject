package com.example.task7tracker.utils

import android.util.Log
import com.example.task7tracker.dataClasses.LocationData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseUtils {
    private val firestoreDB = Firebase.firestore

    fun cloudSave(collectionName: String, locationData: LocationData) {
        val locationInfo = hashMapOf(
            "longitude" to locationData.longitude,
            "latitude" to locationData.latitude,
            "date" to locationData.date,
            "time" to locationData.time
        )
        firestoreDB.collection(collectionName)
            .add(locationInfo)
            .addOnSuccessListener { documentReference ->
                Log.d("DATABASE_TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("DATABASE_TAG", "Error adding document", e)
            }
    }
}