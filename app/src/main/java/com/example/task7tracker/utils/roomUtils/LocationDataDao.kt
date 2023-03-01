package com.example.task7tracker.utils.roomUtils

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.task7tracker.dataClasses.LocationData
import com.example.task7tracker.dataClasses.RoomLocationData

@Dao
interface RoomLocationDataDao {

    @Insert
    suspend fun insertUserLocation(roomLocationData: RoomLocationData)

    @Query("SELECT * FROM locations_room")
    suspend fun getUserLocations():List<RoomLocationData>

    @Query("DELETE FROM locations_room")
    suspend fun clearTable()

}