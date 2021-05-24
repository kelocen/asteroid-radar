package dev.kelocen.asteroidradar.network

import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.kelocen.asteroidradar.domain.PictureOfDay
import dev.kelocen.asteroidradar.util.Constants
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * An [Enum] for displaying the status of Near Earth Object (NEO) API requests.
 */
enum class AsteroidApiStatus { LOADING, ERROR, DONE, NOT_CONNECTED }

/**
 * An [Enum] for displaying the status of Astronomy Picture of the Day API requests.
 */
enum class PictureApiStatus { LOADING, ERROR, DONE, NOT_CONNECTED }

/**
 * An [OkHttpClient] object to monitor API requests.
 */
private val httpClient: OkHttpClient = OkHttpClient().newBuilder()
    .addInterceptor(OkHttpProfilerInterceptor())
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

/**
 * A Moshi converter object used by the Retrofit service.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * An interface that exposes the getAsteroidsAsync method.
 */
interface AsteroidApiService {
    /**
     * Returns a [JSONObject] that contains asteroid data.
     */
    @GET(Constants.NEO_FEED)
    suspend fun getAsteroids(@Query(Constants.START_DATE) startDate: String,
                             @Query(Constants.END_DATE) endDate: String,
                             @Query(Constants.API_KEY) apiKey: String): String
}

/**
 * An interface that exposes the getPictureAsync method.
 */
interface PictureApiService {
    /**
     * Returns a [PictureOfDay] object.
     */
    @GET(Constants.IMAGE_OF_DAY)
    suspend fun getPicture(@Query(Constants.API_KEY) apiKey: String): PictureOfDay
}

/**
 * A lazy-initialized object that exposes the Retrofit service.
 */
object AsteroidApi {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(httpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}

/**
 * A lazy-initialized object that exposes the Retrofit service.
 */
object PictureApi {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    val retrofitService: PictureApiService by lazy {
        retrofit.create(PictureApiService::class.java)
    }
}

