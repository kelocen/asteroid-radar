package dev.kelocen.asteroidradar.ui.asteroid

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kelocen.asteroidradar.data.database.AsteroidRepository
import dev.kelocen.asteroidradar.domain.Asteroid
import dev.kelocen.asteroidradar.domain.PictureOfDay
import dev.kelocen.asteroidradar.network.AsteroidApiStatus
import dev.kelocen.asteroidradar.network.PictureApiStatus
import kotlinx.coroutines.launch

/**
 * A [ViewModel] subclass for [Asteroid] data.
 */
class AsteroidViewModel(application: Application) : ViewModel() {

    /**
     * An instance of [AsteroidRepository] used to retrieve [Asteroid] data.
     */
    private var asteroidRepository = AsteroidRepository(application)

    /**
     * Retrieves the status of Near Earth Object API requests made by [AsteroidRepository].
     */
    val asteroidApiStatus: LiveData<AsteroidApiStatus> = asteroidRepository.asteroidApiStatus

    /**
     * Retrieves the status of Picture of the Day API requests made by [AsteroidRepository].
     */
    val pictureApiStatus: LiveData<PictureApiStatus> = asteroidRepository.pictureApiStatus

    /**
     * Retrieves the list of [Asteroid] objects from the repository.
     */
    private var _asteroidsLiveData = asteroidRepository.asteroids
    val asteroidsLiveData: LiveData<List<Asteroid>>
        get() = _asteroidsLiveData

    /**
     * Retrieves the [PictureOfDay] from the repository.
     */
    private var _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    /**
     * Used to assign selected [Asteroid] objects.
     */
    private var _navigateToAsteroidDetail = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetail
        get() = _navigateToAsteroidDetail

    init {
        initializeAsteroids()
    }

    private fun initializeAsteroids() {
        refreshAsteroidRepository()
        refreshPictureOfDay()
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
     * Refreshes NASA's **Picture of the Day**.
     */
    fun refreshPictureOfDay() {
        viewModelScope.launch {
            _pictureOfDay.value = asteroidRepository.getPictureOfDay()
        }
    }

    /**
     * Handler for opening selected asteroids with the [DetailFragment][dev.kelocen.asteroidradar.ui.detail.DetailFragment].
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
}