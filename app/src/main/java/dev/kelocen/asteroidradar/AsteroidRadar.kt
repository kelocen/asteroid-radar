@file:Suppress("unused")

package dev.kelocen.asteroidradar

import android.app.Application
import timber.log.Timber

class AsteroidRadar : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}