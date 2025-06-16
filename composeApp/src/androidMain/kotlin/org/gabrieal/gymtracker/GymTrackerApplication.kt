package org.gabrieal.gymtracker

import android.app.Application
import org.gabrieal.gymtracker.data.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class GymTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@GymTrackerApplication)
            androidLogger()
            modules()
        }
    }
}
