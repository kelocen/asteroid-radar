package dev.kelocen.asteroidradar.util.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.kelocen.asteroidradar.data.models.PictureOfDay
import dev.kelocen.asteroidradar.util.Constants
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * An [Enum] for displaying the status of Near Earth Object API requests.
 */
enum class AsteroidApiStatus { LOADING, ERROR, DONE, NOT_CONNECTED }

/**
 * An [OkHttpClient] object to monitor API requests.
 */
private val httpClient: OkHttpClient = OkHttpClient().newBuilder()
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

private val retrofit: Retrofit = Retrofit.Builder()
    .client(httpClient)
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

/**
 * An interface that exposes the getAsteroidsAsync method.
 */
interface AsteroidApiService {
    /**
     * Returns a [JSONObject] from that contains asteroid data.
     */
    @GET(Constants.NEO_FEED)
    fun getAsteroidsAsync(@Query(Constants.API_KEY) apiKey: String): Deferred<JSONObject>
}

/**
 * An interface that exposes the getPictureAsync method.
 */
interface PictureApiService {
    /**
     * Returns a [PictureOfDay] object.
     */
    @GET(Constants.IMAGE_OF_DAY)
    fun getPictureAsync(@Query(Constants.API_KEY) apiKey: String): Deferred<PictureOfDay>
}

/**
 * A lazy-initialized object that exposes the Retrofit service.
 */
object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}

/**
 * A lazy-initialized object that exposes the Retrofit service.
 */
object PictureApi {
    val retrofitService: PictureApiService by lazy {
        retrofit.create(PictureApiService::class.java)
    }
}

