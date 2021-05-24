package dev.kelocen.asteroidradar.network

import dev.kelocen.asteroidradar.data.database.DatabaseAsteroid
import dev.kelocen.asteroidradar.domain.Asteroid

/**
 * A data class for [Asteroid] objects retrieved from the API.
 */
data class NetworkAsteroid(
        val id: Long,
        val codename: String,
        val closeApproachDate: String,
        val absoluteMagnitude: Double,
        val estimatedDiameter: Double,
        val relativeVelocity: Double,
        val distanceFromEarth: Double,
        val isPotentiallyHazardous: Boolean)

/**
 * An extension function that transforms a list of [NetworkAsteroid] objects into a list of [DatabaseAsteroid] objects.
 */
fun List<NetworkAsteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return map { asteroid ->
        DatabaseAsteroid(
                id = asteroid.id,
                codename = asteroid.codename,
                closeApproachDate = asteroid.closeApproachDate,
                absoluteMagnitude = asteroid.absoluteMagnitude,
                estimatedDiameter = asteroid.absoluteMagnitude,
                relativeVelocity = asteroid.relativeVelocity,
                distanceFromEarth = asteroid.distanceFromEarth,
                isPotentiallyHazardous = asteroid.isPotentiallyHazardous
        )
    }.toTypedArray()
}