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
     * Adds a new logging tree and starts the work background task.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    /**
     * Configures a recurring background job to refresh [Asteroid][dev.kelocen.asteroidradar.domain.Asteroid] data.
     */
    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<AsteroidWork>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
                AsteroidWork.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest)
    }
}