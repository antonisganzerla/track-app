package com.sgztech.rastreamento.di

import com.sgztech.rastreamento.database.AppDatabase
import com.sgztech.rastreamento.repository.TrackObjectRepository
import com.sgztech.rastreamento.viewmodel.TrackObjectViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dbModule = module {
    single { AppDatabase.getInstance(
        context = get()
    )}
    factory { get<AppDatabase>().trackObjectDao()}
}

val repositoryModule = module {
    single { TrackObjectRepository(get()) }
}

val uiModule = module {
    viewModel { TrackObjectViewModel(get()) }
}