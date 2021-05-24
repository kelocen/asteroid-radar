package dev.kelocen.asteroidradar.work

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.kelocen.asteroidradar.data.AsteroidRepository
import retrofit2.HttpException

/**
 * A [CoroutineWorker] subclass to refresh the [Asteroid][dev.kelocen.asteroidradar.domain.Asteroid]
 * cache for [AsteroidDatabase][dev.kelocen.asteroidradar.data.database.AsteroidDatabase].
 */
class AsteroidWork(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private var appContext = context.applicationContext

    /**
     * A companion object for [AsteroidWork].
     */
    companion object {
        const val WORK_NAME = "AsteroidWorker"
    }

    /**
     * Refreshes the [AsteroidDatabase][dev.kelocen.asteroidradar.data.database.AsteroidDatabase] cache.
     */
    override suspend fun doWork(): Result {
        val repository = AsteroidRepository(appContext as Application)
        return try {
            repository.refreshAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}