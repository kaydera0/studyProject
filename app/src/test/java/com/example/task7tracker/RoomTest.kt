package com.example.task7tracker


import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.task7tracker.dataClasses.RoomLocationData
import com.example.task7tracker.utils.roomUtils.RoomDB
import com.example.task7tracker.utils.roomUtils.RoomLocationDataDao
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class RoomTest {

    lateinit var context: Context
    lateinit var db: RoomDB
    lateinit var locationDao: RoomLocationDataDao

    @Before
    fun setupDatabase() {
        context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.databaseBuilder(context, RoomDB::class.java, RoomDB.DB_NAME)
            .build()
        locationDao = db.roomLocationDataDao()!!
    }
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
    @Test
    fun testInsertRoom() = runBlocking {
        db.roomLocationDataDao()?.insertUserLocation(RoomLocationData(1,1.0,1.0,"",""))
        Assert.assertEquals(locationDao.getUserLocations().size, 1)
    }
    @Test
    fun testClearRoom() = runBlocking {
        db.roomLocationDataDao()?.clearTable()
        Assert.assertEquals(locationDao.getUserLocations().size, 0)
    }
}