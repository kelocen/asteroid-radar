package dev.kelocen.asteroidradar.util

import android.util.Base64
import android.util.Base64.URL_SAFE
import dev.kelocen.asteroidradar.BuildConfig
import dev.kelocen.asteroidradar.data.models.Asteroid
import dev.kelocen.asteroidradar.data.models.DatabaseAsteroid
import java.nio.charset.StandardCharsets

/**
 * Retrieves the API key from the build config, decodes it from base 64, and returns it as
 * a string.
 */
fun getApiKey(): String {
    if (!BuildConfig.isKeyEncoded) {
        return BuildConfig.API_KEY
    }
    val encodedKey = BuildConfig.API_KEY
    val bytes = Base64.decode(encodedKey, URL_SAFE)
    return String(bytes, StandardCharsets.UTF_8)
}

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