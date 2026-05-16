package com.joseleandro.fullfocus

import android.app.Application
import com.joseleandro.fullfocus.core.di.KoinModule.dataModule
import com.joseleandro.fullfocus.core.di.KoinModule.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(uiModule, dataModule)
        }
    }
}