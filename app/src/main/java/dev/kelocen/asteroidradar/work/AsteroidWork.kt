package dev.kelocen.asteroidradar.work

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.kelocen.asteroidradar.data.AsteroidRepository
import retrofit2.HttpException

/**
 * A [CoroutineWorker] subclass to refresh the [Asteroid][dev.kelocen.asteroidradar.domain.Asteroid]
 * cache for [AsteroidDatabase][dev.kelocen.asteroidradar.data.database.AsteroidDatabase].
 */
class AsteroidWork(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    /**
     * Refreshes the [AsteroidDatabase][dev.kelocen.asteroidradar.data.database.AsteroidDatabase] cache.
     */
    override suspend fun doWork(): Result {
        val repository = AsteroidRepository(applicationContext)
        return try {
            repository.cleanUpAsteroids()
            repository.refreshAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        } catch (e: SQLiteException) {
            Result.retry()
        }
    }

    /**
     * A companion object for [AsteroidWork].
     */
    companion object {
        const val WORK_NAME = "AsteroidWorker"
    }
}