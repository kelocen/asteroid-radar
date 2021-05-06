package dev.kelocen.asteroidradar.util

import dev.kelocen.asteroidradar.data.models.Asteroid
import dev.kelocen.asteroidradar.data.models.DatabaseAsteroid

/**
 * An extension function that transforms a list of [DatabaseAsteroid] objects into a list of [Asteroid] objects
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
