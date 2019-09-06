package com.sgztech.rastreamento.core

import android.app.Application
import androidx.room.Room
import com.sgztech.rastreamento.database.AppDatabase

open class CoreApplication: Application() {

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "my-db").build()
    }
}