package dev.kelocen.asteroidradar.ui.asteroid

import android.app.Application
import androidx.lifecycle.*
import dev.kelocen.asteroidradar.data.AsteroidRepository
import dev.kelocen.asteroidradar.data.database.AsteroidDatabase
import dev.kelocen.asteroidradar.domain.Asteroid
import dev.kelocen.asteroidradar.domain.PictureOfDay
import dev.kelocen.asteroidradar.network.AsteroidApiStatus
import dev.kelocen.asteroidradar.network.PictureApiStatus
import dev.kelocen.asteroidradar.ui.detail.DetailFragment
import kotlinx.coroutines.launch

/**
 * An enum class to filter the asteroids on the [Asteroid] screen.
 */
enum class AsteroidFilter { SHOW_TODAY, SHOW_WEEK, SHOW_ALL }

/**
 * A [ViewModel] subclass for [Asteroid] data.
 */
class AsteroidViewModel(application: Application) : ViewModel() {

    /**
     * An instance of [AsteroidRepository] used to retrieve [Asteroid] data.
     */
    private var asteroidRepository = AsteroidRepository(application)

    /**
     * A [Boolean] that's set to true if the **API_KEY** variable in the **key.properties** file
     * is not blank or empty. This variable is used to determine if ViewModel classes should
     * call [AsteroidRepository.refreshAsteroids] and [AsteroidRepository.getPictureOfDay].
     */
    var hasApiKey = asteroidRepository.hasApiKey

    /**
     * A [LiveData] list of [Asteroid] objects for the recycler view.
     */
    private var _recyclerAsteroids = MutableLiveData<List<Asteroid>?>()
    val recyclerAsteroids: LiveData<List<Asteroid>?>
        get() = _recyclerAsteroids

    /**
     * An [Observer] for [AsteroidViewModel] that monitors changes to [asteroidsLiveData] and
     * updates [_recyclerAsteroids] as necessary.
     */
    private val asteroidViewModelObserver = Observer<List<Asteroid>> { updatedAsteroids ->
        _recyclerAsteroids.value = updatedAsteroids
    }

    /**
     * A [LiveData] list of [Asteroid] objects for [AsteroidViewModel] that is used to retrieve
     * data from [AsteroidRepository] and observed by [asteroidViewModelObserver].
     */
    private lateinit var asteroidsLiveData: LiveData<List<Asteroid>>

    /**
     * A [LiveData] object for [PictureOfDay].
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
     * Used to assign selected [Asteroid] objects for the [DetailFragment] screen.
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
        getSelectedAsteroids(AsteroidFilter.SHOW_WEEK)
    }

    /**
     * Used to specify the [AsteroidFilter] in the menu on the [AsteroidFragment] screen.
     */
    fun updateAsteroidSelection(selection: AsteroidFilter?) {
        getSelectedAsteroids(selection)
    }

    /**
     * Retrieves a list of [Asteroid] objects from [AsteroidDatabase] using the specified filter.
     */
    private fun getSelectedAsteroids(selection: AsteroidFilter?) {
        asteroidsLiveData = asteroidRepository.getSelectedAsteroids(selection)
        asteroidsLiveData.observeForever(asteroidViewModelObserver)
    }

    /**
     * Refreshes [AsteroidRepository] to update the list of [Asteroid] objects.
     */
    fun refreshAsteroidRepository() {
        if (hasApiKey) { // If no API key is found, method doesn't run.
            viewModelScope.launch {
                _asteroidApiStatus.postValue(AsteroidApiStatus.LOADING)
                asteroidRepository.refreshAsteroids()
                if (asteroidsLiveData.value != null) {
                    _asteroidApiStatus.postValue(AsteroidApiStatus.DONE)
                } else {
                    _asteroidApiStatus.postValue(AsteroidApiStatus.ERROR)
                }
            }
        }
    }

    /**
     * Refreshes the [PictureOfDay].
     */
    fun refreshPictureOfDay() {
        if (hasApiKey) { // If no API key is found, method doesn't run.
            viewModelScope.launch {
                _pictureApiStatus.postValue(PictureApiStatus.LOADING)
                _pictureOfDay.postValue(asteroidRepository.getPictureOfDay())
                if (_pictureOfDay.value != null) {
                    _pictureApiStatus.postValue(PictureApiStatus.DONE)
                } else {
                    _pictureApiStatus.postValue(PictureApiStatus.ERROR)
                }
            }
        }
    }

    /**
     * Handler for opening selected asteroids with the [DetailFragment].
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

    override fun onCleared() {
        super.onCleared()
        asteroidsLiveData.removeObserver(asteroidViewModelObserver)
    }
}