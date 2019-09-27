package com.sgztech.rastreamento.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sgztech.rastreamento.dao.TrackObjectDao
import com.sgztech.rastreamento.model.TrackObject

@Database(entities = [TrackObject::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackObjectDao(): TrackObjectDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "my-db"
                ).build()
            }
            return instance!!
        }
    }
}