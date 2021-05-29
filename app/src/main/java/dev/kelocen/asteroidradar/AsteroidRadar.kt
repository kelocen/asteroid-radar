@file:Suppress("unused")

package dev.kelocen.asteroidradar

import android.app.Application
import androidx.work.*
import dev.kelocen.asteroidradar.work.AsteroidWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AsteroidRadar : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * Adds a new logging tree and starts the work background tasks.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupDataRefreshWork()
            setupDataCleanWork()
        }
    }

    /**
     * Configures a recurring background job to refresh [Asteroid][dev.kelocen.asteroidradar.domain.Asteroid] data.
     */
    private fun setupDataRefreshWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val dataRefreshRequest = PeriodicWorkRequestBuilder<AsteroidWork>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .addTag(AsteroidWork.WORK_NAME)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
                AsteroidWork.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                dataRefreshRequest)
    }

    /**
     * Configures a recurring background job to clean [Asteroid][dev.kelocen.asteroidradar.domain.Asteroid] data.
     */
    private fun setupDataCleanWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val dataCleanRequest = PeriodicWorkRequestBuilder<AsteroidWork>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
                AsteroidWork.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                dataCleanRequest)
    }
}