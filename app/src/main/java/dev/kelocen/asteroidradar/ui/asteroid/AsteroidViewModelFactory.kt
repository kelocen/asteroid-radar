package dev.kelocen.asteroidradar.ui.asteroid

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AsteroidViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AsteroidViewModel::class.java)) {
            return AsteroidViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class!")
    }
}