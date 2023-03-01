package com.example.task7tracker.utils.roomUtils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.task7tracker.dataClasses.RoomLocationData

@Database(
    entities = [RoomLocationData::class],
    version = 1
)
abstract class RoomDB : RoomDatabase() {
    abstract fun roomLocationDataDao(): RoomLocationDataDao?
}