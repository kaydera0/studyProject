package com.example.task7tracker.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.task7tracker.dataClasses.LocationData
import com.example.task7tracker.utils.FeedReaderContract.FeedEntry.DATE
import com.example.task7tracker.utils.FeedReaderContract.FeedEntry.LATITUDE
import com.example.task7tracker.utils.FeedReaderContract.FeedEntry.LONGITUDE
import com.example.task7tracker.utils.FeedReaderContract.FeedEntry.TABLE_NAME
import com.example.task7tracker.utils.FeedReaderContract.FeedEntry.TIME

class DataBaseUtils(context: Context) {

    private val dataBaseHelper = DataBaseHelper(context)
    var db: SQLiteDatabase? = null

    fun saveToDB(locationData: LocationData) {
        db = dataBaseHelper.writableDatabase
        val values = ContentValues().apply {
            put(LONGITUDE, locationData.longitude)
            put(LATITUDE, locationData.latitude)
            put(DATE, locationData.date)
            put(TIME, locationData.time)
        }
        db?.insert(TABLE_NAME, null, values)

    }

    @SuppressLint("Range")
    fun readFromDB(): ArrayList<LocationData> {
        val arrayList = ArrayList<LocationData>()
        db = dataBaseHelper.readableDatabase
        val cursor = db?.query(TABLE_NAME, null, null, null, null, null, null)
        with(cursor) {
            while (this?.moveToNext()!!) {
                val locationData = LocationData(
                    cursor?.getDouble(cursor.getColumnIndex(LONGITUDE))!!,
                    cursor.getDouble(cursor.getColumnIndex(LATITUDE)),
                    cursor.getString(cursor.getColumnIndex(DATE))!!,
                    cursor.getString(cursor.getColumnIndex(TIME))!!
                )
                arrayList.add(locationData)
            }
        }
        return arrayList
    }
}