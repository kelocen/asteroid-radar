package dev.kelocen.asteroidradar.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * A [RoomDatabase] for [Asteroid][dev.kelocen.asteroidradar.domain.Asteroid] objects.
 */
@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {

    /**
     * An instance of [AsteroidDao] to insert and retrieve [AsteroidDatabase] data.
     */
    abstract val asteroidDao: AsteroidDao

    /**
     * A companion object for [AsteroidDatabase].
     */
    companion object {

        @Volatile
        private var INSTANCE: AsteroidDatabase? = null

        /**
         * Uses the **Singleton** pattern to initialize and return an instance of [AsteroidDatabase].
         *
         * @param context An instance of the [Context].
         * @return An instance of [AsteroidDatabase].
         */
        fun getInstance(context: Context): AsteroidDatabase {
            synchronized(this) {
                var instance: AsteroidDatabase? = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            AsteroidDatabase::class.java, "asteroids"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

/**
 * A database access object for [AsteroidDatabase].
 */
@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("SELECT * FROM asteroids ORDER BY close_approach_date")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids WHERE close_approach_date = :dateToday ORDER BY close_approach_date")
    fun getTodayAsteroids(dateToday: String): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids WHERE close_approach_date >= :dateToday ORDER BY close_approach_date")
    fun getWeekAsteroids(dateToday: String): LiveData<List<DatabaseAsteroid>>
}