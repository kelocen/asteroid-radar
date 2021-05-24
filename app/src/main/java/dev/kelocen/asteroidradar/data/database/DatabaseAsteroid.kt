package dev.kelocen.asteroidradar.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.kelocen.asteroidradar.domain.Asteroid
import kotlinx.android.parcel.Parcelize

/**
 *  A data class for asteroid database objects.
 */
@Parcelize
@Entity(tableName = "asteroids")
data class DatabaseAsteroid(
        @PrimaryKey
        val id: Long,
        @ColumnInfo(name = "code_name")
        val codename: String,
        @ColumnInfo(name = "close_approach_date")
        val closeApproachDate: String,
        @ColumnInfo(name = "absolute_magnitude")
        val absoluteMagnitude: Double,
        @ColumnInfo(name = "estimated_diameter")
        val estimatedDiameter: Double,
        @ColumnInfo(name = "relative_velocity")
        val relativeVelocity: Double,
        @ColumnInfo(name = "distance_from_earth")
        val distanceFromEarth: Double,
        @ColumnInfo(name = "is_potentially_hazardous")
        val isPotentiallyHazardous: Boolean) : Parcelable

/**
 * An extension function that transforms a list of [DatabaseAsteroid] objects into a list of [Asteroid] objects.
 */
fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map { databaseAsteroid ->
        Asteroid(
                id = databaseAsteroid.id,
                codename = databaseAsteroid.codename,
                closeApproachDate = databaseAsteroid.closeApproachDate,
                absoluteMagnitude = databaseAsteroid.absoluteMagnitude,
                estimatedDiameter = databaseAsteroid.estimatedDiameter,
                relativeVelocity = databaseAsteroid.relativeVelocity,
                distanceFromEarth = databaseAsteroid.distanceFromEarth,
                isPotentiallyHazardous = databaseAsteroid.isPotentiallyHazardous
        )
    }
}