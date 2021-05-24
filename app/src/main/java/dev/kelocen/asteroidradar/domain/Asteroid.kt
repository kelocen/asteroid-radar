package dev.kelocen.asteroidradar.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A data class for asteroid domain objects.
 */
@Parcelize
data class Asteroid(
        val id: Long,
        val codename: String,
        val closeApproachDate: String,
        val absoluteMagnitude: Double,
        val estimatedDiameter: Double,
        val relativeVelocity: Double,
        val distanceFromEarth: Double,
        val isPotentiallyHazardous: Boolean) : Parcelable