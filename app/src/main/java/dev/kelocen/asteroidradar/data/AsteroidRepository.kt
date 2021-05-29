package dev.kelocen.asteroidradar.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import dev.kelocen.asteroidradar.data.database.AsteroidDatabase
import dev.kelocen.asteroidradar.data.database.DatabaseAsteroid
import dev.kelocen.asteroidradar.data.database.asDomainModel
import dev.kelocen.asteroidradar.domain.Asteroid
import dev.kelocen.asteroidradar.domain.PictureOfDay
import dev.kelocen.asteroidradar.network.AsteroidApi
import dev.kelocen.asteroidradar.network.NetworkAsteroid
import dev.kelocen.asteroidradar.network.PictureApi
import dev.kelocen.asteroidradar.network.asDatabaseModel
import dev.kelocen.asteroidradar.util.api.parseAsteroidsJsonResult
import dev.kelocen.asteroidradar.util.getApiKey
import dev.kelocen.asteroidradar.util.getDateNextWeek
import dev.kelocen.asteroidradar.util.getDateToday
import dev.kelocen.asteroidradar.util.getDateYesterday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

/**
 * An enum class to filter the asteroids on the [Asteroid] screen.
 */
enum class AsteroidFilter { SHOW_TODAY, SHOW_WEEK, SHOW_ALL }

/**
 * A repository for asteroid data.
 */
class AsteroidRepository(context: Context) {

    private var asteroidDatabase = AsteroidDatabase.getInstance(context.applicationContext)

    /**
     * Retrieves [DatabaseAsteroid] objects from [AsteroidDatabase] and uses [Transformations]
     * to return [Asteroid] objects.
     */
    fun getSelectedAsteroids(selection: AsteroidFilter?): LiveData<List<Asteroid>> {
        return when (selection) {
            AsteroidFilter.SHOW_TODAY -> {
                Transformations.map(asteroidDatabase.asteroidDao.getTodayAsteroids(getDateToday()))
                { transformedAsteroids -> transformedAsteroids.asDomainModel() }
            }
            AsteroidFilter.SHOW_WEEK -> {
                Transformations.map(asteroidDatabase.asteroidDao.getWeekAsteroids(getDateToday()))
                { transformedAsteroids -> transformedAsteroids.asDomainModel() }
            }
            AsteroidFilter.SHOW_ALL -> {
                Transformations.map(asteroidDatabase.asteroidDao.getAllAsteroids())
                { transformedAsteroids -> transformedAsteroids.asDomainModel() }
            }
            else -> {
                Transformations.map(asteroidDatabase.asteroidDao.getWeekAsteroids(getDateToday()))
                { transformedAsteroids -> transformedAsteroids.asDomainModel() }
            }
        }
    }

    /**
     * Retrieves a response from the API, parses the results to [NetworkAsteroid] objects, converts
     * them to [DatabaseAsteroid] objects, and inserts them into [AsteroidDatabase].
     */
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val apiResponse =
                    AsteroidApi.retrofitService.getAsteroids(getDateToday(), getDateNextWeek(),
                            getApiKey())
                val networkAsteroids = parseAsteroidsJsonResult(JSONObject(apiResponse))
                asteroidDatabase.asteroidDao.insertAll(*networkAsteroids.asDatabaseModel())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    /**
     * Retrieves the [PictureOfDay] from the API.
     */
    suspend fun getPictureOfDay(): PictureOfDay? {
        var picture: PictureOfDay? = null
        withContext(Dispatchers.IO) {
            try {
                picture = PictureApi.retrofitService.getPicture(getApiKey())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        return picture
    }

    /**
     * Deletes the [DatabaseAsteroid] objects from [AsteroidDatabase] older than today.
     */
    suspend fun cleanUpAsteroids() {
        withContext(Dispatchers.IO) {
            asteroidDatabase.asteroidDao.deleteLastWeekAsteroids(getDateYesterday())
        }
    }
}
