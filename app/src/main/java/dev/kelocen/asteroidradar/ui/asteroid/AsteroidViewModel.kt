package dev.kelocen.asteroidradar.ui.asteroid

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kelocen.asteroidradar.data.database.AsteroidRepository
import dev.kelocen.asteroidradar.data.models.Asteroid
import dev.kelocen.asteroidradar.data.models.PictureOfDay
import dev.kelocen.asteroidradar.util.api.AsteroidApiStatus
import kotlinx.coroutines.launch

/**
 * A [ViewModel] subclass for [Asteroid] data.
 */
class AsteroidViewModel(application: Application) : ViewModel() {

    private var asteroidRepository = AsteroidRepository(application)

    val asteroidStatus: LiveData<AsteroidApiStatus> = asteroidRepository.asteroidApiStatus

    private var _asteroidsLiveData = asteroidRepository.asteroids
    val asteroidsLiveData: LiveData<List<Asteroid>>
        get() = _asteroidsLiveData

    private var _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private var _navigateToAsteroidDetail = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetail
        get() = _navigateToAsteroidDetail

    init {
        initializeAsteroids()
        refreshPictureOfDay()
    }

    private fun initializeAsteroids() {
        refreshAsteroidRepository()
    }

    /**
     * Refreshes [AsteroidRepository] to update the list of [Asteroid] objects.
     */
    fun refreshAsteroidRepository() {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }

    /**
     * Handler for opening selected asteroids in the [DetailFragment][dev.kelocen.asteroidradar.ui.detail.DetailFragment].
     */
    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetail.value = asteroid
    }

    /**
     * Resets the click event handler value to null.
     */
    fun onAsteroidNavigated() {
        _navigateToAsteroidDetail.value = null
    }

    /**
     * Retrieves NASA's **Picture of the Day** and assigns it to the [_pictureOfDay] live data.
     */
    fun refreshPictureOfDay() {
        viewModelScope.launch {
            _pictureOfDay.value = asteroidRepository.getPictureOfDay()
        }
    }
}