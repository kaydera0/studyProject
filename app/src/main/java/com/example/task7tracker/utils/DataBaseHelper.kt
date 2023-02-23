package com.example.task7tracker.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.task7tracker.utils.FeedReaderContract.FeedEntry.DATABASE_NAME
import com.example.task7tracker.utils.FeedReaderContract.FeedEntry.DATABASE_VERSION
import com.example.task7tracker.utils.FeedReaderContract.FeedEntry.SQL_CREATE_TABLE
import com.example.task7tracker.utils.FeedReaderContract.FeedEntry.SQL_DELETE_TABLE

class DataBaseHelper(val context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_TABLE)
        onCreate(db)
    }

    fun onDelete(db: SQLiteDatabase?) {
        db?.execSQL(SQL_DELETE_TABLE)
    }
}

object FeedReaderContract {
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "LOCATIONS"
        const val LATITUDE = "LATITUDE"
        const val LONGITUDE = "LONGITUDE"
        const val DATE = "DATE"
        const val TIME = "TIME"
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "TrackerDataBase.db"
        const val SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS ${TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${LATITUDE} REAL," +
                    "${LONGITUDE} REAL," +
                    "${DATE} TEXT," +
                    "${TIME} TEXT)"
        const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS ${TABLE_NAME}"
    }
}
