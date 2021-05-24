package dev.kelocen.asteroidradar.ui.asteroid

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kelocen.asteroidradar.data.AsteroidRepository
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
     * Retrieves the list of [Asteroid] objects from the repository.
     */
    private var _asteroidsLiveData = asteroidRepository.asteroids
    val asteroidsLiveData: LiveData<List<Asteroid>>?
        get() = _asteroidsLiveData

    /**
     * Retrieves the [PictureOfDay] from the repository.
     */
    private var _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay

    /**
     * Returns the status of the Near Earth Objects API call.
     */
    private var _asteroidApiStatus = MutableLiveData<AsteroidApiStatus?>()
    val asteroidApiStatus: LiveData<AsteroidApiStatus?>
        get() = _asteroidApiStatus

    /**
     * Returns the status of the Picture of the Day API call.
     */
    private var _pictureApiStatus = MutableLiveData<PictureApiStatus?>()
    val pictureApiStatus: LiveData<PictureApiStatus?>
        get() = _pictureApiStatus

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
        refreshPictureOfDay()
        refreshAsteroidRepository()
    }

    /**
     * Refreshes [AsteroidRepository] to update the list of [Asteroid] objects.
     */
    fun refreshAsteroidRepository() {
        _asteroidApiStatus.value = AsteroidApiStatus.LOADING
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
            if (_asteroidsLiveData?.value != null) {
                _asteroidApiStatus.postValue(AsteroidApiStatus.DONE)
            } else {
                _asteroidApiStatus.postValue(AsteroidApiStatus.ERROR)
            }
        }
    }

    /**
     * Refreshes NASA's **Picture of the Day**.
     */
    fun refreshPictureOfDay() {
        _pictureApiStatus.value = PictureApiStatus.LOADING
        viewModelScope.launch {
            _pictureOfDay.postValue(asteroidRepository.getPictureOfDay())
            if (_pictureOfDay.value != null) {
                _pictureApiStatus.postValue(PictureApiStatus.DONE)
            } else {
                _pictureApiStatus.postValue(PictureApiStatus.ERROR)
            }
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