package com.example.task7tracker.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations_room")
data class RoomLocationData(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val longitude: Double,
    val latitude: Double,
    val date: String,
    val time: String
) {
    fun toLocationData(): LocationData = LocationData(
        longitude = longitude,
        latitude = latitude,
        date = date,
        time = time
    )

    companion object {
        fun toLocationDataRoom(locationData: LocationData): RoomLocationData = RoomLocationData(
            id = 0,
            longitude = locationData.longitude,
            latitude = locationData.latitude,
            date = locationData.date,
            time = locationData.time
        )
    }
}