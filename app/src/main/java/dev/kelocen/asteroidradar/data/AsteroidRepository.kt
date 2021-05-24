package dev.kelocen.asteroidradar.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import dev.kelocen.asteroidradar.data.database.AsteroidDatabase
import dev.kelocen.asteroidradar.data.database.DatabaseAsteroid
import dev.kelocen.asteroidradar.data.database.asDomainModel
import dev.kelocen.asteroidradar.domain.Asteroid
import dev.kelocen.asteroidradar.domain.PictureOfDay
import dev.kelocen.asteroidradar.network.AsteroidApi
import dev.kelocen.asteroidradar.network.PictureApi
import dev.kelocen.asteroidradar.network.asDatabaseModel
import dev.kelocen.asteroidradar.util.api.parseAsteroidsJsonResult
import dev.kelocen.asteroidradar.util.getApiKey
import dev.kelocen.asteroidradar.util.getDateSevenDays
import dev.kelocen.asteroidradar.util.getDateToday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

/**
 * A repository for asteroid data.
 */
class AsteroidRepository(application: Application) {

    private var asteroidDatabase = AsteroidDatabase.getInstance(application)

    /**
     * Retrieves [DatabaseAsteroid] objects from [AsteroidDatabase] and uses [Transformations]
     * to return [Asteroid] objects.
     */
    var asteroids: LiveData<List<Asteroid>>? =
        Transformations.map(asteroidDatabase.asteroidDao.getAsteroids()) { transformedAsteroids ->
            transformedAsteroids.asDomainModel()
        }

    /**
     * Retrieves a response from the API, parses the results to [DatabaseAsteroid] objects, and inserts them into [AsteroidDatabase].
     */
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val apiResponse =
                    AsteroidApi.retrofitService.getAsteroids(getDateToday(), getDateSevenDays(),
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
}
