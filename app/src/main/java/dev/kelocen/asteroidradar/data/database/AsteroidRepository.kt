package dev.kelocen.asteroidradar.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import dev.kelocen.asteroidradar.data.models.Asteroid
import dev.kelocen.asteroidradar.data.models.DatabaseAsteroid
import dev.kelocen.asteroidradar.util.asDomainModel

/**
 * A repository for asteroid data.
 */
class AsteroidRepository(private val database: AsteroidDatabase) {

    /**
     * [DatabaseAsteroid] test objects for the database.
     */
    private val asteroid2420262 = DatabaseAsteroid(2420262, "420262 (2011 KD11)",
            "2021-5-01", 20.17,
            0.5495914639, 12.8824212683,
            0.2324830307, false)
    private val asteroid3638505 = DatabaseAsteroid(3638505, "(2013 JX28)",
            "2021-05-01", 20.1,
            0.5675968529, 12.9798591125,
            0.2852109848, true)
    private val asteroid3831469 = DatabaseAsteroid(3831469, "(2018 TV2)",
            "2021-05-01", 20.8,
            0.411187571, 14.9001934529,
            0.2233126811, false)
    private val asteroid3843359 = DatabaseAsteroid(3843359, "(2019 OH1)",
            "2021-04-01", 22.5,
            0.1879489824, 7.7979798223,
            0.2774095599, true)
    private val asteroid3892272 = DatabaseAsteroid(3892272, "(2019 VR)",
            "2021-05-01", 28.1,
            0.0142573883, 11.3395047436,
            0.0905681376, false)
    private val asteroid3892702 = DatabaseAsteroid(3892702, "(2019 VT3)",
            "2021-05-01", 28.2,
            0.0136157002, 5.9367178539,
            0.030909189, true)
    private val asteroid3991588 = DatabaseAsteroid(3991588, "(2020 CQ2)",
            "2021-05-01", 28.6,
            0.0113250461, 6.0135394439,
            0.2060363578, false)


    /**
     * List of [DatabaseAsteroid] objects for testing.
     */
    private val testAsteroids =
        arrayOf(asteroid2420262, asteroid3638505, asteroid3831469, asteroid3843359, asteroid3892272,
                asteroid3892702, asteroid3991588)


    /**
     * Inserts the [DatabaseAsteroid] objects into [AsteroidDatabase]
     */
    suspend fun refreshAsteroids() {
        database.asteroidDao.insertAll(*testAsteroids)
    }

    /**
     * Retrieves [DatabaseAsteroid] objects from [AsteroidDatabase] and uses [Transformations]
     * to return [Asteroid] objects.
     */
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) { transformedAsteroids ->
            transformedAsteroids.asDomainModel()
        }
}
