package com.sgztech.rastreamento.core

import android.app.Application
import com.sgztech.rastreamento.di.dbModule
import com.sgztech.rastreamento.di.repositoryModule
import com.sgztech.rastreamento.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class CoreApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CoreApplication)
            modules(listOf(dbModule, repositoryModule, uiModule))
        }
    }
}