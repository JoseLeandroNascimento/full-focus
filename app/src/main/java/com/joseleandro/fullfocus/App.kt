package com.joseleandro.fullfocus

import android.app.Application
import com.joseleandro.fullfocus.core.di.AppModule.dataModule
import com.joseleandro.fullfocus.core.di.AppModule.uiDomainModule
import com.joseleandro.fullfocus.core.di.AppModule.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(uiModule, dataModule, uiDomainModule)
        }
    }
}