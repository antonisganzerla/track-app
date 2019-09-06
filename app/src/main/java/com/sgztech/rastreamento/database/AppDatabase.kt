package com.sgztech.rastreamento.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sgztech.rastreamento.dao.TrackObjectDao
import com.sgztech.rastreamento.model.TrackObject

@Database(entities = [TrackObject::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackObjectDao(): TrackObjectDao
}