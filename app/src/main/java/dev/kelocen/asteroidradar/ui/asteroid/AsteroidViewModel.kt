package dev.kelocen.asteroidradar.ui.asteroid

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kelocen.asteroidradar.data.database.AsteroidDatabase
import dev.kelocen.asteroidradar.data.database.AsteroidRepository
import dev.kelocen.asteroidradar.data.models.Asteroid
import dev.kelocen.asteroidradar.data.models.PictureOfDay
import kotlinx.coroutines.launch

/**
 * A [ViewModel] subclass for [Asteroid] data.
 */
class AsteroidViewModel(application: Application) : ViewModel() {

    private var asteroidDatabase = AsteroidDatabase.getInstance(application)
    private var asteroidRepository = AsteroidRepository(asteroidDatabase)

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
    }

    private fun initializeAsteroids() {
        refreshAsteroidRepository()
    }

    /**
     * Refreshes [AsteroidRepository] to update the list of [Asteroid] objects.
     */
    private fun refreshAsteroidRepository() {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }

    /**
     * Handler for opening selected asteroids in the [dev.kelocen.asteroidradar.ui.detail.DetailFragment]
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