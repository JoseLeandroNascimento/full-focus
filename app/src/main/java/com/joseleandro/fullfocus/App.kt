package com.joseleandro.fullfocus

import android.app.Application
import com.joseleandro.fullfocus.core.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(AppModule.uiModule)
        }
    }
}