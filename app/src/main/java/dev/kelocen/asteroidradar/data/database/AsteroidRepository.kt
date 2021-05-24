package dev.kelocen.asteroidradar.data.database

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dev.kelocen.asteroidradar.domain.Asteroid
import dev.kelocen.asteroidradar.domain.PictureOfDay
import dev.kelocen.asteroidradar.network.*
import dev.kelocen.asteroidradar.util.api.isDeviceConnected
import dev.kelocen.asteroidradar.util.api.parseAsteroidsJsonResult
import dev.kelocen.asteroidradar.util.getApiKey
import dev.kelocen.asteroidradar.util.getDateSevenDays
import dev.kelocen.asteroidradar.util.getDateToday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * A repository for asteroid data.
 */
class AsteroidRepository(application: Application) {

    private var appContext = application
    private var asteroidDatabase = AsteroidDatabase.getInstance(application)

    /**
     * Retrieves [DatabaseAsteroid] objects from [AsteroidDatabase] and uses [Transformations]
     * to return [Asteroid] objects.
     */
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(asteroidDatabase.asteroidDao.getAsteroids()) { transformedAsteroids ->
            transformedAsteroids.asDomainModel()
        }

    /**
     * Returns the status of the Near Earth Objects API call.
     */
    private var _asteroidApiStatus = MutableLiveData<AsteroidApiStatus>()
    val asteroidApiStatus: LiveData<AsteroidApiStatus>
        get() = _asteroidApiStatus

    /**
     * Returns the status of the Picture of the Day API call.
     */
    private var _pictureApiStatus = MutableLiveData<PictureApiStatus>()
    val pictureApiStatus: LiveData<PictureApiStatus>
        get() = _pictureApiStatus

    /**
     * Inserts the [DatabaseAsteroid] objects into [AsteroidDatabase].
     */
    suspend fun refreshAsteroids() {
        _asteroidApiStatus.value = AsteroidApiStatus.LOADING
        if (isDeviceConnected(appContext)) {
            withContext(Dispatchers.IO) {
                try {
                    val apiResponse =
                        AsteroidApi.retrofitService.getAsteroids(getDateToday(), getDateSevenDays(),
                                getApiKey())
                    val networkAsteroids = parseAsteroidsJsonResult(JSONObject(apiResponse))
                    asteroidDatabase.asteroidDao.insertAll(*networkAsteroids.asDatabaseModel())
                    _asteroidApiStatus.postValue(AsteroidApiStatus.DONE)
                } catch (e: Exception) {
                    _asteroidApiStatus.postValue(AsteroidApiStatus.ERROR)
                }
            }
        } else {
            _asteroidApiStatus.value = AsteroidApiStatus.NOT_CONNECTED
        }
    }

    /**
     * Returns a [PictureOfDay] object.
     */
    suspend fun getPictureOfDay(): PictureOfDay? {
        var picture: PictureOfDay? = null
        _pictureApiStatus.value = PictureApiStatus.LOADING
        if (isDeviceConnected(appContext)) {
            withContext(Dispatchers.IO) {
                try {
                    picture = PictureApi.retrofitService.getPicture(getApiKey())
                    _pictureApiStatus.postValue(PictureApiStatus.DONE)
                } catch (e: Exception) {
                    _pictureApiStatus.postValue(PictureApiStatus.ERROR)
                }
            }
        } else {
            _pictureApiStatus.value = PictureApiStatus.NOT_CONNECTED
        }
        return picture
    }
}
