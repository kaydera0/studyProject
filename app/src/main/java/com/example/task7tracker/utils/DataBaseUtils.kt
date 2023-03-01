package com.example.task7tracker.utils

import android.content.Context
import androidx.room.Room
import com.example.task7tracker.dataClasses.LocationData
import com.example.task7tracker.dataClasses.RoomLocationData
import com.example.task7tracker.utils.roomUtils.RoomDB

class DataBaseUtils(context: Context) {

    private val dbRoom = Room.databaseBuilder(
        context,
        RoomDB::class.java, "database-name"
    ).build()
    private val locationDataDao = dbRoom.roomLocationDataDao()

    suspend fun saveToDBRoom(locationData: LocationData) {
        val roomLocationData = RoomLocationData.toLocationDataRoom(locationData)
        locationDataDao?.insertUserLocation(roomLocationData)
    }

    suspend fun readFromDBRoom(): ArrayList<LocationData>? {
        val locationDao = locationDataDao?.getUserLocations()
        val locations = ArrayList<LocationData>()
        for (i in locationDao!!) {
            locations.add(i.toLocationData())
        }
        return locations
    }

    suspend fun clearDBRoom() {
        locationDataDao?.clearTable()
    }
}